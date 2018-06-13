package com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.client.bean;


import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.bean.TargetInfo;
import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.bean.TcpMsg;

/**
 */
public class UdpMsg extends TcpMsg {

    public UdpMsg(byte[] data, TargetInfo target, MsgType type) {
        super(data, target, type);
    }

    public UdpMsg(String data, TargetInfo target, MsgType type) {
        super(data, target, type);
    }

    public UdpMsg(int id) {
        super(id);
    }
}
