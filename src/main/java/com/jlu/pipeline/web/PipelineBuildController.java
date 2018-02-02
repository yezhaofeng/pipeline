package com.jlu.pipeline.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.utils.ModuleUtils;
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

    @Autowired
    private IPipelineBuildService pipelineBuildService;

    @RequestMapping(value = "/{owner}/{repository}/{branchType}", method = RequestMethod.GET)
    public List<PipelineBuildBean> getPipelineBuildBeans(@PathVariable String owner,
                                                         @PathVariable String repository,
                                                         @PathVariable BranchType branchType) {
        return pipelineBuildService.getPipelineBuildBean(ModuleUtils.getFullModule(owner, repository), branchType);
    }

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean build(@RequestParam Long pipelineConfId,
                              @RequestParam(required = false, defaultValue = "0") Long
                                      triggerId) {
        if (triggerId == 0L) {
            pipelineBuildService.build(pipelineConfId);
        } else {
            pipelineBuildService.build(pipelineConfId, triggerId);
        }
        return ResponseBean.TRUE;
    }

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PipelineBuildBean> getPipelineBuildBeans(Long pipelineConfId) {
        return pipelineBuildService.getPipelineBuildBean(pipelineConfId);
    }

}
