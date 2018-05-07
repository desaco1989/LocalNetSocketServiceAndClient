package com.desaco.localnetsocketserviceandclient.client;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desaco.localnetsocketserviceandclient.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * Created by desaco on 2018/4/28.
 */

public class ClientPageActivity extends Activity {

    private Socket s = null;
    private byte[] receiveBuffer = new byte[1024];
    private String sendBuffer = new String();
    private String ip = null;
    private int port;
    private int cmdCount = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (s != null) {
                s.close();
            }
        } catch (IOException ex) {

        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

//        new MyThread("client:hello").start();
        connectServer();
    }

    private void initView() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        //tips_tv
        TextView tipsTv = (TextView) findViewById(R.id.tips_tv);
        tipsTv.setText("客户端：");
        //machine_ip
        machineIpEt = (EditText) findViewById(R.id.machine_ip);

        //machine_port
        machinePortEt = (EditText) findViewById(R.id.machine_port);
        //connect_service
        connectServiceBt = (Button) findViewById(R.id.connect_service);
        connectServiceBt.setText("连接服务");
        //connect_service_tips TODO
        TextView connServiceTv = (TextView) findViewById(R.id.connect_service_tips);
        //msg_content
        msgContentEt = (EditText) findViewById(R.id.msg_content);
        //msg_send
        msgSendBt = (Button) findViewById(R.id.msg_send);
        //one_msg_show
        oneMsgShowTv = (TextView) findViewById(R.id.one_msg_show);
    }

    private EditText machineIpEt;
    private EditText machinePortEt;
    private Button connectServiceBt;
    private EditText msgContentEt;
    private Button msgSendBt;
    private TextView oneMsgShowTv;

    private void initListener() {

         /*开启socket通信*/
//        connectServiceBt.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (s == null) {//这里要设置验证!!!!!!!!!
//                    /*设定ip和port*/
//                    ip = machineIpEt.getText().toString();
//                    port = Integer.parseInt(machinePortEt.getText().toString());
//                    /*开启socket线程*/
//                    new Thread(new SocketClientControl(ip, port)).start();
//                }
//                Toast.makeText(ClientPageActivity.this, "服务器连接成功", Toast.LENGTH_SHORT).show();
//            }
//        });

        /*发送数据*/
        msgSendBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (s != null)
//                    sendBuffer = msgContentEt.getText().toString();
//                Toast.makeText(ClientPageActivity.this, "send -> " + sendBuffer, Toast.LENGTH_SHORT).show();

                sendMsg();
            }
        });
    }

    private Handler handler = new Handler() {//线程与UI交互更新界面
        public void handleMessage(Message msg) {
            oneMsgShowTv.setText(new String(receiveBuffer).trim());
            Arrays.fill(receiveBuffer, (byte) 0);//清空
        }
    };

    //TODO --------------------------------- http://mrhe.iteye.com/blog/1914144
    //TODO ---------------------------------
    private class SocketClientControl implements Runnable {
        private InputStream in = null;
        private OutputStream out = null;

        public SocketClientControl() {

        }

        public SocketClientControl(String ip, int port) {
            try {
//                s = new Socket(ip, port);//获得链接
                //连接服务器 并设置连接超时为5秒
                s = new Socket();
                s.connect(new InetSocketAddress(ip, port), 3000);

                in = s.getInputStream();//获得输入流
                out = s.getOutputStream();//获得输出流
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);//要是出问题，就线程退出
            }
        }

        public void run() {
            new Thread(new WriteThread()).start();//开启“写”线程
            new Thread(new ReadThread()).start();//开启“读”线程
        }

        private class ReadThread implements Runnable {
            public void run() {
                while (true) {
                    try {
                        if (in.read(receiveBuffer) > 0) {//等待命令的输入
                            cmdCount++;
                            handler.sendEmptyMessage(0);//发送信息，更新UI
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private class WriteThread implements Runnable {

            public void run() {
                while (true) {
                    if (!sendBuffer.equals("")) {
                        try {
                            out.write(sendBuffer.getBytes());//输出
                            out.flush();//输出刷新缓冲
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sendBuffer = "";
                    }
                }
            }
        }
    }

    //TODO --------------------------------- https://blog.csdn.net/u010661782/article/details/50946760
    //TODO ---------------------------------
    private OutputStream outputStream = null;
    private Socket socket = null;
//    private String ip;
    private String data;
    private boolean socketStatus = false;

    public void connectServer() {
        ip = machineIpEt.getText().toString();
        if (ip == null) {
            Toast.makeText(this, "please input Server IP", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (!socketStatus) {
                    try {
                        socket = new Socket(ip, 8080);
                        if (socket == null) {
                        } else {
                            socketStatus = true;
                        }
                        outputStream = socket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }


    public void sendMsg() {// connectServer() sendMsg()
        data = msgContentEt.getText().toString();
        if (data == null) {
            Toast.makeText(this, "please input Sending Data", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //在后面加上 '\0' ,是为了在服务端方便我们去解析；
            data = "client:"+data + '\0';
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (socketStatus) {
                    try {
                        outputStream.write(data.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }


}
