package com.jlu.jenkins.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionAdmin;
import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.jenkins.bean.JenkinsConfDTO;
import com.jlu.jenkins.bean.JenkinsJobsBean;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;

/**
 * Created by langshiquan on 18/1/10.
 */
@RestController
@RequestMapping("/pipeline/jenkins/server")
public class JenkinsConfController extends AbstractController {

    @Autowired
    private IJenkinsConfService jenkinsConfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean add(@RequestBody JenkinsConfDTO jenkinsConfDTO) {
        jenkinsConfService.save(jenkinsConfDTO.toJenkinsConf(getLoginUserName()));
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "{jenkinsServerId}", method = RequestMethod.PUT)
    public ResponseBean update(@PathVariable Long jenkinsServerId, @RequestBody JenkinsConfDTO jenkinsConfDTO) {
        JenkinsConf jenkinsConf = jenkinsConfDTO.toJenkinsConf(getLoginUserName());
        jenkinsConf.setId(jenkinsServerId);
        jenkinsConfService.saveOrUpdate(jenkinsConf);
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "{jenkinsServerId}", method = RequestMethod.DELETE)
    public ResponseBean delete(@PathVariable Long jenkinsServerId) {
        jenkinsConfService.delete(jenkinsServerId);
        return ResponseBean.TRUE;
    }

    @PermissionAdmin
    @RequestMapping(value = "{jenkinsServerId}", method = RequestMethod.GET)
    public JenkinsConfDTO get(@PathVariable Long jenkinsServerId) {
        JenkinsConf jenkinsConf = jenkinsConfService.get(jenkinsServerId);
        if (jenkinsConf == null) {
            return null;
        }
        return jenkinsConf.toJenkinsConfDTO();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<JenkinsConf> getAllJenkinsServer(String owner) throws IOException {
        return jenkinsConfService.getConfByCreateUser(owner);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public List<JenkinsJobsBean> getJobs(String owner) throws IOException {
        return jenkinsConfService.getJobsByCreateUser(owner);
    }
}
