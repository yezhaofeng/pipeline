package com.jlu.common.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    @Test
    public void testThreadSafe() throws Exception {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Date date = DateUtils.addDays(new Date(), finalI);
                    String actual = DateUtils.format(date);
                    String excepted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    Assert.assertEquals(excepted, actual);
                }
            }).start();
        }
        Thread.sleep(2000);

    }

    @Test
    public void testName() throws Exception {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

    }
}