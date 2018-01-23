package com.jlu.plugin.instance.release.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.plugin.instance.release.dao.IReleaseBuildDao;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.service.IReleaseService;

/**
 * Created by langshiquan on 18/1/22.
 */
@Repository
public class ReleaseServiceImpl implements IReleaseService {

    public final static String FIRST_VERSION = "1.0.0";
    @Autowired
    private IReleaseBuildDao releaseBuildDao;

    @Override
    public String getMaxVersion(String owner, String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("owner", owner);
        conditionAndSet.put("module", module);
        conditionAndSet.put("status", PipelineJobStatus.SUCCESS);
        List<ReleaseBuild> releaseBuilds = releaseBuildDao.findHeadByProperties(conditionAndSet, null, 0, 1);
        if (CollectionUtils.isEmpty(releaseBuilds)) {
            return FIRST_VERSION;
        }
        return releaseBuilds.get(0).getVersion();
    }

    @Override
    public void saveOrUpdate(ReleaseBuild releaseBuild) {
        releaseBuildDao.saveOrUpdate(releaseBuild);
    }

    @Override
    public ReleaseBuild find(Long id) {
        return releaseBuildDao.findById(id);
    }

    @Override
    public String increaseVersion(String version) {
        String[] everyVersion = version.split("\\.");
        Integer thirdVersion = Integer.parseInt(everyVersion[2]);
        return everyVersion[0] + "." + everyVersion[1] + "." + (thirdVersion + 1);
    }

    @Override
    public Boolean compare(String version1, String version2) {
        // TODO
        return null;
    }
}
