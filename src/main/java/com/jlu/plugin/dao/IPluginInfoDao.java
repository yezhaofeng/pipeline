package com.jlu.plugin.dao;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.plugin.model.PluginInfo;

/**
 * Created by langshiquan on 18/1/13.
 */
public interface IPluginInfoDao extends IBaseDao<PluginInfo> {
    PluginInfo findByJobType(String jobType);

}
