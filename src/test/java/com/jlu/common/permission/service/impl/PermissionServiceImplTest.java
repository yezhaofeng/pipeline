package com.jlu.common.permission.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.SpringBaseTest;
import com.jlu.common.permission.service.PermissionService;

/**
 * Created by langshiquan on 18/2/2.
 */
public class PermissionServiceImplTest extends SpringBaseTest {
    @Autowired
    private PermissionService permissionService;

    @Test
    public void testInitWhiteListUrl() {
        permissionService.getWhiteUrlList();
    }

}