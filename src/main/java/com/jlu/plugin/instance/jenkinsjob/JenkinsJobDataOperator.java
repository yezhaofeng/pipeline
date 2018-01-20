package com.jlu.plugin.instance.jenkinsjob;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jlu.plugin.IDataOperator;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobConfDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobDataOperator extends IDataOperator<JenkinsJobConf, JenkinsJobBuild> {

    @Autowired
    private IJenkinsJobConfDao jenkinsJobConfDao;

    @Autowired
    private IJenkinsJobBuildDao jenkinsJobBuildDao;

    @Override
    public Long saveJob(JSONObject json) {
        JenkinsJobConf jenkinsJobConf = (JenkinsJobConf) JSON.parse(json.toString());
        jenkinsJobConfDao.save(jenkinsJobConf);
        return jenkinsJobConf.getId();
    }

    @Override
    public JenkinsJobConf getJob(Long id) {
        return jenkinsJobConfDao.findById(id);
    }

    @Override
    public JenkinsJobBuild getBuild(Long id) {
        return jenkinsJobBuildDao.findById(id);
    }

    @Override
    public Long initRealJobBuildByRealJobConf(Long pluginConfId) {
        JenkinsJobConf jenkinsJobConf = jenkinsJobConfDao.findById(pluginConfId);
        JenkinsJobBuild jenkinsJobBuild = new JenkinsJobBuild();
        BeanUtils.copyProperties(jenkinsJobConf,jenkinsJobBuild);
        jenkinsJobBuild.setId(null);
        jenkinsJobBuildDao.save(jenkinsJobBuild);
        return jenkinsJobBuild.getId();
    }
}
