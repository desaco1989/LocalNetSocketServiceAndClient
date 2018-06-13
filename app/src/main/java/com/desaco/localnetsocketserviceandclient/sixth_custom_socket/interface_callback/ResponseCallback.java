package com.desaco.localnetsocketserviceandclient.sixth_custom_socket.interface_callback;

import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.protocol.DataProtocol;

/**
 * Created by desaco on 2018/6/13.
 */

public interface ResponseCallback {
    void targetIsOffline(DataProtocol reciveMsg);

    void targetIsOnline(String clientIp);
}
