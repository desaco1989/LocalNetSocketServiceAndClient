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
import com.desaco.localnetsocketserviceandclient.server.ServerPageActivity;

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
 * https://blog.csdn.net/u010661782/article/details/50946760
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

    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server_client);
        mContext = this;

        initView();
        initListener();
    }

    private void initView() {
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
        //connect_service_tips
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
    private SocketClient client;

    private void initListener() {

         /*开启socket通信*/
        connectServiceBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ip = machineIpEt.getText().toString();
                if (ip == null || ip.equals("")) {
                    Toast.makeText(mContext, "please input Server IP", Toast.LENGTH_SHORT).show();
                    return;
                }
                String portStr = machinePortEt.getText().toString();
                if (portStr == null || portStr.equals("")) {
                    Toast.makeText(mContext, "please input Server port", Toast.LENGTH_SHORT).show();
                    return;
                }
                port = Integer.parseInt(portStr);
                client = new SocketClient(mContext, ip, port);
//                client.init(mContext, ip, port);
                connectServiceBt.setText("已经连接了！！");

                //开启客户端接收消息线程
                client.createConnet();
            }
        });

        /*发送数据*/
        msgSendBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String data = msgContentEt.getText().toString();
                if (data == null) {
                    Toast.makeText(mContext, "please input Sending Data", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //在后面加上 '\0' ,是为了在服务端方便我们去解析；
                    data = "client:" + data + '\0';
                }
                client.sendMsg(data);
            }
        });

        SocketClient.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                oneMsgShowTv.setText(msg.obj.toString());
                Toast.makeText(ClientPageActivity.this, "收到来自服务器端的消息： " + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

}
