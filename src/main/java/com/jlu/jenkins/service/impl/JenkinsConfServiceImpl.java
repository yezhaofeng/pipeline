package com.jlu.jenkins.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;

/**
 * Created by langshiquan on 18/1/10.
 */
@Service
public class JenkinsConfServiceImpl implements IJenkinsConfService {

    @Autowired
    private IJenkinsConfDao IJenkinsConfDao;

    @Override
    public void saveOrUpdate(JenkinsConf jenkinsConf) {
        IJenkinsConfDao.save(jenkinsConf);
    }

    @Override
    public JenkinsConf get(Long id) {
        return IJenkinsConfDao.findById(id);
    }
}
