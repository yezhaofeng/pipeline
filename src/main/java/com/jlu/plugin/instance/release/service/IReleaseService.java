package com.jlu.plugin.instance.release.service;

import java.util.List;

import com.jlu.plugin.instance.release.model.ReleaseBuild;

/**
 * Created by langshiquan on 18/1/22.
 */
public interface IReleaseService {
    String getMaxVersion(String module);

    void saveOrUpdate(ReleaseBuild releaseBuild);

    ReleaseBuild find(Long id);

    String increaseVersion(String version);

    Boolean compare(String version1, String version2);

    Boolean check(String version);

    List<ReleaseBuild> getReleaseInfo(String module);

}
