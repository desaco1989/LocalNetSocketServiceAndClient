package com.desaco.localnetsocketserviceandclient.sixth_custom_socket;

import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.interface_callback.ResponseCallback;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.protocol.DataProtocol;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.task.ServerResponseTask;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.utils.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by desaco on 2018/6/13.
 */

public class ServerConnection {
    private static boolean isStart = true;
    private static ServerResponseTask serverResponseTask;

    public ServerConnection() {

    }

    public static void startServer() {
        ServerSocket serverSocket = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(Config.PORT);
            while (isStart) {
                Socket socket = serverSocket.accept();
                serverResponseTask = new ServerResponseTask(socket,
                        new ResponseCallback() {

                            @Override
                            public void targetIsOffline(DataProtocol reciveMsg) {// 离线 TODO
                                if (reciveMsg != null) {
                                    System.out.println("Offline,离线");
                                    System.out.println("" + reciveMsg.getData());
                                }
                            }

                            @Override
                            public void targetIsOnline(String clientIp) {//在线 TODO
                                System.out.println(clientIp + " is onLine");
                                System.out.println("-----------------------------------------");
                            }
                        });

                if (socket.isConnected()) {
                    executorService.execute(serverResponseTask);
                }
            }

            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    isStart = false;
                    serverSocket.close();
                    if (serverSocket != null)
                        serverResponseTask.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
