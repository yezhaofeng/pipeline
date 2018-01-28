package com.jlu.branch.bean;

/**
 * Created by niuwanpeng on 17/3/10.
 */
public enum BranchType {
    TRUNK, BRANCH;

    public static final String MASTER = "master";

    public static BranchType parseType(String branchName) {
        if (MASTER.equals(branchName)) {
            return TRUNK;
        } else {
            return BRANCH;
        }
    }
}
