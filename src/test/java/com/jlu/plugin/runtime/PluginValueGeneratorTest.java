package com.jlu.plugin.runtime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.instance.release.VersionValueGenerator;

/**
 * Created by Administrator on 2018/1/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-test.xml"})
public class PluginValueGeneratorTest {

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private VersionValueGenerator versionValueGenerator;

    @Test
    public void testGenerator() throws Exception {
        jobBuildService.getRuntimeRequire(15L);
    }

}