package com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.udp_client.manager;

import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class UdpSocketManager {
    private static Map<Integer, DatagramSocket> sDatagramSockets = new HashMap();

    public static void putUdpSocket(DatagramSocket socket) {
        sDatagramSockets.put(socket.getLocalPort(), socket);
    }

    public static DatagramSocket getUdpSocket(int port) {
        return sDatagramSockets.get(port);
    }
}
