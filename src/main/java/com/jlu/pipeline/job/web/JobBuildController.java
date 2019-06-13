package com.jlu.pipeline.job.web;

import java.util.List;

import com.jlu.pipeline.job.bean.JobRuntimeDTO;
import com.jlu.plugin.runtime.bean.RunTimeBean;
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
 * Created by yezhaofeng on 2019/1/19.
 */
@RestController
@RequestMapping("/pipeline/job/")
public class JobBuildController extends AbstractController {

    @Autowired
    private IJobBuildService jobBuildService;

    @RequestMapping(value = "{jobBuildId}", method = RequestMethod.POST)
    public ResponseBean build(@PathVariable Long jobBuildId, @RequestBody JobRuntimeDTO jobRuntimeDTO) {
        jobBuildService.build(jobBuildId, jobRuntimeDTO.getExecParam(), jobRuntimeDTO.getRuntimePluginParam(), TriggerMode.MANUAL, getLoginUserName());
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "{jobBuildId}/cancel", method = RequestMethod.POST)
    public ResponseBean build(@PathVariable Long jobBuildId) {
        boolean realCanceled = jobBuildService.cancel(jobBuildId);
        if (realCanceled) {
            return ResponseBean.succ("取消成功");
        } else {
            return ResponseBean.succ("中断请求已发出,请稍后");
        }
    }


    @RequestMapping(value = "{jobBuildId}", method = RequestMethod.GET)
    public JobBuildBean buildInfo(@PathVariable Long jobBuildId) {
        return jobBuildService.getWithPluginBuild(jobBuildId);
    }

    @RequestMapping(value = "/runtimeRequire/{jobBuildId}", method = RequestMethod.GET)
    public List<RunTimeBean> runtimeRequire(@PathVariable Long jobBuildId) {
        return jobBuildService.getRuntimeRequire(jobBuildId);
    }
}

