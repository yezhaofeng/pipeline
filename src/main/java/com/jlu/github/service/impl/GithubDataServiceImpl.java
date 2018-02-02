package com.jlu.github.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlu.branch.bean.BranchType;
import com.jlu.branch.model.GithubBranch;
import com.jlu.branch.service.IBranchService;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.utils.HttpClientAuth;
import com.jlu.common.utils.HttpClientUtil;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.github.bean.GithubBranchBean;
import com.jlu.github.bean.GithubFirstCommitBean;
import com.jlu.github.bean.GithubRepoBean;
import com.jlu.github.model.Module;
import com.jlu.github.service.IGitHubCommitService;
import com.jlu.github.service.IGithubDataService;
import com.jlu.github.service.IModuleService;
import com.jlu.user.bean.UserBean;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IUserService;

/**
 * Created by niuwanpeng on 17/3/24.
 */
@Service
public class GithubDataServiceImpl implements IGithubDataService {

    private final Logger logger = LoggerFactory.getLogger(GithubDataServiceImpl.class);

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private IBranchService branchService;

    @Autowired
    private IGitHubCommitService gitHubCommitService;
    @Autowired
    private IUserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubDataServiceImpl.class);
    private static final Gson GSON = new Gson();

    /**
     * 根据用户注册信息初始化用户
     *
     * @param userBean
     * @return
     */
    @Override
    public GithubUser initUser(UserBean userBean) {
        syncReposByUser(userBean);
        List<Module> modules = moduleService.getModulesByUsername(userBean.getUsername());
        for (Module module : modules) {
            this.creatHooks(userBean.getUsername(), module.getRepository(), userBean.getGitHubToken());
        }
        GithubUser githubUser = userBean.toUser();
        githubUser.setPipelineToken(UUID.randomUUID().toString());
        userService.saveUser(githubUser);
        return githubUser;
    }

    /**
     * 根据用户名获得GitHub代码仓库信息并保存
     *
     * @param userBean
     * @return
     */
    private void syncReposByUser(UserBean userBean) {
        String username = userBean.getUsername();
        List<String> reposName = userBean.getSyncRepos();
        String requestRepoUrl = String.format(PipelineConfigReader.getConfigValueByKey("github.repos"), username);
        String result = HttpClientUtil.get(requestRepoUrl, null);
        List<GithubRepoBean> repoList = GSON.fromJson(result, new TypeToken<List<GithubRepoBean>>() {
        }.getType());
        List<Module> modules = this.saveModules(repoList, username, reposName);
        for (int i = 0; modules != null && i < modules.size(); i++) {
            this.initBranch(modules.get(i), username);
        }
    }

    /**
     * 为代码仓库创建hook
     *
     * @param username
     * @param repo
     * @param githubToken
     * @return
     */
    @Override
    public void creatHooks(String username, String repo, String githubToken) {
        String repoUrl = String.format(PipelineConfigReader.getConfigValueByKey("github.all.hooks"), username, repo);
        HttpClientAuth.postForCreateHook(repoUrl, githubToken);
    }

    /**
     * 增加新的模块
     *
     * @param username
     * @param repository
     * @return
     */
    @Override
    public void addModule(String username, String repository) {
        GithubUser githubUser = userService.getUserByName(username);
        if (githubUser == null) {
            throw new PipelineRuntimeException("该用户不存在！请联系管理员。");
        }
        Module moduleInDB = moduleService.get(PipelineUtils.getFullModule(username, repository));
        if (moduleInDB != null) {
            throw new PipelineRuntimeException("该模块已存在，不需要再次配置。");
        }
        LOGGER.info("Start initBuild module:{} on user:{}", moduleInDB, username);
        Module module = new Module(PipelineUtils.getFullModule(username, repository), username, repository);
        moduleService.saveModule(module);
        try {
            LOGGER.info("Start initBuild branch on module:{}, user:{}", moduleInDB, username);
            this.initBranch(module, username);
            LOGGER.info("Start create hook on module:{}, user:{}", moduleInDB, username);
            this.creatHooks(username, repository, githubUser.getGitHubToken());
            LOGGER.info("Add module is successful! module:{}, user:{}", moduleInDB, username);
        } catch (Exception e) {
            moduleService.delete(module);
            throw new PipelineRuntimeException("该仓库不存在，请检查是否配置正确。");
        }
    }

    /**
     * 保存模块数据
     *
     * @param repoList 所有仓库列表
     * @param username
     * @param reposName 应该被同步的仓库列表
     */
    private List<Module> saveModules(List<GithubRepoBean> repoList, String username,
                                     List<String> reposName) {
        List<Module> modules = new ArrayList<>();
        for (int i = 0; repoList != null && i < repoList.size(); i++) {
            String name = repoList.get(i).getName();
            if (!reposName.contains(name)) {
                logger.info("ignore repository {}", name);
                continue;
            }
            String repoName = repoList.get(i).getName();
            Module module = new Module(PipelineUtils.getFullModule(repoName, username), username, repoName);
            modules.add(module);
        }

        moduleService.saveModules(modules);
        return moduleService.getModulesByUsername(username);
    }

    /**
     * 初始化分支
     *
     * @param module
     * @param username
     */
    private void initBranch(Module module, String username) {
        String requestBranchUrl = String.format(PipelineConfigReader.getConfigValueByKey("github.repo.branches"),
                username, module.getModule());
        String result = HttpClientUtil.get(requestBranchUrl, null);
        List<GithubBranchBean> branchBeans = GSON.fromJson(result, new TypeToken<List<GithubBranchBean>>() {
        }.getType());
        saveBranchs(branchBeans, module);
    }

    /**
     * 保存分支数据
     *
     * @param branchBeans
     * @param module
     */
    private void saveBranchs(List<GithubBranchBean> branchBeans, Module module) {
        List<GithubBranch> githubBranches = new ArrayList<>();
        if (githubBranches == null) {
            return;
        }
        for (int i = 0; branchBeans != null && i < branchBeans.size(); i++) {
            GithubBranchBean branchBean = branchBeans.get(i);
            BranchType branchType = branchBean.getName().equals(BranchType.MASTER) ? BranchType.TRUNK : BranchType.BRANCH;
            String branchName = branchBean.getName();
            GithubBranch githubBranch
                    = new GithubBranch(module.getModule(), branchName, branchType);
            githubBranches.add(githubBranch);
        }
        branchService.saveBranches(githubBranches);
    }

    /**
     * 保存一个commit数据，用于触发第一条流水线
     * FIXME: 18/1/28 暂时无法知道commit所属分支
     * @param userName
     * @param module
     * @param branchName
     */
    @Deprecated
    private void initOneCommit(String userName, Module module, String branchName) {
        String requestBranchUrl
                = String.format(PipelineConfigReader.getConfigValueByKey("github.repo.commits"),
                userName, module.getModule());
        String result = HttpClientUtil.get(requestBranchUrl, null);
        List<GithubFirstCommitBean> commits = GSON.fromJson(result, new TypeToken<List<GithubFirstCommitBean>>() {
        }.getType());
        if (commits == null) {
            return;
        }
        GithubFirstCommitBean githubFirstCommitBean = commits.get(0);
        gitHubCommitService.save(githubFirstCommitBean.toGithubCommit());
    }

}
