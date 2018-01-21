package com.jlu.plugin.instance.release;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.pipeline.job.bean.JobParameter;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.instance.release.dao.IReleaseBuildDao;

/**
 * Created by langshiquan on 18/1/20.
 */
@Service
public class ReleaseExecutor extends AbstractExecutor {

    private final static String TEMP_PRODUCT_SOURCE_LOCATION = "TEMP_PRODUCT_SOURCE_LOCATION";
    private final static String RELEASE_PRODUCT_TARGET_LOCATION = "RELEASE_PRODUCT_TARGET_LOCATION";
    private final static String FTP_SERVER_URL = "ftp://139.199.15.115/";

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private IReleaseBuildDao releaseBuildDao;

    @Override
    protected void execute(JobBuildContext context, JobBuild jobBuild) {
        Map<String, String> inParams = jobBuild.getInParameterMap();
        String compileProductFtpPath = inParams.get(JobParameter.PIPELINE_COMPILE_PRODUCT_PATH);
        if (StringUtils.isBlank(compileProductFtpPath)) {
            jobBuild.setMessage("未发现编译产出，请在发版Job之前配置构建Job");
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuildService.notifiedJobBuildUpdated(jobBuild, new HashedMap());
            return;
        }

    }

    @Override
    public void handleCallback(JobBuild jobBuild) {
        super.handleCallback(jobBuild);
    }
}
