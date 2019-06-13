package com.jlu.branch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.branch.model.GithubBranch;
import com.jlu.branch.service.IBranchService;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.common.web.ResponseBean;

/**
 * Created by yezhaofeng on 2019/5/4.
 */
@RestController
@RequestMapping("/pipeline")
public class BranchController {

    @Autowired
    private IBranchService branchService;

    @RequestMapping(value = "/{owner}/{repository}/branches", method = RequestMethod.GET)
    @ResponseBody
    public List<GithubBranch> getBranchesByModule(@PathVariable String owner, @PathVariable String repository) {
        return branchService.getBranchesByModule(PipelineUtils.getFullModule(owner, repository));
    }

    @RequestMapping(value = "/branch/{branchId}/remark", method = RequestMethod.POST)
    public ResponseBean updateRemark(@PathVariable Long branchId, @RequestBody String remark) {
        branchService.updateRemark(branchId, remark);
        return ResponseBean.TRUE;
    }

}
