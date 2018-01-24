package com.jlu.plugin.runtime;

import com.jlu.common.utils.AopTargetUtils;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.instance.release.VersionValueGenerator;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-test.xml"})
public class PluginDefaultValueGeneratorTest {

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private VersionValueGenerator versionValueGenerator;

    @Test
    public void testGenerator() throws Exception {
        jobBuildService.getRuntimeRequire(15L);
    }

    @Test
    public void testComponent() throws Exception {
        JobBuild jobBuild = new JobBuild();
        versionValueGenerator.generator(jobBuild);
    }
}