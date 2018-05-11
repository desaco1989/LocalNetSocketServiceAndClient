package com.desaco.localnetsocketserviceandclient.server;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by desaco on 2018/4/28.
 */

public class TcpSocketServerUtils {

    private ServerSocket ss = null;
    private Socket s = null;
    private OutputStream out = null;
    private InputStream in = null;
    private String receiveBuffer = null;

    public TcpSocketServerUtils(int port) {
        //新建ServerSocket对象,端口为传进来的port;
        try {
            ss = new ServerSocket(port);
            //开始监听数据
            s = ss.accept();
            out = s.getOutputStream();
            in = s.getInputStream();
            Log.e("desaco", "开启成功 port=" + port);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("desaco", "开启失败 port=" + port);
        }
    }

    /**
     * @steps write();
     * @effect socket服务端发送信息
     */
    public void sendMessage(final String chat) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(s.getOutputStream());
                    out.print(chat);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public String getMessage() {
        byte[] temp = new byte[1024];
        try {
            if (in.read(temp) > 0) {
                return receiveBuffer = new String(temp).trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeStream() {
        try {
            if (ss != null) {
                ss.close();
            }
            if (s != null) {
                s.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {

        }
    }
}
