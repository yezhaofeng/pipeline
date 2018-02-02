package com.jlu.common.permission.service.impl;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.common.permission.service.ModuleFinderService;
import com.jlu.common.utils.CollUtils;
import com.jlu.common.utils.PackageScanUtils;

/**
 * Created by langshiquan on 18/2/2.
 */
@Service
public class ModuleFinderServiceImpl implements ModuleFinderService {

    private final Logger logger = LoggerFactory.getLogger(ModuleFinderServiceImpl.class);
    private static final String PERMISSION_PASS_CLASS_PATTERN = "com.jlu";

    private Set<String> whiteListUrl = new HashSet<>();

    @Override
    public String getModuleByParamType(String paramType, Object paramValue) {
        return null;
    }

    @Override
    public Set<String> getWhiteListUrl() {
        return whiteListUrl;
    }

    @PostConstruct
    public void initWhiteListUrl() {
        Set<String> controllerClassSet = PackageScanUtils.findPackageAnnotationClass(PERMISSION_PASS_CLASS_PATTERN,
                Controller.class, RestController.class);
        if (CollUtils.isEmpty(controllerClassSet)) {
            return;
        }
        Set<String> whiteListUrls = new HashSet<>();
        for (String classPath : controllerClassSet) {
            try {
                Class controllerClass = Class.forName(classPath);
                // 父url
                String[] parentUrls;
                RequestMapping requestMappingAnno =
                        (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                if (requestMappingAnno == null) {
                    parentUrls = null;
                } else {
                    parentUrls = requestMappingAnno.value();
                }
                // 如果类上面加了PermissionPass注解，则类下面所有url全部都不需要鉴权。
                Boolean isClassPermissionPass = controllerClass.isAnnotationPresent(PermissionPass.class);
                if (isClassPermissionPass) {
                    Method[] methods = controllerClass.getDeclaredMethods();
                    for (Method method : methods) {
                        RequestMapping requestMappingClass = method.getDeclaredAnnotation(RequestMapping.class);
                        if (requestMappingClass == null) {
                            continue;
                        }
                        String[] urls = requestMappingClass.value();
                        CollUtils.addAll(whiteListUrls, CollUtils.combination(parentUrls, urls).iterator());
                    }
                } else { // 类没有加PermissionPass注解，扫描方法上的注解
                    Method[] methods = controllerClass.getDeclaredMethods();
                    for (Method method : methods) {
                        Boolean isMethodPermissionPass = method.isAnnotationPresent(PermissionPass.class);
                        if (isMethodPermissionPass) {
                            RequestMapping requestMappingClass = method.getDeclaredAnnotation(RequestMapping.class);
                            if (requestMappingClass == null) {
                                continue;
                            }
                            String[] urls = requestMappingClass.value();
                            CollUtils.addAll(whiteListUrls, CollUtils.combination(parentUrls, urls).iterator());
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("Scan {} Class error:", classPath, e);
            }
        }
    }

}
