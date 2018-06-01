package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

/**
 * socketservice及多客户端创建、管理类
 *
 * @author thinkpad
 */
public class ServersSocket {
    private static ServersSocket socketServer = null;
    private ServerSocket serverSocket = null;
    private ClientSocketManager mClientSocketManager;
    private boolean allThreadStop = false;// 所有线程循环条件，当需要停止所有线程的时候把这个标志置为true
    private boolean stopFlag = false;// 接收客户端信息线程的标志位，当false时，一直等待客户端输入
    private static long time = 0;// 与timeflag一起判断客户端意外退出时造成的无限接收空情况
    private static int timeFlag = 0;//
    private Handler mHandler = null;

    private ServersSocket() {

    }

    /**
     * 单例
     *
     * @return
     */
    public synchronized static ServersSocket getInstance() {
        if (socketServer == null) {
            socketServer = new ServersSocket();
        }
        return socketServer;
    }

    /**
     * 启动socketserver port端口号
     */
    public void startServer(final ClientDataCallBack callBack) {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int flag = msg.what;
                String str = (String) msg.obj;
                callBack.getClientData(flag, str);
            }
        };
        try {
            mClientSocketManager = ClientSocketManager.getInstence();
            mClientSocketManager.setLimit(true);// 设置限制最大客户端数
            mClientSocketManager.setLimitNum(6);// 设置最大客户端数为6
            serverSocket = new ServerSocket(Info.SERVERSOCKET_PORT);
            ShowLogManager.outputDebug("tag", "Create ServerSocket success!");
            new Thread(waitClientConnection).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止socket连接
     */
    public void stopServer() {
        stopFlag = true;
        allThreadStop = true;
        clear();
    }

    /**
     * 清除所有数据
     */
    private void clear() {
        if (mClientSocketManager != null) {
            mClientSocketManager.closeSocket();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socketServer = null;

    }

    /**
     * 服务端
     *
     * @return
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * 等待客户端连接
     *
     * @return
     */

    private Runnable waitClientConnection = new Runnable() {

        @Override
        public void run() {
            while (!stopFlag && !allThreadStop) {
                if (serverSocket != null) {
                    try {
                        ShowLogManager.outputDebug("tag",
                                "serverSocket waiting!");
                        Socket mSocket = serverSocket.accept();
                        MyThread thread = new MyThread(mSocket);
                        thread.start();// 有新客户端连接进来，有几个开几个线程
                        mClientSocketManager.putSocket(mSocket.getInetAddress()
                                .getHostAddress(), mSocket, thread);// 添加到客户端管理类中
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowLogManager.outputDebug("tag", "serverSocket is null!");

                }
            }
            ShowLogManager.outputDebug("tag",
                    "Wating Thread is exit! and stopFlag:" + stopFlag);
        }
    };

    /**
     * 接收客户端数据
     */
    private class MyThread extends Thread {
        private Socket mSocket;

        MyThread(Socket mSocket) {
            this.mSocket = mSocket;
        }

        @Override
        public void run() {
            super.run();
            InputStream mInputStream = null;
            OutputStream outStream = null;
            if (mSocket != null) {
                boolean stopFlags = false;// 循环标志置为不停止
                try {
                    mInputStream = mSocket.getInputStream();// 获得输入流
                    outStream = mSocket.getOutputStream();// 获得输出流
                    String clientIP = mSocket.getInetAddress().getHostAddress();// 获得客户端IP
                    sendMessages(Info.CONNECT_SUCCESS, clientIP);// 连接成功的回调
                    while (!stopFlags && !allThreadStop) {
                        byte[] buf = new byte[10240];
                        mInputStream.read(buf);// 读取客户端数据
                        // 当客户端非正常退出时，会无线发送空包，因此加入判断，避免造成无限接收空包
                        if (time != 0
                                && System.currentTimeMillis() - time < 200
                                && timeFlag > 5) {// 说明客户端断开，应断掉这个线程
                            stopFlags = true;
                            time = 0;
                            timeFlag = 0;
                            mClientSocketManager.removeSocket(clientIP);// 在管理列表中去除这个客户端的所有信息
                            sendMessages(Info.CONNECT_FAIL, clientIP);// 客户端退出回调IP
                            continue;
                        } else if (time != 0
                                && System.currentTimeMillis() - time >= 200) {// 偶尔一次空包，数据清零
                            timeFlag = 0;
                        } else if (time != 0) {// 确定是空包，把空包计数器增加
                            timeFlag++;
                        }
                        time = System.currentTimeMillis();
                        String str = new String(buf, "utf-8").trim();// 转码
                        if (str != null && !"".equals(str) && !" ".equals(str)) {
                            if ("IHAVEQUIT".equals(str)) {// 客户端正常退出时发送过来的数据
                                mClientSocketManager.removeSocket(clientIP);// 在管理列表中去除这个客户端的所有信息
                                sendMessages(Info.CONNECT_FAIL, clientIP);// 客户端退出回调
                            } else if ("IAMINTHETEST".equals(str)) {// 客户端的心跳测试
                                outStream.write("YOUSTAYONLINE"
                                        .getBytes("utf-8"));
                                outStream.flush();
                            } else {// 正常的数据传输
                                sendMessages(Info.CONNECT_GETDATA, str);
                                mClientSocketManager.setFrequency(clientIP);// 此客户端活动量增加，用来记录次客户端的活动量
                            }
                        }
                        buf = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (mInputStream != null) {
                        try {
                            mInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        /**
         * 需回调数据
         *
         * @param flag
         * @param message
         */
        private void sendMessages(int flag, String message) {
            Message msg = mHandler.obtainMessage();
            msg.what = flag;
            msg.obj = message;
            mHandler.sendMessage(msg);// 连接成功回调客户端IP
        }
    }

    /**
     * 客户端数据回调接口
     *
     * @author thinkpad
     */
    public interface ClientDataCallBack {
        public void getClientData(int connectMode, String str);
    }
}
