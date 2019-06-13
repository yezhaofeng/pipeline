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
 * Created by yezhaofeng on 2019/3/10.
 *
 *  模块信息dao实体类
 */
@Repository
public class ModuleDaoImpl extends AbstractBaseDao<Module> implements IModuleDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Module> getSuggestProductModules(String query, String username, PageBean page) {
        String hql = "from Module cm where cm.owner = :owner AND (cm.module like :queryParam or cm.repository like "
                + ":queryParam)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("queryParam", "%" + query + "%");
        params.put("owner", username);
        return queryByHQL(hql, params, null, page);
    }
}
