package com.jlu.branch.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jlu.branch.dao.IBranchDao;
import com.jlu.branch.model.GithubBranch;
import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;

/**
 * Created by niuwanpeng on 17/3/10.
 *
 *  模块信息dao实体类
 */
@Repository
public class BranchDaoImpl extends AbstractBaseDao<GithubBranch> implements IBranchDao {

    @Override
    public List<GithubBranch> find(String moduleName) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("module", moduleName);
        List<GithubBranch> branches = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(branches) ? new ArrayList<>(0) : branches;
    }

    @Override
    public String getModuleById(Long branchId) {
        if (branchId == null || branchId == 0L) {
            return StringUtils.EMPTY;
        }
        GithubBranch branch = findById(branchId);
        return branch == null ? StringUtils.EMPTY : branch.getModule();
    }
}
