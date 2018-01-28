package com.jlu.pipeline.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.service.IPipelineConfService;

/**
 * Created by langshiquan on 18/1/14.
 */
@RestController
@RequestMapping("/pipeline/conf")
public class PipelineConfController extends AbstractController {

    @Autowired
    private IPipelineConfService pipelineConfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean saveConf(@RequestBody PipelineConfBean pipelineConfBean) {
        pipelineConfService.processPipelineWithTransaction(pipelineConfBean, getLoginUserName());
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "/{pipelineConfId}", method = RequestMethod.GET)
    public PipelineConfBean getConf(@PathVariable Long pipelineConfId) {
        return pipelineConfService.getPipelineConfBean(pipelineConfId);
    }

    @RequestMapping(value = "/initDefault", method = RequestMethod.POST)
    public ResponseBean init(String owner, String module) {
        pipelineConfService.initDefaultConf(owner, module);
        return ResponseBean.TRUE;
    }



}
