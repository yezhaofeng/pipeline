package com.jlu.github.dao;

import java.util.List;

import com.jlu.common.db.bean.PageBean;
import com.jlu.common.db.dao.IBaseDao;
import com.jlu.github.model.Module;

/**
 * Created by yezhaofeng on 2019/3/10.
 */
public interface IModuleDao extends IBaseDao<Module> {

    List<Module> getSuggestProductModules(String query, String username, PageBean page);
}
