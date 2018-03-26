package com.jieyue.wechat.search.bean;

import java.io.Serializable;


public class VersionBean implements Serializable {

    /**
     * appURL :
     * forceState :
     * newAppVersion :
     * versionContent :
     * versionName :
     */
    private String appURL = "";
    private String forceState = ""; // 强制升级（0-不升级； 1-非强制； 2-强制）
    private String newAppVersion = ""; // 最新版本号
    private String versionContent = ""; //版本内容
    private String versionName = ""; // 版本名称

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    public String getForceState() {
        return forceState;
    }

    public void setForceState(String forceState) {
        this.forceState = forceState;
    }

    public String getNewAppVersion() {
        return newAppVersion;
    }

    public void setNewAppVersion(String newAppVersion) {
        this.newAppVersion = newAppVersion;
    }

    public String getVersionContent() {
        return versionContent;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

}
