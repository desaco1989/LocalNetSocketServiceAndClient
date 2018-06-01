package com.desaco.localnetsocketserviceandclient.third_cs.client;

import java.net.SocketException;

import com.desaco.localnetsocketserviceandclient.R;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class ThirdClientActivity extends Activity {
    //

    private TextView tv_show;
    private UDPSocketBroadCast mBroadCast;
    private SocketClientManager mClientManager;
    private String localIP;
    private TextView tv_showip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_client);

        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_showip = (TextView) findViewById(R.id.tv_showip);
        mBroadCast = new UDPSocketBroadCast();
        try {
            localIP = ConnectionManager.getLocalIP();
            tv_showip.setText("本机IP:" + localIP);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tv_show.setText("正在自动连接中...");
        mClientManager = SocketClientManager.getInstance();

        //TODO
        Info.SERVER_IP = "192.168.3.50";
        Info.SERVER_PORT = 8888;
        mClientManager.startClientScoket(Info.SERVER_IP,
                Info.SERVER_PORT);
        tv_show.setText("已连接到：" + Info.SERVER_IP);

        // 接收到的udp广播
//        mBroadCast.startUDP(new UDPSocketBroadCast.UDPDataCallBack() {
//            @Override
//            public void mCallback(String str) {
//                if (str != null && !"error".equals(str)) {
//                    String[] strs = str.split("-");
//                    if (strs.length > 2) {
//                        Info.SERVER_IP = strs[1];
//                        Info.SERVER_PORT = Integer.parseInt(strs[2]);
//                        tv_show.setText("已连接到：" + strs[1]);
//                        mClientManager.startClientScoket(strs[1],
//                                Integer.parseInt(strs[2]));
//                    }
//                }
//            }
//        });

        /**
         * 获得socket的状态，比如断开
         */
        mClientManager.getSocketMessage(new SocketClientManager.SocketClientCallBack() {
            @Override
            public void callBack(int what) {
                // TODO Auto-generated method stub
                switch (what) {
                    case 1:// 与服务器断开
                        tv_show.setText("与服务器断开，正在重新连接...");
                        mBroadCast.reStartUDP();
                        break;
                }
            }
        });
    }
}
