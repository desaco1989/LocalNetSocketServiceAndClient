package com.desaco.localnetsocketserviceandclient.second_cs;

import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lai on 2018/2/3.
 */

public class TcpServer implements Runnable {
    private Handler handler;
    private int port;
    private boolean isConnected;
    private ServerSocket server;
    private SocketManager socketManager;
    TcpServer(Handler handler)
    {
        this.handler=handler;
    }
    public void start(int port)
    {
        if(isConnected)
            close();
        this.port=port;
        new Thread(this).start();
    }
    public void close()
    {
        if(server.isBound())
        {
            try {
                server.close();
                handler.sendEmptyMessage(TcpMsg.SERVER_CLOSE_OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(isConnected)
        {
            socketManager.close();
            isConnected=false;
        }
    }
    public void send(String msg)
    {
        socketManager.send(msg);
    }
    @Override
    public void run() {
        try {
            server=null;
            server=new ServerSocket(port);
            handler.sendEmptyMessage(TcpMsg.BIND_OK);
            Socket socket=server.accept();
            socketManager=new SocketManager(socket,handler);
            socketManager.start();
            isConnected=true;
            handler.sendEmptyMessage(TcpMsg.CLINET_CONNECTED);
        } catch (IOException e) {
            e.printStackTrace();
            if(server==null)
                handler.sendEmptyMessage(TcpMsg.BIND_ERR);
        }

    }
}
