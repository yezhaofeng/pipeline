package com.jlu.plugin.instance.compile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by langshiquan on 18/1/20.
 */
@Entity
public class CompileConf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isMulti = true;

    public Boolean getMulti() {
        return isMulti;
    }

    public void setMulti(Boolean multi) {
        isMulti = multi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
