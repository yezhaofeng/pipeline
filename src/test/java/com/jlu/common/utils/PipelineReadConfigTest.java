package com.jlu.common.utils;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;

/**
 * Created by langshiquan on 18/1/30.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-test.xml"})
public class PipelineReadConfigTest {
    @Test
    public void testGetConfigValueByKey() throws UnsupportedEncodingException {
        Assert.assertEquals("api列表", new String(PipelineReadConfig.getConfigValueByKey("swagger.description")
                .getBytes("ISO-8859-1"), "UTF-8"));
    }

}