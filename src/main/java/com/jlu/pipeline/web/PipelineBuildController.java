package com.jlu.pipeline.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.pipeline.bean.PipelineBuildBean;
import com.jlu.pipeline.service.IPipelineBuildService;

/**
 * Created by langshiquan on 18/1/14.
 */
@RestController
@RequestMapping("/pipeline/build")
public class PipelineBuildController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(PipelineBuildController.class);

    @Autowired
    private IPipelineBuildService pipelineBuildService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean build(Long pipelineConfId, @RequestParam(required = false, defaultValue = "0") Long triggerId) {
        try {
            if (triggerId == 0L) {
                pipelineBuildService.build(pipelineConfId);
            } else {
                pipelineBuildService.build(pipelineConfId, triggerId);
            }
        } catch (Exception e) {
            logger.error("html.error:{}", e);
            return ResponseBean.fail(e.getMessage());
        }
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PipelineBuildBean> getPipelineBuildBeans(Long pipelineConfId) {
        return pipelineBuildService.getPipelineBuildBean(pipelineConfId);
    }

}
