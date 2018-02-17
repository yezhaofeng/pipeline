package com.jlu.pipeline.web;

import java.util.List;

import com.jlu.pipeline.model.PipelineConf;
import com.jlu.pipeline.service.IPipelineConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.utils.PipelineUtils;
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

    @Autowired
    private IPipelineConfService pipelineConfService;

    @RequestMapping(value = "/{owner}/{repository}/{branchType}", method = RequestMethod.GET)
    public List<PipelineBuildBean> getPipelineBuildBeans(@PathVariable String owner,
                                                         @PathVariable String repository,
                                                         @PathVariable BranchType branchType) {
        return pipelineBuildService.getPipelineBuildBean(PipelineUtils.getFullModule(owner, repository), branchType);
    }
    @RequestMapping(value = "/{owner}/{repository}/{branchType}/{branchName}", method = RequestMethod.GET)
    public List<PipelineBuildBean> getPipelineBuildBeans(@PathVariable String owner,
                                                         @PathVariable String repository,
                                                         @PathVariable BranchType branchType,
                                                         @PathVariable String branchName) {
        return pipelineBuildService.getPipelineBuildBean(PipelineUtils.getFullModule(owner, repository), branchType,branchName);
    }

    @RequestMapping(value = "/{owner}/{repository}/{branchType}", method = RequestMethod.POST)
    public ResponseBean build(@PathVariable String owner, @PathVariable String repository,
                              @PathVariable BranchType branchType, @RequestParam(required = false, defaultValue = "0") Long triggerId) {
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(PipelineUtils.getFullModule(owner,repository),branchType);
        if(pipelineConf == null){
            throw new PipelineRuntimeException("无流水线配置");
        }
        Long pipelineConfId = pipelineConf.getId();
        if (triggerId == 0L) {
            pipelineBuildService.build(pipelineConfId);
        } else {
            pipelineBuildService.build(pipelineConfId, triggerId);
        }
        return ResponseBean.TRUE;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean build(@RequestParam(required = false, defaultValue = "0") Long pipelineConfId,
                              @RequestParam(required = false, defaultValue = "0") Long triggerId) {
        if (pipelineConfId == 0L || triggerId == null) {
            throw new PipelineRuntimeException("缺少参数");
        }
        if (triggerId == 0L) {
            pipelineBuildService.build(pipelineConfId);
        } else if (pipelineConfId == 0L) {
            pipelineBuildService.rebuild(triggerId);
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
