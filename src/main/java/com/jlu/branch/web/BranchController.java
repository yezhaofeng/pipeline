package com.jlu.branch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.branch.service.IBranchService;
import com.jlu.common.web.ResponseBean;

/**
 * Created by niuwanpeng on 17/5/4.
 */
@RestController
@RequestMapping("/branch")
public class BranchController {

    @Autowired
    private IBranchService branchService;

    /**
     * 获得分支名
     * @param username
     * @param module
     * @return
     */
    @RequestMapping(value = "/getBranches", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getBranchesByModule(@RequestParam("username") String username,
                                                  @RequestParam("module") String module) {
        return branchService.getBranchesByModule(username, module);
    }

    @RequestMapping(value = "/{branchId}/remark", method = RequestMethod.POST)
    public ResponseBean updateRemark(@PathVariable Long branchId, @RequestBody String remark) {
        branchService.updateRemark(branchId, remark);
        return ResponseBean.TRUE;
    }

}
