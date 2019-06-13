package com.jlu.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Created by yezhaofeng on 2019/2/3.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class PipelineUtilsTest {
    @Test
    public void testGetRepoNameByModule() {
        Assert.assertEquals("devops", PipelineUtils.getRepoNameByModule("z521598/devops"));
    }

    @Test
    public void testGetFullModule() {
        Assert.assertEquals("z521598/devops", PipelineUtils.getFullModule("z521598", "devops"));
    }

    @Test
    public void testParseQueryString() {
        String queryString = "pipelineConfId=123&triggerId=456";
        Map<String, String> param = new LinkedHashMap<>();
        param.put("pipelineConfId", "123");
        param.put("triggerId", "456");
        Assert.assertEquals(param.toString(), PipelineUtils.parseQueryString(queryString).toString());

    }

}