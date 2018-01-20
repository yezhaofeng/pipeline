package com.jlu.pipeline.job.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.service.IJobBuildService;

/**
 * Created by langshiquan on 18/1/19.
 */
@RestController
@RequestMapping("/pipeline/job/")
public class JobBuildController extends AbstractController {

    @Autowired
    private IJobBuildService jobBuildService;

    @RequestMapping(value = "{jobBuildId}", method = RequestMethod.POST)
    public ResponseBean build(@PathVariable Long jobBuildId, @RequestBody Map<String, Object> execParam) {
        jobBuildService.build(jobBuildId, execParam, TriggerMode.MANUAL, getLoginUserName());
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "{jobBuildId}", method = RequestMethod.GET)
    public JobBuildBean buildInfo(@PathVariable Long jobBuildId) {
        return jobBuildService.getBuildInfo(jobBuildId);
    }

}

