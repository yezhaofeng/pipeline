package com.jlu.plugin.instance.release.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.utils.PipelineUtils;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.service.IReleaseService;

/**
 * Created by yezhaofeng on 2019/1/28.
 */
@RestController
@RequestMapping("/pipeline/release")
public class ReleaseController {

    @Autowired
    private IReleaseService releaseService;

    @RequestMapping(value = "/{owner}/{repository}", method = RequestMethod.GET)
    public List<ReleaseBuild> releaseInfo(@PathVariable String owner, @PathVariable String repository) {
        return releaseService.getReleaseBuild(PipelineUtils.getFullModule(owner, repository));
    }
}
