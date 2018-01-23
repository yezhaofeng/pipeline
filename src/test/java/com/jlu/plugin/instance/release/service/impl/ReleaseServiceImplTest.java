package com.jlu.plugin.instance.release.service.impl;

import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.plugin.instance.release.service.IReleaseService;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/1/23.
 */
public class ReleaseServiceImplTest {

    private IReleaseService releaseService = new ReleaseServiceImpl();

    @Test
    public void testCompareGreater() throws Exception {
        Assert.assertEquals(true, releaseService.compare("1.0.10", "1.0.1"));
    }

    @Test
    public void testCompareLess() throws Exception {
        Assert.assertEquals(false, releaseService.compare("1.4.0", "2.0.1"));
    }

    @Test
    public void testCompareEquals() throws Exception {
        Assert.assertEquals(true, releaseService.compare("10.4.1", "10.4.1"));
    }

    @Test(expected = PipelineRuntimeException.class)
    public void testCompareException() throws Exception {
        Assert.assertEquals(false, releaseService.compare("10.4.1", "10.4.1.2"));
    }


}