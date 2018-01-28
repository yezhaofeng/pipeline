package com.jlu.branch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.branch.bean.BranchType;
import com.jlu.branch.dao.IBranchDao;
import com.jlu.branch.model.GithubBranch;
import com.jlu.branch.service.IBranchService;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.db.sqlcondition.DescOrder;
import com.jlu.common.db.sqlcondition.LessThanCondition;
import com.jlu.common.db.sqlcondition.OrderCondition;
import com.jlu.github.model.Module;
import com.jlu.github.service.IModuleService;

/**
 * Created by niuwanpeng on 17/3/10.
 */
@Service
public class BranchServiceImpl implements IBranchService {

    @Autowired
    private IBranchDao branchDao;

    @Autowired
    private IModuleService moduleService;

    /**
     *  保存模块信息
     * @param githubBranch
     */
    @Override
    public void saveBranch(GithubBranch githubBranch) {
        branchDao.save(githubBranch);
    }

    /**
     * 批量保存模块信息
     * @param githubBranches
     */
    @Override
    public void saveBranches(List<GithubBranch> githubBranches) {
        branchDao.saveOrUpdateAll(githubBranches);
    }


    /**
     * 获得分支信息
     * @param moduleId
     * @param branchName
     * @return
     */
    @Override
    public GithubBranch getBranchByModule(int moduleId, String branchName) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("moduleId", moduleId);
        conditionAndSet.put("branchName", branchName);
        List<GithubBranch> githubBranches = branchDao.findByProperties(conditionAndSet);
        return githubBranches != null && githubBranches.size() != 0 ? githubBranches.get(0) : null;
    }

    /**
     * 获得branchId之前的10条记录
     * @param module
     * @return
     */
    @Override
    public List<GithubBranch> getBranches(Module module, int branchId, int limit) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("moduleId", module.getId());
        conditionAndSet.put("branchType", BranchType.BRANCH);
        if (branchId != 0) {
            conditionAndSet.addCompareCondition(new LessThanCondition("id", branchId));
        }
        List<OrderCondition> orderConditions = new ArrayList<>();
        orderConditions.add(new DescOrder("id"));
        List<GithubBranch>
                githubBranches = branchDao.findHeadByProperties(conditionAndSet, orderConditions, branchId, limit);
        return githubBranches != null && githubBranches.size() != 0 ? githubBranches : new ArrayList<GithubBranch>();
    }

    /**
     * 获得分支名集合
     * @param username
     * @param module
     * @return
     */
    @Override
    public List<GithubBranch> getBranchesByModule(String username, String module) {
        Module ciHomeModule = moduleService.getModuleByUserAndModule(username, module);
        List<GithubBranch> githubBranches = new ArrayList<>();
        if (ciHomeModule != null) {
            githubBranches = getBranches(ciHomeModule, 0, 50);
        }
        return githubBranches;
    }

    @Override
    public void updateRemark(Long branchId, String remark) {
        GithubBranch githubBranch = branchDao.findById(branchId);
        githubBranch.setRemarks(remark);
        branchDao.saveOrUpdate(githubBranch);
    }

}
