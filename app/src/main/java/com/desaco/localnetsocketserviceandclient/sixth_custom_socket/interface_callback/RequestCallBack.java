package com.desaco.localnetsocketserviceandclient.sixth_custom_socket.interface_callback;

import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.protocol.BasicProtocol;

/**
 * Created by desaco on 2018/6/13.
 */

public interface RequestCallBack {
    void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);
}
