package com.jlu.plugin.instance.release;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2019/1/23.
 */
public class ReleaseExecutorTest {
    ReleaseExecutor releaseExecutor = new ReleaseExecutor();

    @Test
    public void testGetCompileLocation() throws Exception {
        Method getCompileLocationMathod = releaseExecutor.getClass().getDeclaredMethod("getCompileLocation", String.class);
        getCompileLocationMathod.setAccessible(true);
        String compileProductPath = "ftp://139.199.15.115/z521598/devops/ba1cdf20851bb39ca434c6ff06daf89c46a77cc7/2018-01-23/10/4c5d337a-0cea-4689-ab6d-29b5f6a1b3ae";
        String result = (String) getCompileLocationMathod.invoke(releaseExecutor, compileProductPath);
        Assert.assertEquals("snapshot/z521598/devops/ba1cdf20851bb39ca434c6ff06daf89c46a77cc7/2018-01-23/10/4c5d337a-0cea-4689-ab6d-29b5f6a1b3ae", result);
    }

    @Test
    public void testName() throws Exception {
        System.out.println("ftp://139.199.15.115/".length());

    }
}