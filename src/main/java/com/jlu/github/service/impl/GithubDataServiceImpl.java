package com.jlu.github.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jlu.common.utils.HttpClientAuth;
import com.jlu.common.utils.HttpClientUtil;
import com.jlu.common.utils.PipelineReadConfig;
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
    private final static String MESSAGE = "MESSAGE";
    private final static String ADD_MODULE_STATUS = "ADD_MODULE_STATUS";

    /**
     * 根据用户注册信息初始化用户
     *
     * @param userBean
     * @return
     */
    @Override
    public void initUser(UserBean userBean) {
        syncReposByUser(userBean);
        List<Module> modules = moduleService.getModulesByUsername(userBean.getUsername());
        for (Module module : modules) {
            this.creatHooks(userBean.getUsername(), module.getModule(), userBean.getGitHubToken());
        }
        userService.saveUser(userBean.toUser());

    }

    /**
     * 根据用户名获得GitHub代码仓库信息并保存
     *
     * @param userBean
     * @return
     */
    @Override
    public void syncReposByUser(UserBean userBean) {
        String username = userBean.getUsername();
        List<String> reposName = userBean.getSyncRepos();
        String requestRepoUrl = String.format(PipelineReadConfig.getConfigValueByKey("github.repos"), username);
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
    public Map<String, Object> creatHooks(String username, String repo, String githubToken) {
        Map<String, Object> result = new HashMap<>();
        String repoUrl
                = String.format(PipelineReadConfig.getConfigValueByKey("github.all.hooks"), username, repo);
        String resultHook = HttpClientAuth.postForCreateHook(repoUrl, githubToken);
        return result;
    }

    /**
     * 增加新的模块
     *
     * @param username
     * @param module
     * @return
     */
    @Override
    public String addModule(String username, String module) {
        module = StringUtils.substringBeforeLast(module, ".git");
        Map<String, String> result = new HashMap<>();
        result.put(ADD_MODULE_STATUS, "NO");
        GithubUser githubUser = userService.getUserByName(username);
        if (githubUser == null) {
            result.put(MESSAGE, "该用户不存在！请联系管理员！");
        } else {
            Module module1 = moduleService.getModuleByUserAndModule(username, module);
            if (module1 != null) {
                result.put(MESSAGE, "该模块已存在，不需要再次配置！");
            } else {
                LOGGER.info("Start initBuild module:{} on user:{}", module, username);
                Module ciHomeModule = new Module(module, username, new Date());
                moduleService.saveModule(ciHomeModule);
                try {
                    LOGGER.info("Start initBuild branch on module:{}, user:{}", module, username);
                    this.initBranch(ciHomeModule, username);
                    LOGGER.info("Start create hook on module:{}, user:{}", module, username);
                    this.creatHooks(username, module, githubUser.getGitHubToken());
                    LOGGER.info("Add module is successful! module:{}, user:{}", module, username);
                    result.put(ADD_MODULE_STATUS, "OK");
                    result.put(MESSAGE, "仓库" + module + "配置成功!");
                    result.put("MODULE", module);
                } catch (Exception e) {
                    moduleService.delete(ciHomeModule);
                    result.put(MESSAGE, "该仓库不存在!请检查是否配置正确。");
                }
            }
        }
        return GSON.toJson(result);
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
            Module module = new Module(repoList.get(i).getName(), username, new Date());
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
        String requestBranchUrl = String.format(PipelineReadConfig.getConfigValueByKey("github.repo.branches"),
                username, module.getModule());
        String result = HttpClientUtil.get(requestBranchUrl, null);
        List<GithubBranchBean> branchBeans = GSON.fromJson(result, new TypeToken<List<GithubBranchBean>>() {
        }.getType());
        saveBranchs(branchBeans, module, username);
    }

    /**
     * 保存分支数据
     *
     * @param branchBeans
     * @param module
     */
    private void saveBranchs(List<GithubBranchBean> branchBeans, Module module, String username) {
        List<GithubBranch> githubBranches = new ArrayList<>();
        if (githubBranches == null) {
            return;
        }
        for (int i = 0; branchBeans != null && i < branchBeans.size(); i++) {
            GithubBranchBean branchBean = branchBeans.get(i);
            BranchType branchType = branchBean.getName().equals("master") ? BranchType.TRUNK : BranchType.BRANCH;
            GithubBranch githubBranch
                    = new GithubBranch(module.getId(), branchBean.getName(), branchType, new Date());
            githubBranches.add(githubBranch);
            // FIXME: 18/1/28 暂时无法知道commit的哪个分支的
            // this.initOneCommit(username, module, branchBean.getName());
        }
        branchService.saveBranches(githubBranches);
    }

    /**
     * 保存一个commit数据，用于触发第一条流水线
     *
     * @param userName
     * @param module
     * @param branchName
     */
    private void initOneCommit(String userName, Module module, String branchName) {
        String requestBranchUrl
                = String.format(PipelineReadConfig.getConfigValueByKey("github.repo.commits"),
                userName, module.getModule());
        String result = HttpClientUtil.get(requestBranchUrl, null);
        List<GithubFirstCommitBean> commits = GSON.fromJson(result, new TypeToken<List<GithubFirstCommitBean>>() {
        }.getType());
        if (commits == null) {
            return;
        }
        GithubFirstCommitBean githubFirstCommitBean = commits.get(0);

        // FIXME
        gitHubCommitService.save(githubFirstCommitBean.toGithubCommit());
    }

}
