package com.jlu.plugin.runtime;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.SpringBaseTest;
import com.jlu.pipeline.job.service.IJobBuildService;

/**
 * Created by Administrator on 2019/1/24.
 */
public class PluginValueGeneratorTest extends SpringBaseTest {

    @Autowired
    private IJobBuildService jobBuildService;


    @Test
    public void testGenerator() throws Exception {
        jobBuildService.getRuntimeRequire(15L);
    }

}