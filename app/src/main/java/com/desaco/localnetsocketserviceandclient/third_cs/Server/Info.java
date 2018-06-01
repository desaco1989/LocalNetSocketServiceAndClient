package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import android.app.Application;

//最长生命周期
public class Info {//extends Application

    public static final int SERVERSOCKET_PORT = 8888;// socket端口号
    public static String SERVERSOCKET_IP = "";// 本机ip
    public static boolean SHOW_LOG_FLAG = true;// debug输出标志
    public static final int NETWORK_TYPE_WIFI = 0;
    public static final int NETWORK_TYPE_PHONE = 1;
    public static final int NETWORK_TYPE_OTHER = 2;
    public static final int CONNECT_SUCCESS = 0; // 客户端连接成功
    public static final int CONNECT_GETDATA = 1;// 获得客户端数据
    public static final int CONNECT_FAIL = 2;// 有客户端断开
    public static boolean SERVICE_IS_ALIVE = false;//服务是否开启的标志位
}
