package com.jlu.github.service.impl;

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
import com.jlu.common.utils.DateUtils;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by niuwanpeng on 17/4/19.
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubHookServiceImpl.class);

    private static final Gson GSON = new Gson();

    /**
     * 解析hook信息，触发编译
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
        PipelineConf pipelineConf = pipelineConfService.getPipelineConfBean(gitHubCommit.getOwner(), gitHubCommit.getModule(), gitHubCommit.getBranch());
        pipelineBuildService.build(pipelineConf.getId(), gitHubCommit);

        try {
        } catch (Exception e) {
            LOGGER.error("解析Json数据失败！hookMessage:{}", hookMessage.toString());
        }
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
        HookRepositoryBean repositoryBean = null;
        repositoryBean = GSON.fromJson(hookMessage.getString("repository"),
                new TypeToken<HookRepositoryBean>() {
                }.getType());
        String moduleName = repositoryBean.getName();
        String moduleOwner = repositoryBean.getOwner().getName();
        Module module = moduleService.getModuleByUserAndModule(moduleName, moduleOwner);
        if (module == null) {
            LOGGER.info("This module is not exist and ignore compile!user:{}, module:{}",
                    repositoryBean.getOwner().getName(), repositoryBean.getName());
        }
        String branchName = StringUtils.substringAfterLast(hookMessage.getString("ref"), "refs/heads/");
        BranchType branchType = branchName.equals("master") ? BranchType.TRUNK : BranchType.BRANCH;
        JSONArray commitsArray = hookMessage.getJSONArray("commits");
        String commits = commitsArray.getString(0);
        GitHubCommitBean commitBean = GSON.fromJson(commits, new TypeToken<GitHubCommitBean>() {
        }.getType());

        String commitId = "";

        checkNewBranch(hookMessage, branchName, module);

        gitHubCommit.setCommitId(commitId);
        gitHubCommit.setBranch(branchName);
        gitHubCommit.setModule(moduleName);
        gitHubCommit.setCommitter(commitBean.getCommitter().getName());
        gitHubCommit.setCommitterEmail(commitBean.getCommitter().getEmail());
        gitHubCommit.setOwner(moduleOwner);
        gitHubCommit.setCommitTime(commitBean.getTimestamp());
//        gitHubCommit.setAddedFiles(commitBean.getAdded());
        gitHubCommit.setCommitUrl(commitBean.getUrl());
//        gitHubCommit.setRemovedFiles(commitBean.getRemoved());
//        gitHubCommit.setModifiedFiles(commitBean.getModified());
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
                githubBranch.setBranchType(BranchType.BRANCH);
                githubBranch.setBranchName(branchName);
                githubBranch.setCreateTime(DateUtils.getNowDateFormat());
                githubBranch.setModuleId(module.getId());
                githubBranch.setVersion(branchService.getLastThreeVersion(module));
                branchService.saveBranch(githubBranch);
                LOGGER.info("Create branch:{} is successful!", branchName);
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error("Create branch:{} is fail!", branchName);
        } finally {
            return result;
        }
    }
}
