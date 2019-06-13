package com.jlu.github.service;

import java.util.List;

import com.jlu.github.model.Module;

/**
 * Created by yezhaofeng on 2019/3/10.
 */
public interface IModuleService {

    /**
     *  保存模块信息
     * @param module
     */
    void saveModule(Module module);

    /**
     * 删除模块
     * @param module
     */
    void delete(Module module);

    /**
     * 批量保存模块信息
     * @param modules
     */
    void saveModules(List<Module> modules);

    /**
     * 搜索模块
     * @param q
     * @param username
     * @param limit
     * @return
     */
    List<Module> getSuggestProductModules(String q, String username, int limit);

    /**
     * 通过用户名获得该名下所有模块信息
     * @param username
     * @return
     */
    List<Module> getModulesByUsername(String username);

    Module get(String module);
}
