package com.jlu.github.service.impl;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlu.branch.bean.BranchType;
import com.jlu.branch.model.GithubBranch;
import com.jlu.branch.service.IBranchService;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.github.bean.GitHubCommitBean;
import com.jlu.github.bean.HookRepositoryBean;
import com.jlu.github.model.GitHubCommit;
import com.jlu.github.model.Module;
import com.jlu.github.service.IGitHubCommitService;
import com.jlu.github.service.IGitHubHookService;
import com.jlu.github.service.IModuleService;
import com.jlu.pipeline.model.PipelineConf;
import com.jlu.pipeline.service.IPipelineBuildService;
import com.jlu.pipeline.service.IPipelineConfService;

import net.sf.json.JSONObject;

/**
 * Created by yezhaofeng on 2019/4/19.
 */
@Service
public class GitHubHookServiceImpl implements IGitHubHookService {

    @Autowired
    private IBranchService branchService;
    @Autowired
    private IPipelineBuildService pipelineBuildService;
    @Autowired
    private IPipelineConfService pipelineConfService;
    @Autowired
    private IModuleService moduleService;
    @Autowired
    private IGitHubCommitService gitHubCommitService;

    private final Logger logger = LoggerFactory.getLogger(GitHubHookServiceImpl.class);

    private static final Gson GSON = new Gson();

    /**
     * 解析hook信息，触发流水线
     *
     * @param hookMessage
     */
    @Override
    public void dealHookMessage(JSONObject hookMessage) {
        GitHubCommit gitHubCommit = getCommitByHook(hookMessage);
        if (gitHubCommit == null) {
            return;
        }
        gitHubCommitService.save(gitHubCommit);
        PipelineConf pipelineConf = pipelineConfService
                .getPipelineConf(gitHubCommit.getModule(), gitHubCommit.getBranch());
        if (pipelineConf == null) {
            logger.info("{} github commit not found pipeline conf", gitHubCommit);
            return;
        }
        pipelineBuildService.build(pipelineConf.getId(), gitHubCommit);
    }

    /**
     * 解析json数据，获得commit
     *
     * @param hookMessage
     * @return
     * @throws Exception
     */
    private GitHubCommit getCommitByHook(JSONObject hookMessage) {
        GitHubCommit gitHubCommit = new GitHubCommit();
        HookRepositoryBean repositoryBean = GSON.fromJson(hookMessage.getString("repository"),
                new TypeToken<HookRepositoryBean>() {
                }.getType());
        String repositoryName = repositoryBean.getName();
        String repositoryOwner = repositoryBean.getOwner().getName();
        String moduleName = PipelineUtils.getFullModule(repositoryOwner, repositoryName);
        Module module = moduleService.get(moduleName);
        if (module == null) {
            logger.info("This module is not exist and ignore message,user:{},repository:{}",
                    repositoryBean.getOwner().getName(), repositoryBean.getName());
            return null;
        }
        String branchName = StringUtils.substringAfterLast(hookMessage.getString("ref"), "refs/heads/");
        BranchType branchType = branchName.equals(BranchType.MASTER) ? BranchType.TRUNK : BranchType.BRANCH;
        String headCommit = hookMessage.getString("head_commit");
        GitHubCommitBean commitBean = GSON.fromJson(headCommit, new TypeToken<GitHubCommitBean>() {
        }.getType());
        checkNewBranch(hookMessage, branchName, module);
        gitHubCommit.setCommitId(commitBean.getId());
        gitHubCommit.setBranch(branchName);
        gitHubCommit.setModule(moduleName);
        gitHubCommit.setCommitter(commitBean.getCommitter().getName());
        gitHubCommit.setCommitterEmail(commitBean.getCommitter().getEmail());
        gitHubCommit.setOwner(repositoryOwner);
        gitHubCommit.setCommitTime(commitBean.getTimestamp());
        gitHubCommit.setAddedFiles(Arrays.asList(commitBean.getAdded()).toString());
        gitHubCommit.setCommitUrl(commitBean.getUrl());
        gitHubCommit.setRemovedFiles(Arrays.asList(commitBean.getRemoved()).toString());
        gitHubCommit.setModifiedFiles(Arrays.asList(commitBean.getModified()).toString());
        gitHubCommit.setCommits(commitBean.getMessage());
        gitHubCommit.setBranchType(branchType);
        return gitHubCommit;
    }


    /**
     * 检查本次提交是否为新的分支，如果是则将该分支写库
     *
     * @param hookMessage
     */
    private boolean checkNewBranch(JSONObject hookMessage, String branchName, Module module) {
        boolean result = false;
        try {
            String status = hookMessage.getString("created");
            if (status.equals("true")) {
                GithubBranch githubBranch = new GithubBranch();
                githubBranch.setBranchType(BranchType.parseType(branchName));
                githubBranch.setBranchName(branchName);
                githubBranch.setCreateTime(new Date());
                githubBranch.setModule(module.getModule());
                branchService.saveBranch(githubBranch);
                logger.info("Create branch:{} is successful!", branchName);
                result = true;
            }
        } catch (Exception e) {
            logger.error("Create branch:{} is fail!", branchName);
        } finally {
            return result;
        }
    }
}
