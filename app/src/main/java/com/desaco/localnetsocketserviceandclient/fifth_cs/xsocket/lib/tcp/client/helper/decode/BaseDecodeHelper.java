package com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.helper.decode;


import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.TcpConnConfig;
import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.tcp.client.bean.TargetInfo;

public class BaseDecodeHelper implements AbsDecodeHelper {
    @Override
    public byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig) {
        return new byte[][]{data};
    }
}
