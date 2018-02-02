package com.jlu.common.permission.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.SpringBaseTest;
import com.jlu.common.permission.service.ModuleFinderService;

/**
 * Created by langshiquan on 18/2/2.
 */
public class ModuleFinderServiceImplTest extends SpringBaseTest {
    @Autowired
    private ModuleFinderService moduleFinderService;

    @Test
    public void testInitWhiteListUrl() {
        moduleFinderService.getWhiteListUrl();
    }

}