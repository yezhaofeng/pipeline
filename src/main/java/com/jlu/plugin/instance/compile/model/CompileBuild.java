package com.jlu.plugin.instance.compile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Entity
public class CompileBuild {
/*    private static final String WGET_COMMAND_PREFIX = "wget -r -nH --level=0 --cut-dirs=6 ";
    private static final String WGET_COMMAND_SUFFIX =
            "/output --user getprod --password getprod@123 --preserve-permissions";*/
    /**
     * --no-passive-ftp 不使用被动模式
     * -r, --recursive 递归下载
     * -nH, --no-host-directories 不创建含有远程主机名称的目录。
     * -l, --level=数字 最大递归深度(inf 或 0 表示无限)。
     * --cut-dirs=数目 忽略远程目录中指定数目的目录层,在此例中7层是指从snapshot到UUID，output是第八层，只下载output文件夹
     * -p,--preserve-permissions 提取有关文件权限的信息（超级用户默认选项）,即wget下来的文件的权限不变
     */
    private static final String WGET_COMMAND_PREFIX = "wget --no-passive-ftp -r -nH --level=0 --cut-dirs=7 ";
    private static final String WGET_COMMAND_SUFFIX =
            "/output --user ftptest --password ftptest --preserve-permissions";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isMulti = true;

    public Boolean getMulti() {
        if (isMulti == null) {
            return false;
        }
        return isMulti;
    }

    public void setMulti(Boolean multi) {
        if (isMulti == null) {
            isMulti = false;
        }
        isMulti = multi;
    }

    private String logUrl;

    private String buildPath;

    private String message;

    private Integer buildNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductWgetCommand() {
        return WGET_COMMAND_PREFIX + buildPath + WGET_COMMAND_SUFFIX;
    }
}
