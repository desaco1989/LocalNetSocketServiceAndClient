package com.desaco.localnetsocketserviceandclient.second_cs;

import android.os.Handler;

import com.desaco.localnetsocketserviceandclient.second_cs.SocketManager;
import com.desaco.localnetsocketserviceandclient.second_cs.TcpMsg;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by lai on 2018/2/3.
 */

public class TcpServer implements Runnable {
    private Handler handler;
    private int port;
    private boolean isConnected;
    private ServerSocket server;
    private SocketManager socketManager;

    TcpServer(Handler handler) {
        this.handler = handler;
    }

    public void start(int port) {
        if (isConnected) {
            close();
        }
        this.port = port;
        new Thread(this).start();
    }

    public void close() {
        if (server.isBound()) {
            try {
                server.close();
                handler.sendEmptyMessage(TcpMsg.SERVER_CLOSE_OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isConnected) {
            socketManager.close();
            isConnected = false;
        }
    }

    public void send(String msg) {
        socketManager.send(msg);
    }

    LinkedList<Socket> list = new LinkedList<Socket>();
    public void distributeMsg(String msg){
        // 把收到的信息转发给其它客户端
        for (Socket s : list) {
            send(msg);
//            if (s != client) {
//                OutputStream outputStream = s.getOutputStream();
//                outputStream.write(text.getBytes());
//            }
        }
    }

    @Override
    public void run() {
        try {
            server = null;
            server = new ServerSocket(port);
            handler.sendEmptyMessage(TcpMsg.BIND_OK);

            //TODO
            System.out.println("准备阻塞...");
            Socket socket = server.accept();
            System.out.println("阻塞完成...");
            // 添加到集合里
            list.add(socket);

            socketManager = new SocketManager(socket, handler);
            socketManager.start();
            isConnected = true;
            handler.sendEmptyMessage(TcpMsg.CLINET_CONNECTED);
        } catch (IOException e) {
            e.printStackTrace();
            if (server == null)
                handler.sendEmptyMessage(TcpMsg.BIND_ERR);
        }

    }
}
