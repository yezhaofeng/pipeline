package com.jlu.common.utils;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.jlu.SpringBaseTest;

import junit.framework.Assert;

/**
 * Created by langshiquan on 18/1/30.
 */
public class PipelineReadConfigTest extends SpringBaseTest {
    @Test
    public void testGetConfigValueByKey() throws UnsupportedEncodingException {
        Assert.assertEquals("api列表", new String(PipelineReadConfig.getConfigValueByKey("swagger.description")
                .getBytes("ISO-8859-1"), "UTF-8"));
    }

}