package com.jlu.plugin.instance.release;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.JsonUtils;
import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.instance.release.dao.IReleaseBuildDao;
import com.jlu.plugin.instance.release.dao.IReleaseConfDao;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.model.ReleaseConf;

/**
 * Created by langshiquan on 18/1/20.
 */
@Service
public class ReleaseDataOperator extends AbstractDataOperator<ReleaseConf, ReleaseBuild> {

    @Autowired
    private IReleaseBuildDao releaseBuildDao;

    @Autowired
    private IReleaseConfDao releaseConfDao;

    @Override
    public Long saveConf(JSONObject json) {
        ReleaseConf releaseConf = JsonUtils.getObjectByJsonObject(json, ReleaseConf.class);
        releaseConfDao.save(releaseConf);
        return releaseConf.getId();
    }

    @Override
    public ReleaseConf getConf(Long id) {
        return releaseConfDao.findById(id);
    }

    @Override
    public ReleaseBuild getBuild(Long id) {
        ReleaseBuild releaseBuild = releaseBuildDao.findById(id);
        return releaseBuild;
    }

    @Override
    public Long initPluginBuildByPluginConf(Long pluginConfId) {
        ReleaseBuild releaseBuild = new ReleaseBuild();
        releaseBuildDao.saveOrUpdate(releaseBuild);
        return releaseBuild.getId();
    }
}
