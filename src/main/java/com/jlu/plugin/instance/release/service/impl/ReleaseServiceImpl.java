package com.jlu.plugin.instance.release.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.plugin.instance.release.dao.IReleaseBuildDao;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.service.IReleaseService;

/**
 * Created by langshiquan on 18/1/22.
 */
@Repository
public class ReleaseServiceImpl implements IReleaseService {

    public final static String FIRST_VERSION = "1.0.0";
    public final static String VERSION_REGEX = "\\d+\\.\\d+\\.\\d+";
    @Autowired
    private IReleaseBuildDao releaseBuildDao;

    @Override
    public String getMaxVersion(String owner, String module) {
        ReleaseBuild releaseBuild = releaseBuildDao.getLastest(owner, module);
        if (releaseBuild == null) {
            return FIRST_VERSION;
        }
        return releaseBuild.getVersion();
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
    public List<ReleaseBuild> getReleaseInfo(String owner, String module) {
        return releaseBuildDao.get(owner, module);
    }

    @Override
    public String increaseVersion(String version) {
        String[] everyVersion = version.split("\\.");
        Integer thirdVersion = Integer.parseInt(everyVersion[2]);
        return everyVersion[0] + "." + everyVersion[1] + "." + (thirdVersion + 1);
    }

    /**
     * 1 >= 2 true
     * 1 < 2 false
     *
     * @param version1
     * @param version2
     * @return
     */
    @Override
    public Boolean compare(String version1, String version2) {
        if (!Pattern.matches(VERSION_REGEX, version1)) {
            throw new PipelineRuntimeException(version1 + " 版本号不合法");
        }
        if (!Pattern.matches(VERSION_REGEX, version2)) {
            throw new PipelineRuntimeException(version1 + " 版本号不合法");
        }
        String[] version1s = version1.split("\\.");
        String[] version2s = version2.split("\\.");
        int i = 0;
        while (i < 3) {
            Integer v1 = Integer.parseInt(version1s[i]);
            Integer v2 = Integer.parseInt(version2s[i]);
            if (v1 > v2) {
                return true;
            } else if (v1 < v2) {
                return false;
            }
            i++;
        }
        return true;
    }

    @Override
    public Boolean check(String version) {
        return Pattern.matches(VERSION_REGEX, version);
    }

}
