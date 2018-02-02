package com.jlu.common.permission.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.branch.dao.IBranchDao;
import com.jlu.common.permission.annotations.PermissionAdmin;
import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.common.permission.bean.EnvType;
import com.jlu.common.permission.service.IPermissionService;
import com.jlu.common.swagger2.Swagger2Config;
import com.jlu.common.utils.CollUtils;
import com.jlu.common.utils.PackageScanUtils;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.github.dao.IGitHubCommitDao;
import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.pipeline.dao.IPipelineConfDao;
import com.jlu.pipeline.job.dao.IJobBuildDao;

/**
 * Created by langshiquan on 18/2/2.
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    private final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private static final String PERMISSION_PASS_CLASS_PATTERN = "com.jlu";

    private Set<String> whiteUrlList = new HashSet<>();
    private Set<String> adminUrlList = new HashSet<>();

    @Autowired
    private IBranchDao branchDao;

    @Autowired
    private IJobBuildDao jobBuildDao;

    @Autowired
    private IPipelineConfDao pipelineConfDao;

    @Autowired
    private IGitHubCommitDao gitHubCommitDao;

    @Autowired
    private IJenkinsConfDao jenkinsConfDao;
    /**
     * @param paramType
     * @param paramValue
     *
     * @return 模块名字
     */
    @Override
    public String getModuleByParamType(String paramType, String paramValue) {
        switch (paramType) {
            case "branchId":
                if (!NumberUtils.isNumber(paramValue)) {
                    return StringUtils.EMPTY;
                }
                return branchDao.getModuleById(Long.parseLong(paramValue));
            case "jobBuildId":
                if (!NumberUtils.isNumber(paramValue)) {
                    return StringUtils.EMPTY;
                }
                return jobBuildDao.getModuleById(Long.parseLong(paramValue));
            case "pipelineConfId":
                if (!NumberUtils.isNumber(paramValue)) {
                    return StringUtils.EMPTY;
                }
                return pipelineConfDao.getModuleById(Long.parseLong(paramValue));
            case "triggerId":
                if (!NumberUtils.isNumber(paramValue)) {
                    return StringUtils.EMPTY;
                }
                return gitHubCommitDao.getModuleById(Long.parseLong(paramValue));
            case "jenkinsServerId":
                if (!NumberUtils.isNumber(paramValue)) {
                    return StringUtils.EMPTY;
                }
                // jenkinsServer属于跨模块的资源，此处做一下适配，方便鉴权
                return jenkinsConfDao.findCreateUserById(Long.parseLong(paramValue)) + "/all-module";
            default:
                break;
        }
        return StringUtils.EMPTY;
    }

    @Override
    public Boolean checkPermission(String module, String username) {
        if (StringUtils.isBlank(module)) {
            return false;
        }
        String[] element = module.split("/");
        if (element.length != 2) {
            return false;
        }
        if (username.equals(element[0])) {
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getWhiteUrlList() {
        return whiteUrlList;
    }

    @Override
    public Set<String> getAdminUrlList() {
        return adminUrlList;
    }

    @PostConstruct
    public void initWhiteListUrl() {
        initSetBySpringMvcAnnotation(PERMISSION_PASS_CLASS_PATTERN, PermissionPass.class, whiteUrlList);
        // 线下环境
        if (EnvType.LOCAL.toString().equals(PipelineConfigReader.getConfigValueByKey("env.type"))) {
            whiteUrlList.add(Swagger2Config.JSON_URL);
            whiteUrlList.add(Swagger2Config.UI_URL);
            whiteUrlList.add("/mock/userLogin");
        }
        logger.info("admin ulr list:{}", whiteUrlList);

    }

    @PostConstruct
    public void initAdminListUrl() {
        initSetBySpringMvcAnnotation(PERMISSION_PASS_CLASS_PATTERN, PermissionAdmin.class, adminUrlList);
        // 线上环境
        if (EnvType.ONLINE.toString().equals(PipelineConfigReader.getConfigValueByKey("env.type"))) {
            adminUrlList.add(Swagger2Config.JSON_URL);
        }

        logger.info("white ulr list:{}", adminUrlList);

    }

    private void initSetBySpringMvcAnnotation(String packageScan, Class<? extends Annotation> annotationClass, Set
            toSet) {
        Set<String> controllerClassSet = PackageScanUtils.findPackageAnnotationClass(packageScan,
                Controller.class, RestController.class);
        if (CollUtils.isEmpty(controllerClassSet)) {
            return;
        }
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
                // 如果类上面加了PermissionAdmin注解，则类下面所有url全部都不需要鉴权
                Boolean isClassPermissionPass = controllerClass.isAnnotationPresent(annotationClass);
                if (isClassPermissionPass) {
                    Method[] methods = controllerClass.getDeclaredMethods();
                    for (Method method : methods) {
                        RequestMapping requestMappingClass = method.getDeclaredAnnotation(RequestMapping.class);
                        if (requestMappingClass == null) {
                            continue;
                        }
                        String[] urls = requestMappingClass.value();
                        CollUtils.addAll(toSet, CollUtils.combination(parentUrls, urls).iterator());
                    }
                } else { // 类没有加PermissionAdmin注解，扫描方法上的注解
                    Method[] methods = controllerClass.getDeclaredMethods();
                    for (Method method : methods) {
                        Boolean isMethodPermissionPass = method.isAnnotationPresent(annotationClass);
                        if (isMethodPermissionPass) {
                            RequestMapping requestMappingClass = method.getDeclaredAnnotation(RequestMapping.class);
                            if (requestMappingClass == null) {
                                continue;
                            }
                            String[] urls = requestMappingClass.value();
                            CollUtils.addAll(toSet, CollUtils.combination(parentUrls, urls).iterator());
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("Scan {} Class error:", classPath, e);
            }
        }
    }

}
