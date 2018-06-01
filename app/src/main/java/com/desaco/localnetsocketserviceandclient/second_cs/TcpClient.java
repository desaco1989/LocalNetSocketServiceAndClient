package com.desaco.localnetsocketserviceandclient.second_cs;

import android.os.Handler;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by lai on 2018/2/3.
 */

public class TcpClient implements Runnable {
    private Handler handler;
    private String host;
    private int port;
    private boolean isConnected;
    private SocketManager socketManager;
    TcpClient(Handler handler)
    {
        this.handler=handler;
    }
    public void start(String host, int port)
    {
        this.host=host;
        this.port=port;
        if(isConnected)
            close();
        new Thread(this).start();
    }
    public void close()
    {
        if(isConnected) {
            socketManager.close();
            isConnected = false;
        }
    }
    public void send(String msg)
    {
        socketManager.send(msg);
    }
    @Override
    public void run() {
        try {
            Socket socket=new Socket(host,port);
            handler.sendEmptyMessage(TcpMsg.CONNECT_OK);
            isConnected=true;
            socketManager=new SocketManager(socket,handler);
            socketManager.start();
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(TcpMsg.CONNECT_ERR);
        }
    }

}
