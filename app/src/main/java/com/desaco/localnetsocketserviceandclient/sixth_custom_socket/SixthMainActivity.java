package com.desaco.localnetsocketserviceandclient.sixth_custom_socket;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.desaco.localnetsocketserviceandclient.R;
import com.desaco.localnetsocketserviceandclient.common_utils.CommonUtils;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.interface_callback.RequestCallBack;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.protocol.BasicProtocol;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.utils.Config;

/**
 * 自定义通信Socket协议
 * Created by desaco on 2018/6/13.
 * <p>
 * https://blog.csdn.net/u010818425/article/details/53448817
 */

public class SixthMainActivity extends Activity implements View.OnClickListener {

    private TextView serverIpTv, serverMsgTv;
    private Button startServiceBt;
    //
    private EditText clientIpEt, clientPortEt;
    private Button conncetServiceBt;
    private TextView clientMsgTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sixth_main_activity);

        Config.ADDRESS = CommonUtils.getMobileIp(this);

        initView();
        initData();
    }

    private void initView() {
        //服务器端
        serverIpTv = (TextView) findViewById(R.id.server_ip_port);
        serverMsgTv = (TextView) findViewById(R.id.server_receive_msg);
        startServiceBt = (Button) findViewById(R.id.start_service_bt);
        startServiceBt.setOnClickListener(this);
        //客户端
        clientIpEt = (EditText) findViewById(R.id.client_ip);
        clientIpEt.setText(Config.ADDRESS);

        clientPortEt = (EditText) findViewById(R.id.client_port);
        clientPortEt.setText("" + Config.PORT);

        conncetServiceBt = (Button) findViewById(R.id.conncet_service_bt);
        conncetServiceBt.setOnClickListener(this);

        clientMsgTv = (TextView) findViewById(R.id.client_receive_msg);


    }

    private void initData() {
        serverIpTv.setText("ip=" + Config.ADDRESS + ", port=" + Config.PORT);
    }

    private ClientConnection clientConnection;
    private ServerConnection serverConnection;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_service_bt://开启服务器的服务
                serverConnection = new ServerConnection();
                serverConnection.startServer();
                break;
            case R.id.conncet_service_bt://客户端去连接服务器
                clientConnection = new ClientConnection(new RequestCallBack() {
                    @Override
                    public void onSuccess(BasicProtocol msg) {
                        //连接成功
                    }

                    @Override
                    public void onFailed(int errorCode, String msg) {
                        //连接失败
                    }
                });
                break;
            default:
                break;

        }
    }
}
