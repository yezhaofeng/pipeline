package com.jlu.github.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.db.bean.PageBean;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.github.dao.IModuleDao;
import com.jlu.github.model.Module;
import com.jlu.github.service.IModuleService;

/**
 * Created by yezhaofeng on 2019/3/10.
 */
@Service
public class ModuleServiceImpl implements IModuleService{

    @Autowired
    private IModuleDao moduleDao;

    /**
     *  保存模块信息
     * @param module
     */
    @Override
    public void saveModule(Module module) {
        moduleDao.save(module);
    }

    /**
     * 删除模块
     * @param module
     */
    public void delete(Module module) {
        moduleDao.delete(module);
    }

    /**
     * 批量保存模块信息
     * @param modules
     */
    @Override
    public void saveModules(List<Module> modules) {
        if (modules == null) {
            return;
        }
        for (Module module : modules) {
            Module moduleInDb = get(module.getModule());
            if (moduleInDb != null) {
                continue;
            }
            moduleDao.saveOrUpdate(module);
        }

    }

    /**
     * 搜索模块
     * @param q
     * @param username
     * @param limit
     * @return
     */
    @Override
    public List<Module> getSuggestProductModules(String q, String username, int limit) {
        PageBean page = new PageBean(limit);
        return moduleDao.getSuggestProductModules(q, username, page);
    }

    /**
     * 通过用户名获得该名下所有模块信息
     * @param username
     * @return
     */
    @Override
    public List<Module> getModulesByUsername(String username) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("owner", username);
        return moduleDao.findByProperties(conditionAndSet);
    }

    @Override
    public Module get(String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("module", module);
        List<Module> modules = moduleDao.findByProperties(conditionAndSet);
        if (modules != null && modules.size() != 0) {
            return modules.get(0);
        }
        return null;
    }
}
