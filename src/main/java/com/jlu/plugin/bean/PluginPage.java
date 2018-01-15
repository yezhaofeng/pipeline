package com.jlu.plugin.bean;

/**
 * Created by baidu on 15/10/28.
 */
public class PluginPage {

    private String html;

    private String js;

    public PluginPage(String html) {
        this.html = html;
    }

    public PluginPage(String html, String js) {
        this.html = html;
        this.js = js;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }
}
