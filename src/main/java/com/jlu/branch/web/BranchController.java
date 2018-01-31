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
import com.jlu.common.utils.ModuleUtils;
import com.jlu.common.web.ResponseBean;

/**
 * Created by niuwanpeng on 17/5/4.
 */
@RestController
@RequestMapping("/pipeline")
public class BranchController {

    @Autowired
    private IBranchService branchService;

    @RequestMapping(value = "/{owner}/{repository}/branches", method = RequestMethod.GET)
    @ResponseBody
    public List<GithubBranch> getBranchesByModule(@PathVariable String owner, @PathVariable String repository) {
        String module = ModuleUtils.getFullModule(owner, repository);
        return branchService.getBranchesByModule(module);
    }

    @RequestMapping(value = "/{branchId}/remark", method = RequestMethod.POST)
    public ResponseBean updateRemark(@PathVariable Long branchId, @RequestBody String remark) {
        branchService.updateRemark(branchId, remark);
        return ResponseBean.TRUE;
    }

}
