package com.jlu.pipeline.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.pipeline.bean.PipelineBuildBean;
import com.jlu.pipeline.service.IPipelineBuildService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Created by langshiquan on 18/1/14.
 */
@Api(value = "流水线构建")
@RestController
@RequestMapping("/pipeline/build")
public class PipelineBuildController extends AbstractController {

    @Autowired
    private IPipelineBuildService pipelineBuildService;

    @ApiOperation(value = "根据流水线配置id构建流水线", httpMethod = "GET", response = ResponseBean.class, notes =
            "使用triggerId可以使用具体的提交进行构建")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean build(Long pipelineConfId, @RequestParam(required = false, defaultValue = "0") Long triggerId) {
        if (triggerId == 0L) {
            pipelineBuildService.build(pipelineConfId);
        } else {
            pipelineBuildService.build(pipelineConfId, triggerId);
        }
        return ResponseBean.TRUE;
    }

    @ApiOperation(value = "根据配置id获取流水线构建列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PipelineBuildBean> getPipelineBuildBeans(Long pipelineConfId) {
        return pipelineBuildService.getPipelineBuildBean(pipelineConfId);
    }

}
