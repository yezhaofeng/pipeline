package com.jlu.common.permission.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

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
    public Boolean checkPermissionByParamType(String paramType, String paramValue, String owner) {
        // 暂时还没有不是资源的id走到这里
        if (!NumberUtils.isNumber(paramValue)) {
            return false;
        }
        Long id = Long.parseLong(paramValue);
        if (id == 0L) {
            return true;
        }
        switch (paramType) {
            case "branchId":
                return checkUsernameByModule(branchDao.getModuleById(id), owner);
            case "jobBuildId":
                return checkUsernameByModule(jobBuildDao.getModuleById(id), owner);
            case "pipelineConfId":
                return checkUsernameByModule(pipelineConfDao.getModuleById(id), owner);
            case "triggerId":
                return checkUsernameByModule(gitHubCommitDao.getModuleById(id), owner);
            case "jenkinsServerId":
                // jenkinsServer属于跨模块的资源
                String createUser = jenkinsConfDao.findCreateUserById(id);
                if (owner.equals(createUser)) {
                    return true;
                }
                return false;
            default:
                break;
        }
        return true;
    }

    public Boolean checkUsernameByModule(String module, String username) {
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

    @Override
    public Boolean isStaticResource(HttpServletRequest request) {

        return request.getRequestURL().indexOf("resources/") > 0
                || request.getRequestURL().indexOf("static/") > 0
                || request.getRequestURL().indexOf("css/") > 0
                || request.getRequestURL().indexOf("js/") > 0
                || request.getRequestURL().indexOf("images/") > 0
                || request.getRequestURL().indexOf("html/") > 0
                || request.getRequestURL().indexOf("error/") > 0
                || request.getRequestURL().indexOf("resource/") > 0
                || request.getRequestURL().indexOf("favicon.ico") > 0
                || request.getRequestURL().indexOf("webjars") > 0;

    }

    @Override
    public Boolean checkSourcePermission(Map<String, String> resourceParam, String username) {
        String owner = resourceParam.get("owner");
        if (owner != null) {
            if (owner.equals(username)) {
                return true;
            } else {
                return false;
            }
        } else {
            Set<String> keySet = resourceParam.keySet();
            Boolean result = true;
            for (String key : keySet) {
                Boolean sourcePermission = checkPermissionByParamType(key, resourceParam.get(key), username);
                result = result && sourcePermission;
            }
            return result;
        }

    }

    @PostConstruct
    public void initWhiteListUrl() {
        initSetBySpringMvcAnnotation(PERMISSION_PASS_CLASS_PATTERN, PermissionPass.class, whiteUrlList);
        // 线下环境
        if (EnvType.LOCAL.toString().equals(PipelineConfigReader.getConfigValueByKey("env.type"))) {
            whiteUrlList.add(Swagger2Config.JSON_URL);
            whiteUrlList.add(Swagger2Config.UI_URL);
            whiteUrlList.add("/mock/userLogin");
            whiteUrlList.add("/swagger-ui.html");
        }
        logger.info("admin ulr list:{}", whiteUrlList);

    }

    @PostConstruct
    public void initAdminListUrl() {
        initSetBySpringMvcAnnotation(PERMISSION_PASS_CLASS_PATTERN, PermissionAdmin.class, adminUrlList);
        // 线上环境
        if (EnvType.ONLINE.toString().equals(PipelineConfigReader.getConfigValueByKey("env.type"))) {
            adminUrlList.add(Swagger2Config.JSON_URL);
            adminUrlList.add("/swagger-ui.html");
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
