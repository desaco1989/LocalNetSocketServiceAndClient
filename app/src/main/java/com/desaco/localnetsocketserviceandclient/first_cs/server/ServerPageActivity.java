package com.desaco.localnetsocketserviceandclient.first_cs.server;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desaco.localnetsocketserviceandclient.R;

/**
 * Created by desaco on 2018/4/28.
 * http://mrhe.iteye.com/blog/1914144
 */

public class ServerPageActivity extends Activity {

//    private ServerThread serverThread = null;
    private String sendBuffer = null;
    private String receiveBuffer = null;
//    private TcpSocketServerUtils tss = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server_client);

        initView();
        initListener();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (tss != null) {
//            tss.closeStream();
//        }
//
//    }

    private EditText machinePortEt;
    private Button connectServiceBt;
    private EditText msgContentEt;
    private Button msgSendBt;
    private TextView oneMsgShowTv;

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
        tipsTv.setText("服务器端：请先开启服务器服务");
        //machine_ip
        EditText machineIpTv = (EditText) findViewById(R.id.machine_ip);
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        machineIpTv.setText(ip + "-ip:" + ipAddress);
        //machine_port
        machinePortEt = (EditText) findViewById(R.id.machine_port);
        //connect_service
        connectServiceBt = (Button) findViewById(R.id.connect_service);
        connectServiceBt.setText("开启服务");
        //connect_service_tips TODO
        TextView connServiceTv = (TextView) findViewById(R.id.connect_service_tips);
        //msg_content
        msgContentEt = (EditText) findViewById(R.id.msg_content);
        //msg_send
        msgSendBt = (Button) findViewById(R.id.msg_send);
        //one_msg_show
        oneMsgShowTv = (TextView) findViewById(R.id.one_msg_show);
    }

    private SocketServer server;

    private void initListener() {
        /**socket收到消息线程*/
        SocketServer.ServerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                oneMsgShowTv.setText(msg.obj.toString());
            }
        };

        //监听服务器开启
        connectServiceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                if (serverThread == null) {
//                    String port = machinePortEt.getText().toString().trim();
//                    serverThread = new ServerThread(port);
//                    Log.e("desaco", "port=" + port);
//                    serverThread.start();
//                }
                /**socket服务端开始监听*/
                String port = machinePortEt.getText().toString().trim();
                int por = Integer.parseInt(port);
                server = new SocketServer(por);
                server.startListen();
                Toast.makeText(ServerPageActivity.this, "开启的端口号:" + port, Toast.LENGTH_SHORT).show();
                connectServiceBt.setText("服务已开启！");
            }
        });

        //监听发送信息
        msgSendBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendBuffer = msgContentEt.getText().toString().trim();
//                if (tss != null && !"".equals(sendBuffer)) {//为了避免线程把它弄为buffer = null;
//                    try {
//                        tss.sendMessage("server:" + sendBuffer);
//                        Toast.makeText(ServerPageActivity.this, "服务器发送的消息为：" + sendBuffer, Toast.LENGTH_SHORT).show();
//                        Log.e("desaco", "服务器发送的消息为：" + sendBuffer);
//                    } catch (Exception ex) {
//
//                    }
//                }
                if(server != null){
                    server.sendMsg("server:" + sendBuffer);
                        Toast.makeText(ServerPageActivity.this, "服务器发送的消息为：" + sendBuffer, Toast.LENGTH_SHORT).show();
                        Log.e("desaco", "服务器发送的消息为：" + sendBuffer);
                }

            }
        });
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

//    private Handler handler = new Handler() {//线程与UI交互更新界面
//        public void handleMessage(Message msg) {
//            oneMsgShowTv.setText(receiveBuffer);
//            Toast.makeText(ServerPageActivity.this, "收到来自客户端的消息： " + receiveBuffer, Toast.LENGTH_SHORT).show();
//        }
//    };
//

//
//    class ServerThread extends Thread {
//        private int port;
//
//        public ServerThread(String port) {
//            this.port = Integer.parseInt(port);
//        }
//
//        public void run() {
//            //建立服务端
//            if (tss == null)
//                tss = new TcpSocketServerUtils(this.port);
//            new Thread(new WriteThread()).start();//开启“写”线程
//            new Thread(new ReadThread()).start();//开启“读”线程
//        }
//
//        private class ReadThread implements Runnable {
//            public void run() {
//                while (true) {
//                    if ((receiveBuffer = tss.getMessage()) != null) {//收到不为null的信息就发送出去
//                        handler.sendEmptyMessage(0);
//                    }
//                }
//            }
//        }
//
//        private class WriteThread implements Runnable {
//            public void run() {
//                while (true) {
//                    try {
//                        //发送数据
//                        if (sendBuffer != null) {
//                            //tss.sendMessage(1821,buffer);
//                            tss.sendMessage(sendBuffer);
//                            sendBuffer = null;//清空，不让它连续发
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

}
