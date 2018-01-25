package com.jlu.jenkins.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.jenkins.bean.JenkinsBuildDTO;
import com.jlu.jenkins.service.IJenkinsBuildService;

/**
 * Created by langshiquan on 18/1/10.
 */
@Controller
@RequestMapping("/pipeline/jenkins/build")
public class JenkinsBuildController extends AbstractController {

    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean add(@RequestBody JenkinsBuildDTO jenkinsBuildDTO) throws IOException {

        return ResponseBean.TRUE;
    }
}
