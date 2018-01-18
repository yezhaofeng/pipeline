package com.jlu.github.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.bean.PageBean;
import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.github.dao.IModuleDao;
import com.jlu.github.model.Module;

/**
 * Created by niuwanpeng on 17/3/10.
 *
 *  模块信息dao实体类
 */
@Repository
public class ModuleDaoImpl extends AbstractBaseDao<Module> implements IModuleDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Module> getSuggestProductModules(String query, String username, PageBean page) {
        String hql = "from CiHomeModule cm where cm.username = :username AND cm.module like :module";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("module", "%" + query + "%");
        params.put("username", username);
        return queryByHQL(hql, params, null, page);
    }
}
