package com.jlu.plugin.instance.compile;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.JsonUtils;
import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.instance.compile.dao.ICompileBuildDao;
import com.jlu.plugin.instance.compile.dao.ICompileConfDao;
import com.jlu.plugin.instance.compile.model.CompileBuild;
import com.jlu.plugin.instance.compile.model.CompileConf;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Service
public class CompileDataOperator extends AbstractDataOperator<CompileConf, CompileBuild> {

    @Autowired
    private ICompileBuildDao compileBuildDao;

    @Autowired
    private ICompileConfDao compileConfDao;

    @Override
    public Long saveConf(JSONObject json) {
        CompileConf compileConf = JsonUtils.getObjectByJsonObject(json, CompileConf.class);
        compileConfDao.save(compileConf);
        return compileConf.getId();
    }

    @Override
    public CompileConf getConf(Long id) {
        return compileConfDao.findById(id);
    }

    @Override
    public CompileBuild getBuild(Long id) {
        return compileBuildDao.findById(id);
    }

    @Override
    public Long initPluginBuildByPluginConf(Long pluginConfId) {
        CompileConf compileConf = compileConfDao.findById(pluginConfId);
        CompileBuild compileBuild = new CompileBuild();
        BeanUtils.copyProperties(compileConf, compileBuild);
        compileBuildDao.save(compileBuild);
        return compileBuild.getId();
    }
}
