package com.desaco.localnetsocketserviceandclient.client;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by desaco on 2018/5/11.
 */

public class SocketClient {
    private Socket client;
    private Context context;
    private int port;           //IP
    private String site;        //端口
    private Thread thread;
    public static Handler mHandler;
    private boolean isClient = false;
    private PrintWriter out;
    private InputStream in;
    private String str;

    /**
     * 调用时向类里传值
     */
    public SocketClient(Context context, String site, int port) {
        this.context = context;
        this.site = site;
        this.port = port;
    }

    /**
     * @effect 开启线程建立连接开启客户端
     */
    public void createConnet() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     *  connect()步骤
                     * */
                    client = new Socket(site, port);
                    client.setSoTimeout(5000);//设置超时时间
                    if (client != null) {
                        isClient = true;
                        forOut();
                        forIn();
                    } else {
                        isClient = false;
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }


    /**
     * @effect 得到输出字符串
     */
    public void forOut() {
        try {
            out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @steps read();
     * @effect 得到输入字符串
     */
    public void forIn() {
        while (isClient) {
            try {
                in = client.getInputStream();
                /**得到的是16进制数，需要进行解析*/
                byte[] bt = new byte[50];
                in.read(bt);
                str = new String(bt, "UTF-8");
                
            } catch (IOException e) {
            }
            if (str != null) {
                Message msg = new Message();
                msg.obj = str;
                mHandler.sendMessage(msg);//TODO
            }
        }
    }

    /**
     * @steps write();
     * @effect 发送消息
     */
    public void sendMsg(final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    out.print(str);
                    out.flush();
                } else {
                    isClient = false;
                }
            }
        }).start();
    }

    public void closeSocket() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (client != null) {
                client.close();
            }
        } catch (Exception ex) {

        }
    }

    public void resetConnet() {

    }
}
