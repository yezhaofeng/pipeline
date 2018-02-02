package com.jlu.jenkins.service.impl;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.jlu.jenkins.bean.JenkinsJobsBean;
import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.offbytwo.jenkins.JenkinsServer;

/**
 * Created by langshiquan on 18/1/25.
 */
@RunWith(MockitoJUnitRunner.class)
public class JenkinsConfServiceImplTest {

    @InjectMocks
    JenkinsConfServiceImpl jenkinsConfService = new JenkinsConfServiceImpl();

    @Mock
    private IJenkinsConfDao jenkinsConfDao;

    @Mock
    private IJenkinsServerService jenkinsServerService;

    @Test
    public void testGetByCreateUser() throws IOException, URISyntaxException {
        String createUser = "langshiquan";
        List<JenkinsConf> jenkinsConfs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            JenkinsConf jenkinsConf = new JenkinsConf();
            jenkinsConf.setId(Long.parseLong(String.valueOf(i)));
            jenkinsConf.setServerUrl("http://localhost:8080");
            jenkinsConf.setMasterUser("langshiquan");
            jenkinsConf.setMasterPassword("langshiquan");
            jenkinsConf.setCreateTime(new Date());
            jenkinsConfs.add(jenkinsConf);
        }
        when(jenkinsConfDao.find(Mockito.anyString())).thenReturn(jenkinsConfs);
        Set<String> jobSet = new HashSet<>();
        jobSet.add("job1");
        jobSet.add("job2");
        when(jenkinsServerService.getJobs(Mockito.any(JenkinsServer.class))).thenReturn(jobSet);
        List<JenkinsJobsBean> jenkinsJobsBeanList = jenkinsConfService.getJobsByCreateUser(createUser);
        Assert.assertEquals(1, jenkinsJobsBeanList.size());
    }

}