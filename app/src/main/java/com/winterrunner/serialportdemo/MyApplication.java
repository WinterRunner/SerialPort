package com.winterrunner.serialportdemo;

import android.app.Application;

import com.winterrunner.serialportdemo.manager.SerialConstant;
import com.winterrunner.serialportdemo.manager.SerialManager;

/**
 * author: L.K.X
 * created on: 2017/8/24 下午4:47
 * description:
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SerialManager.getInstance().init(SerialConstant.path,SerialConstant.baudrate);
    }
}
