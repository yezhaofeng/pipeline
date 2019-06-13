package com.jlu.common.utils;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.jlu.SpringBaseTest;

import junit.framework.Assert;

/**
 * Created by yezhaofeng on 2019/1/30.
 */
public class PipelineConfigReaderTest extends SpringBaseTest {
    @Test
    public void testGetConfigValueByKey() throws UnsupportedEncodingException {
        System.out.println(PipelineConfigReader.getConfigValueByKey("swagger.description"));
    }

}