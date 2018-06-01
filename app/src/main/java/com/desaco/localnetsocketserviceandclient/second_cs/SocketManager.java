package com.desaco.localnetsocketserviceandclient.second_cs;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by lai on 2018/2/4.
 */

public class SocketManager extends Thread {
    private Socket socket;
    private BufferedWriter bw;
    private boolean noTask, unexpectedClose;
    private int what;
    private String msg;
    private Handler handler;
    protected boolean isConnected;

    SocketManager(Socket socket, Handler handler) throws IOException {
        this.socket = socket;
        this.handler = handler;
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        noTask = true;
        isConnected = true;
    }

    public void send(String s) {
        if (!isConnected)
            return;
        what = 0;
        msg = s;
        noTask = false;
        synchronized (this) {
            notify();
        }
    }

    public void close() {
        if (socket.isClosed())
            return;
        what = -1;
        noTask = false;
        synchronized (this) {
            notify();
        }
    }

    private class Listener extends Thread {
        private BufferedReader br;

        Listener(BufferedReader br) {
            this.br = br;
        }

        @Override
        public void run() {
            String s;
            while (true) {
                try {
                    while ((s = br.readLine()) != null) {
                        Message msg = new Message();
                        msg.what = TcpMsg.MSG;
                        msg.obj = s + '\n';
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {//可能是由于socket主动关闭或者网络错误引发
                    e.printStackTrace();
                    if (!socket.isClosed()) {
                        unexpectedClose = true;
                        close();
                        handler.sendEmptyMessage(TcpMsg.UNEXPECTED_CLOSE);
                    }
                    return;
                }
                if (s == null)//由远程关闭引发
                {
                    unexpectedClose = true;
                    close();
                    handler.sendEmptyMessage(TcpMsg.UNEXPECTED_CLOSE);
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Listener(br).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            synchronized (this) {
                while (noTask)
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            noTask = true;

            switch (what) {
                case 0:
                    try {
                        bw.write(msg);
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(TcpMsg.UNEXPECTED_CLOSE);
                    }
                    break;
                case -1:
                    try {
                        socket.close();
                        if (!unexpectedClose)
                            handler.sendEmptyMessage(TcpMsg.CLIENT_CLOSE_OK);
                    } catch (IOException e) {
                        if (!unexpectedClose)
                            handler.sendEmptyMessage(TcpMsg.CLIENT_CLOSE_ERR);
                    }
                    isConnected = false;
                    return;
            }
        }
    }
}
