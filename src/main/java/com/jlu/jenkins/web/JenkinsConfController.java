package com.jlu.jenkins.web;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.common.web.ResponseBean;
import com.jlu.common.web.AbstractController;
import com.jlu.jenkins.bean.JenkinsConfDTO;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;

/**
 * Created by langshiquan on 18/1/10.
 */
@Controller
@RequestMapping("/pipeline/jenkins/server")
public class JenkinsConfController extends AbstractController {

    @Autowired
    private IJenkinsConfService jenkinsConfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean add(@RequestBody JenkinsConfDTO jenkinsConfDTO) {
        JenkinsConf jenkinsConf = new JenkinsConf();
        BeanUtils.copyProperties(jenkinsConfDTO, jenkinsConf);
        jenkinsConf.setCreateTime(new Date());
        jenkinsConf.setCreateUser(getLoginUserName());
        jenkinsConf.setLastModifiedUser(getLoginUserName());
        jenkinsConfService.saveOrUpdate(jenkinsConf);
        return ResponseBean.TRUE;
    }
}
