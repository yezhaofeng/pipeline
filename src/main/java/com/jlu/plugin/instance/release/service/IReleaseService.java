package com.jlu.plugin.instance.release.service;

import java.util.List;

import com.jlu.plugin.instance.release.model.ReleaseBuild;

/**
 * Created by yezhaofeng on 2019/1/22.
 */
public interface IReleaseService {
    void saveOrUpdate(ReleaseBuild releaseBuild);

    ReleaseBuild get(Long id);

    List<ReleaseBuild> getReleaseBuild(String module);

    String getMaxVersion(String module);

    String increaseVersion(String version);

    Boolean compareVersion(String version1, String version2);

    Boolean checkVersion(String version);


}
