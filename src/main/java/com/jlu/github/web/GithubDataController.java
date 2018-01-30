package com.jlu.github.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.common.aop.utils.AopTargetUtils;
import com.jlu.github.service.IGitHubHookService;
import com.jlu.github.service.IGithubDataService;
import com.wordnik.swagger.annotations.ApiOperation;

import net.sf.json.JSONObject;

/**
 * Created by niuwanpeng on 17/3/24.
 */
@Controller
@RequestMapping("/github")
public class GithubDataController {
    private final Logger logger = LoggerFactory.getLogger(GithubDataController.class);
    @Autowired
    private IGithubDataService githubDataService;
    @Autowired
    private IGitHubHookService gitHubHookService;

    /**
     * 监听代码提交事件（push），触法流水线
     * @param request
     * @param response
     */
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
                info.append(new String(buffer,0,iRead,"gbk"));
            }
            if (info != null) {
                final JSONObject hookMessage = JSONObject.fromObject(info.toString());
                logger.info("whook-message:{}", hookMessage);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((IGitHubHookService) AopTargetUtils.getTarget(gitHubHookService)).dealHookMessage
                                        (hookMessage);
                        } catch (Exception e) {
                            logger.error("deal hook-message failed,json:{},html.error:", hookMessage, e);
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            logger.error("Resolving hook-message is fail! hook:{}", info.toString());
        }
    }

    /**
     * 配置新的模块
     * @param module
     * @param username
     * @return
     */
    // TODO
    @RequestMapping(value = "/addModule", method = RequestMethod.GET)
    @ResponseBody
    public String addModule(@RequestParam(value = "module") String module,
                            @RequestParam(value = "username") String username) {
        return githubDataService.addModule(username, module);
    }

}
