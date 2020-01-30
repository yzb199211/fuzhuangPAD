package com.yyy.fuzhuangpad.login.model;


import com.yyy.fuzhuangpad.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AppInfoBean {

    /**
     * sAppVersion :
     * sAppApk :
     */

    private int iAppVersion;
    private String sAppApk;
    private int iTimeout;
    private String sAppImage1;
    private String sAppImage2;
    private String sAppImage3;
    private String sAppImage4;


    public int getSAppVersion() {
        return iAppVersion;
    }

    public void setSAppVersion(int sAppVersion) {
        this.iAppVersion = sAppVersion;
    }

    public String getSAppApk() {
        return sAppApk;
    }

    public void setSAppApk(String sAppApk) {
        this.sAppApk = sAppApk;
    }

    public int getiTimeout() {
        return iTimeout;
    }

    public void setiTimeout(int iTimeout) {
        this.iTimeout = iTimeout;
    }

    public List<String> getImages() {
        List<String> imgs = new ArrayList<>();
        if (StringUtil.isNotEmpty(sAppImage1))
            imgs.add(sAppImage1);
        if (StringUtil.isNotEmpty(sAppImage2))
           imgs.add(sAppImage2);
        if (StringUtil.isNotEmpty(sAppImage3))
          imgs.add(sAppImage3);
        if (StringUtil.isNotEmpty(sAppImage4))
           imgs.add(sAppImage4);
        return imgs;
    }
}
