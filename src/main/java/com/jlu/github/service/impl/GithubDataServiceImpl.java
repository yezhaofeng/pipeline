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
import com.jlu.common.utils.CiHomeReadConfig;
import com.jlu.common.utils.DateUtils;
import com.jlu.common.utils.HttpClientAuth;
import com.jlu.common.utils.HttpClientUtil;
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
    private final static String REGISTER_STATUS = "REGISTER_STATUS";
    private final static String MESSAGE = "MESSAGE";
    private final static String ADD_MODULE_STATUS = "ADD_MODULE_STATUS";

    /**
     * 根据用户注册信息初始化用户
     *
     * @param userBean
     * @return
     */
    @Override
    public Map<String, Object> initUser(UserBean userBean) {
        Map<String, Object> result = new HashMap<>();
        result.put(REGISTER_STATUS, false);
        if (userBean == null) {
            result.put(MESSAGE, "输入信息有误，请重新注册！");
            LOGGER.error("注册失败！The message is userbean:{}", userBean);
        } else {
            try {
                if (userBean.isSyncGithub()) {
                    this.syncReposByUser(userBean.getUsername());
                    List<Module> modules = moduleService.getModulesByUsername(userBean.getUsername());
                    for (Module module : modules) {
                        this.creatHooks(userBean.getUsername(), module.getModule(), userBean.getGitHubToken());
                    }
                }
                result.put(REGISTER_STATUS, true);
                GithubUser githubUser = this.assembleCiHomeUser(userBean);
                userService.saveUser(githubUser);
            } catch (Exception e) {
                LOGGER.error("注册失败！The message is userbean:{},html.error:", userBean, e);
                result.put(MESSAGE, "不可预知错误，请重新注册！");
            }
        }
        return result;
    }

    /**
     * 根据用户名获得GitHub代码仓库信息并保存
     *
     * @param username
     * @return
     */
    @Override
    public boolean syncReposByUser(String username) {
        String requestRepoUrl = String.format(CiHomeReadConfig.getConfigValueByKey("github.repos"), username);
        String result = HttpClientUtil.get(requestRepoUrl, null);
        List<GithubRepoBean> repoList = GSON.fromJson(result, new TypeToken<List<GithubRepoBean>>() {
        }.getType());
        List<Module> modules = this.saveCiHomeModuleByBean(repoList, username);
        // 只初始化最新的3个模块
        for (int i = 0; modules != null && i < modules.size() && i < 3; i++) {
            this.initBranch(modules.get(i), username);
        }
        return true;
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
                = String.format(CiHomeReadConfig.getConfigValueByKey("github.all.hooks"), username, repo);
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
                Module ciHomeModule = new Module(module, username, DateUtils.getNowTimeFormat());
                ciHomeModule.setVersion("1.0.0");
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
     * @param repoList
     * @param username
     */
    private List<Module> saveCiHomeModuleByBean(List<GithubRepoBean> repoList, String username) {
        List<Module> modules = new ArrayList<>();

        for (GithubRepoBean githubRepoBean : repoList) {
            Module module = new Module(githubRepoBean.getName(), username, DateUtils.getNowTimeFormat());
            module.setVersion("1.0.0");
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
        String requestBranchUrl
                = String.format(CiHomeReadConfig.getConfigValueByKey("github.repo.branches"),
                username, module.getModule());
        String result = HttpClientUtil.get(requestBranchUrl, null);
        List<GithubBranchBean> branchBeans = GSON.fromJson(result, new TypeToken<List<GithubBranchBean>>() {
        }.getType());
        this.saveCiHomeBranchByBean(branchBeans, module, username);
    }

    /**
     * 保存分支数据
     *
     * @param branchBeans
     * @param module
     */
    private void saveCiHomeBranchByBean(List<GithubBranchBean> branchBeans, Module module, String username) {
        List<GithubBranch> githubBranches = new ArrayList<>();
        String version = "1.0.0";
        if (githubBranches == null) {
            return;
        }
        for (int i = 0; branchBeans != null && i < branchBeans.size(); i++) {
            GithubBranchBean branchBean = branchBeans.get(i);
            BranchType branchType = branchBean.getName().equals("master") ? BranchType.TRUNK : BranchType.BRANCH;
            GithubBranch githubBranch
                    = new GithubBranch(module.getId(), branchBean.getName(), branchType, new Date());
            githubBranches.add(githubBranch);
            this.initOneCommit(username, module, branchBean.getName());
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
                = String.format(CiHomeReadConfig.getConfigValueByKey("github.repo.commits"),
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

    /**
     * 根据Userbean装配CiHomeUser
     *
     * @param userBean
     * @return
     */
    private GithubUser assembleCiHomeUser(UserBean userBean) {
        GithubUser githubUser = new GithubUser();
        githubUser.setUsername(userBean.getUsername());
        githubUser.setPassword(userBean.getPassword());
        githubUser.setUserEmail(userBean.getEmail());
        githubUser.setCreateTime(new Date());
        githubUser.setGitHubToken(userBean.getGitHubToken());
        return githubUser;
    }

}
