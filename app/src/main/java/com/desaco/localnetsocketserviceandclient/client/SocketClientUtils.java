package com.desaco.localnetsocketserviceandclient.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by desaco on 2018/4/28.
 */

public class SocketClientUtils {
    private static Socket s = null;

    private SocketClientUtils() {

    }

    public static Socket getSocket(String ip, int port) {
        try {
            s = new Socket(ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
