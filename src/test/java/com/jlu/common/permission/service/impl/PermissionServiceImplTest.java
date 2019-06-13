package com.jlu.common.permission.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.SpringBaseTest;
import com.jlu.common.permission.service.IPermissionService;

/**
 * Created by yezhaofeng on 2019/2/2.
 */
public class PermissionServiceImplTest extends SpringBaseTest {
    @Autowired
    private IPermissionService permissionService;

    @Test
    public void testInitWhiteListUrl() {
        System.out.println(permissionService.getAdminUrlList());
        System.out.println(permissionService.getWhiteUrlList());
    }

}