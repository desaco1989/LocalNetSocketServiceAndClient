package com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.server.listener;


import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.XTcpClient;
import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.bean.TcpMsg;
import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.server.XTcpServer;

/**
 * tcpserver
 */
public interface TcpServerListener {
    void onCreated(XTcpServer server);

    void onListened(XTcpServer server);

    void onAccept(XTcpServer server, XTcpClient tcpClient);

    void onSended(XTcpServer server, XTcpClient tcpClient, TcpMsg tcpMsg);

    void onReceive(XTcpServer server, XTcpClient tcpClient, TcpMsg tcpMsg);

    void onValidationFail(XTcpServer server, XTcpClient client, TcpMsg tcpMsg);

    void onClientClosed(XTcpServer server, XTcpClient tcpClient, String msg, Exception e);

    void onServerClosed(XTcpServer server, String msg, Exception e);
}
