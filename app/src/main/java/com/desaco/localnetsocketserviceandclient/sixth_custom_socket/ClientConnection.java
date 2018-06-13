package com.desaco.localnetsocketserviceandclient.sixth_custom_socket;

import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.interface_callback.RequestCallBack;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.protocol.DataProtocol;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.task.ClientRequestTask;

/**
 * Created by desaco on 2018/6/13.
 */

public class ClientConnection {
    private boolean isClosed;

    private ClientRequestTask mClientRequestTask;

    public ClientConnection(RequestCallBack requestCallBack) {
        mClientRequestTask = new ClientRequestTask(requestCallBack);
        new Thread(mClientRequestTask).start();
    }

    public void addNewRequest(DataProtocol data) {
        if (mClientRequestTask != null && !isClosed)
            mClientRequestTask.addRequest(data);
    }

    public void closeConnect() {
        isClosed = true;
        mClientRequestTask.stop();
    }
}
