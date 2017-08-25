package com.winterrunner.serialportdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.winterrunner.serialportdemo.manager.SerialManager;


/**
 * 简单说明：主要有两个类，SerialPort和SerialPortFinder
 *
 * 1.SerialPort:主要是获取 inputStream 和 outputStream 两个流，用于在串口（读取）和（写入）数据
 *
 * 2.SerialPortFinder：就是一个查找串口和串口路径的类，查找出所有的设备，至于我们要操作的串口的路径是什么，一个个的试吧。
 * 这就是这个类的作用了，找到相应的类之后，这个类就没啥用了= =
 *
 * 3.有些人会问，怎么知道我找到了对的路径呢，请看SerialPort中的61行代码，若mFd为空就是错的，反之，恭喜你找对路径了
 *
 * mFd = open(device.getAbsolutePath(), baudrate, flags);
 * if (mFd == null) {
 *      Log.e(TAG, "native open returns null");
 *      throw new IOException();
 * }
 */


public class MainActivity extends AppCompatActivity implements OnResponseListener {

    private TextView tvReceive;
    private EditText etContent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvReceive.setText((String) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        SerialManager.getInstance().registListener(this);
    }

    private void initView() {
        tvReceive = (TextView) findViewById(R.id.tv_receive);
        etContent = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void onResponse(final String result) {
        //注意这个回调在子线程

        Message obtain = Message.obtain();
        obtain.obj = result;
        handler.sendMessage(obtain);
    }

    public void send(View view) {
        SerialManager.getInstance().writeStr(etContent.getText().toString().trim());
    }


    @Override
    protected void onDestroy() {
        SerialManager.getInstance().unRegistListener();
        super.onDestroy();
    }
}
