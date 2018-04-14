package com.jlu.jenkins.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.jenkins.model.JenkinsBuild;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */
public interface IJenkinsBuildDao extends IBaseDao<JenkinsBuild>{

    List<JenkinsBuild> findWithoutBuildResult();

}
