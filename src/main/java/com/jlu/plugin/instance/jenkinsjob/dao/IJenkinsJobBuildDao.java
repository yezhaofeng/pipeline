package com.jlu.plugin.instance.jenkinsjob.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;

import java.util.List;

/**
 * Created by yezhaofeng on 2019/1/20.
 */

public interface IJenkinsJobBuildDao extends IBaseDao<JenkinsJobBuild> {
    List<JenkinsJobBuild> get(Long jenkinsServerId, String jobName, Integer buildNumber);
}
