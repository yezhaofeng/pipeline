package com.jlu.pipeline.web;

import com.jlu.common.permission.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.common.web.AbstractController;
import com.jlu.common.web.ResponseBean;
import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.service.IPipelineConfService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

/**
 * Created by langshiquan on 18/1/14.
 */
@RestController
@RequestMapping("/pipeline/conf")
public class PipelineConfController extends AbstractController {

    @Autowired
    private IPipelineConfService pipelineConfService;

    @ApiOperation(value = "修改流水线配置")
    @RequestMapping(value = "/{owner}/{repository}/{branchType}", method = RequestMethod.PUT)
    public ResponseBean saveConf(@Valid @RequestBody PipelineConfBean pipelineConfBean, BindingResult result,
                                 @PathVariable String owner,
                                 @PathVariable String repository, @PathVariable BranchType branchType) {
        checkBindingResult(result);
        String moduleInPath = PipelineUtils.getFullModule(owner, repository);
        String moduleInBody = pipelineConfBean.getModule();
        if (!moduleInPath.equals(moduleInBody)) {
            throw new ForbiddenException("无权限");
        }
        pipelineConfService.processPipelineWithTransaction(pipelineConfBean);
        return ResponseBean.TRUE;
    }

    @ApiOperation(value = "获取流水线配置")
    @RequestMapping(value = "/{owner}/{repository}/{branchType}", method = RequestMethod.GET)
    public PipelineConfBean getConf(@PathVariable String owner,
                                    @PathVariable String repository, @PathVariable BranchType branchType) {
        return pipelineConfService.getPipelineConfBean(PipelineUtils.getFullModule(owner, repository), branchType);
    }

    @ApiOperation(value = "初始化流水线配置")
    @RequestMapping(value = "/{owner}/{repository}", method = RequestMethod.POST)
    public ResponseBean initDefault(@PathVariable String owner,
                                    @PathVariable String repository) {
        pipelineConfService.initDefaultConf(PipelineUtils.getFullModule(owner, repository));
        return ResponseBean.TRUE;
    }

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseBean saveConf(@Valid @RequestBody PipelineConfBean pipelineConfBean, BindingResult result) {
        checkBindingResult(result);
        pipelineConfService.processPipelineWithTransaction(pipelineConfBean);
        return ResponseBean.TRUE;
    }

    @Deprecated
    @RequestMapping(value = "/{pipelineConfId}", method = RequestMethod.GET)
    public PipelineConfBean getConf(@PathVariable Long pipelineConfId) {
        return pipelineConfService.getPipelineConfBean(pipelineConfId);
    }

}
