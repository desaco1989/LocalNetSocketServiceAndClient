package com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.manager;


import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.XTcpClient;
import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.bean.TargetInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * tcpclient的管理者
 */
public class TcpClientManager {
    private static Set<XTcpClient> sMXTcpClients = new HashSet<>();

    public static void putTcpClient(XTcpClient XTcpClient) {
        sMXTcpClients.add(XTcpClient);
    }

    public static XTcpClient getTcpClient(TargetInfo targetInfo) {
        for (XTcpClient tc : sMXTcpClients) {
            if (tc.getTargetInfo().equals(targetInfo)) {
                return tc;
            }
        }
        return null;
    }
}
