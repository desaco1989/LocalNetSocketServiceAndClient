package com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.udp_client.listener;


import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.udp_client.XUdp;
import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.lib.udp_client.bean.UdpMsg;

/**
 */
public interface UdpClientListener {
    void onStarted(XUdp XUdp);

    void onStoped(XUdp XUdp);

    void onSended(XUdp XUdp, UdpMsg udpMsg);

    void onReceive(XUdp client, UdpMsg udpMsg);

    void onError(XUdp client, String msg, Exception e);

    class SimpleUdpClientListener implements UdpClientListener {

        @Override
        public void onStarted(XUdp XUdp) {

        }

        @Override
        public void onStoped(XUdp XUdp) {

        }

        @Override
        public void onSended(XUdp XUdp, UdpMsg udpMsg) {

        }

        @Override
        public void onReceive(XUdp client, UdpMsg msg) {

        }

        @Override
        public void onError(XUdp client, String msg, Exception e) {

        }
    }

}
