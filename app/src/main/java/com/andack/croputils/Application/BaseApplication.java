package com.andack.croputils.Application;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * 项目名称：CropUtils
 * 项目作者：anDack
 * 项目时间：2017/1/30
 * 邮箱：    1160083806@qq.com
 * 描述：    Base Application
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //对bugly进行初始化
        CrashReport.initCrashReport(getApplicationContext(),"8a75c95fa7",true);
    }
}
