package com.jlu.plugin.instance.compile.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Created by langshiquan on 18/1/25.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class CompileBuildTest {
    @Test
    public void testGetProductWgetCommand() {
        CompileBuild compileBuild = new CompileBuild();
        compileBuild.setBuildPath(
                "ftp://139.199.15.115/z521598/devops/ba1cdf20851bb39ca434c6ff06daf89c46a77cc7/2018-01-23/10/4c5d337a"
                        + "-0cea-4689-ab6d-29b5f6a1b3ae/");
        Assert.assertEquals("wget -r -nH --level=0 --cut-dirs=6 "
                + "ftp://139.199.15.115/z521598/devops/ba1cdf20851bb39ca434c6ff06daf89c46a77cc7/2018-01-23/10"
                + "/4c5d337a-0cea-4689-ab6d-29b5f6a1b3ae//output --user getprod --password getprod@123 "
                + "--preserve-permissions", compileBuild.getProductWgetCommand());
    }

}