package com.jlu.jenkins.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/4/14.
 */
public class JenkinsConfTest {

    @Test
    public void testSetServerUrl() throws Exception {
        JenkinsConf jenkinsConf = new JenkinsConf();
        String jenkinsUrl = "http://localhost:8080";
        jenkinsConf.setServerUrl(jenkinsUrl);
        assertEquals(jenkinsUrl+"/",jenkinsConf.getServerUrl());
    }
}