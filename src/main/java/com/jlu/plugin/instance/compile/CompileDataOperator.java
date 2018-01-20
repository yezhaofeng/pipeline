package com.jlu.plugin.instance.compile;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.instance.compile.dao.ICompileBuildDao;
import com.jlu.plugin.instance.compile.dao.ICompileConfDao;
import com.jlu.plugin.instance.compile.model.CompileBuild;
import com.jlu.plugin.instance.compile.model.CompileConf;

/**
 * Created by langshiquan on 18/1/20.
 */
@Service
public class CompileDataOperator extends AbstractDataOperator<CompileConf, CompileBuild> {

    @Autowired
    private ICompileBuildDao compileBuildDao;

    @Autowired
    private ICompileConfDao compileConfDao;

    @Override
    public Long saveConf(JSONObject json) {
        return null;
    }

    @Override
    public CompileConf getConf(Long id) {
        return compileConfDao.findById(id);
    }

    @Override
    public CompileBuild getBuild(Long id) {
        return compileBuildDao.findById(id);
    }
}
