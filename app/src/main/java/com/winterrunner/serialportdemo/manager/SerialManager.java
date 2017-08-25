package com.winterrunner.serialportdemo.manager;

import com.winterrunner.serialportdemo.OnResponseListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android_serialport_api.SerialPort;

/**
 * author: L.K.X
 * created on: 2017/8/24 下午4:48
 * description:
 */

public class SerialManager {

    private static SerialManager instance = new SerialManager();
    private OnResponseListener onResponseListener;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread thread;

    private SerialManager() {}

    public void init(String path,int baudrate) {
        try {
            serialPort = new SerialPort(new File(path),baudrate, 0);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SerialManager getInstance() {
        return instance;
    }

    public void writeStr(String content) {
        if (outputStream != null) {
            try {
                outputStream.write(content.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registListener(final OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
        thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    int size;
                    try {
                        byte[] buffer = new byte[512];
                        if (inputStream == null) return;
                        size = inputStream.read(buffer);
                        if (size > 0 && onResponseListener != null) {
                            //注意这是子线程回调
                            onResponseListener.onResponse(new String(buffer, 0, size));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        };
        thread.start();
    }

    public void unRegistListener() {
        onResponseListener = null;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }
}
