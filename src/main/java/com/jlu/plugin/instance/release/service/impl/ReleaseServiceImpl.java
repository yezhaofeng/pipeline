package com.jlu.plugin.instance.release.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.ListUtils;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.plugin.instance.release.dao.IReleaseBuildDao;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.service.IReleaseService;
import com.jlu.plugin.instance.release.service.VersionService;

/**
 * Created by langshiquan on 18/1/22.
 */
@Repository
public class ReleaseServiceImpl implements IReleaseService {

    @Autowired
    private IReleaseBuildDao releaseBuildDao;

    @Autowired
    private VersionService versionService;

    @Override
    public String getNextVersion(String owner, String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("owner", owner);
        conditionAndSet.put("module", module);
        conditionAndSet.put("status", PipelineJobStatus.SUCCESS);
        List<ReleaseBuild> releaseBuilds = releaseBuildDao.findByProperties(conditionAndSet);
        if (CollectionUtils.isEmpty(releaseBuilds)) {
            return VersionService.FIRST_VERSION;
        }
        List<String> versions = ListUtils.toList(releaseBuilds, ReleaseBuild.VERSION_GETTER);
        String maxVersion = versionService.getMaxReleaseVersion(versions);
        return versionService.increaseVersion(maxVersion);

    }

    @Override
    public void saveOrUpdate(ReleaseBuild releaseBuild) {
        releaseBuildDao.saveOrUpdate(releaseBuild);
    }

    @Override
    public ReleaseBuild find(Long id) {
        return releaseBuildDao.findById(id);
    }
}
