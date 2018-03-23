package com.jlu.github.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.github.model.GitHubCommit;
import com.jlu.github.service.IGitHubCommitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jlu.common.aop.utils.AopTargetUtils;
import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.common.web.ResponseBean;
import com.jlu.github.service.IGitHubHookService;
import com.jlu.github.service.IGithubDataService;

import net.sf.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by langshiquan on 17/3/24.
 */
@Controller
@RequestMapping("/github")
public class GithubDataController {
    private final Logger logger = LoggerFactory.getLogger(GithubDataController.class);
    @Autowired
    private IGithubDataService githubDataService;
    @Autowired
    private IGitHubHookService gitHubHookService;
    @Autowired
    private IGitHubCommitService gitHubCommitService;

    // 尽量不漏掉webHook消息
    private ExecutorService webHookExecutor = Executors.newCachedThreadPool();

    /**
     * 监听代码提交事件（push），触法流水线
     *
     * @param request
     * @param response
     */
    @ApiIgnore
    @PermissionPass
    @RequestMapping(value = "/webHooks", method = RequestMethod.POST)
    @ResponseBody
    public void monitorWebHooks(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer info = new StringBuffer();
        try {
            InputStream in = request.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            int iRead;
            while ((iRead = buf.read(buffer)) != -1) {
                // 防止中文乱码
                info.append(new String(buffer, 0, iRead, "utf-8"));
            }
            if (info != null) {
                final JSONObject hookMessage = JSONObject.fromObject(info.toString());
                logger.info("whook-message:{}", hookMessage);
                webHookExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((IGitHubHookService) AopTargetUtils.getTarget(gitHubHookService)).dealHookMessage
                                    (hookMessage);
                        } catch (Exception e) {
                            logger.error("deal hook-message failed,json:{},html.error:", hookMessage, e);
                        }
                    }
                });
            }
        } catch (IOException e) {
            logger.error("Resolving hook-message is fail! hook:{}", info.toString());
        }
    }

    /**
     * 配置新的模块
     *
     * @param repository
     * @param owner
     * @return
     */
    @RequestMapping(value = "/addModule", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBean addModule(@RequestParam(value = "repository") String repository,
                                  @RequestParam(value = "owner") String owner) {
        githubDataService.addModule(owner, repository);
        return ResponseBean.TRUE;
    }


    @RequestMapping(value = "/{owner}/{repository}/{branchType}/commits", method = RequestMethod.GET)
    @ResponseBody
    public List<GitHubCommit> getBranchesByModule(@PathVariable String owner, @PathVariable String repository,
                                                  @PathVariable BranchType branchType) {
        return gitHubCommitService.get(PipelineUtils.getFullModule(owner, repository), branchType);
    }
}
