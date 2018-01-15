package com.jlu.pipeline.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.pipeline.service.PipelineBuildService;

/**
 * Created by langshiquan on 18/1/14.
 */
@RestController
@RequestMapping("/pipeline/build")
public class PipelineBuildController extends AbstractController {

    @Autowired
    private PipelineBuildService pipelineBuildService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean build(Long pipelineConfId, @RequestParam(required = false, defaultValue = "0") Long triggerId) {
        if (triggerId == 0L) {
            pipelineBuildService.build(pipelineConfId);
        } else {
            pipelineBuildService.build(pipelineConfId, triggerId);
        }
        return ResponseBean.TRUE;
    }

}
