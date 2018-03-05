package com.jlu.plugin.instance.jenkinsjob;

import com.jlu.common.exception.PipelineRuntimeException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.JsonUtils;
import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobConfDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobDataOperator extends AbstractDataOperator<JenkinsJobConf, JenkinsJobBuild> {

    @Autowired
    private IJenkinsJobConfDao jenkinsJobConfDao;

    @Autowired
    private IJenkinsJobBuildDao jenkinsJobBuildDao;

    @Override
    public Long saveConf(JSONObject json) {
        JenkinsJobConf jenkinsJobConf = JsonUtils.getObjectByJsonObject(json, JenkinsJobConf.class);
        if(!jenkinsJobConf.isValid()){
            throw new PipelineRuntimeException("Jenkins Job配置不能为空");
        }
        jenkinsJobConfDao.saveOrUpdate(jenkinsJobConf);
        return jenkinsJobConf.getId();
    }

    @Override
    public JenkinsJobConf getConf(Long id) {
        return jenkinsJobConfDao.findById(id);
    }

    @Override
    public JenkinsJobBuild getBuild(Long id) {
        return jenkinsJobBuildDao.findById(id);
    }

    @Override
    public Long initPluginBuildByPluginConf(Long pluginConfId) {
        JenkinsJobConf jenkinsJobConf = jenkinsJobConfDao.findById(pluginConfId);
        JenkinsJobBuild jenkinsJobBuild = new JenkinsJobBuild();
        BeanUtils.copyProperties(jenkinsJobConf,jenkinsJobBuild);
        jenkinsJobBuild.setId(null);
        jenkinsJobBuildDao.save(jenkinsJobBuild);
        return jenkinsJobBuild.getId();
    }
}
