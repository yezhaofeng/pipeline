package com.jlu.plugin.instance.release.service;

import com.jlu.plugin.instance.release.model.ReleaseBuild;

/**
 * Created by langshiquan on 18/1/22.
 */
public interface IReleaseService {
    String getNextVersion(String owner, String module);

    void saveOrUpdate(ReleaseBuild releaseBuild);

    ReleaseBuild find(Long id);
}
