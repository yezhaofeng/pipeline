package com.jlu.jenkins.service.impl;

import com.jlu.jenkins.exception.JenkinsException;
import com.jlu.jenkins.service.DefaultJenkinsServer;
import com.jlu.plugin.instance.compile.CompileExecutor;
import com.offbytwo.jenkins.JenkinsServer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2019/3/23.
 */
public class JenkinsServerServiceImplTest {

    JenkinsServerServiceImpl jenkinsServerService = new JenkinsServerServiceImpl();

    @Test(expected = JenkinsException.class)
    public void testCancel() throws Exception {
        JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(DefaultJenkinsServer.SERVER_URL, DefaultJenkinsServer.MASTER_USERNAME, DefaultJenkinsServer.MASTER_PASSWORD);
        jenkinsServerService.cancel(jenkinsServer, "compile", 204);
    }
}