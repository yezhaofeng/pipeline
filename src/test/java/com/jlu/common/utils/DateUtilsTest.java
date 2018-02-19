package com.jlu.common.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import junit.framework.Assert;

/**
 * Created by langshiquan on 18/1/26.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class DateUtilsTest {

    @Test
    public void testGetRealableTime() {
        Long mills = 654856780L;
        String readableTime = DateUtils.getRealableTime(mills);
        Assert.assertEquals("7d13h54m16s", readableTime);
    }

    @Test
    public void testGetChineseRealableTime() {
        Long mills = 654856780L;
        String readableTime = DateUtils.getRealableChineseTime(mills);
        Assert.assertEquals("7天13时54分16秒", readableTime);
    }

    @Test
    public void testGetChineseRealableTime0s() {
        Long mills = 0L;
        String readableTime = DateUtils.getRealableChineseTime(mills);
        Assert.assertEquals("小于1毫秒", readableTime);
    }


}