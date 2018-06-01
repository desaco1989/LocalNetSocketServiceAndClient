package com.desaco.localnetsocketserviceandclient.second_cs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desaco.localnetsocketserviceandclient.R;

/**
 * Created by lai on 2018/2/4.
 */

public class ServerFragment extends Fragment implements View.OnClickListener {
    private TcpServer server;
    private AlertDialog dlg;
    private EditText port, send;
    private String sport;
    private TextView recv, status;
    private Button conn;
    private boolean isConnected, isBound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.server, null);

        send = v.findViewById(R.id.etx);
        recv = v.findViewById(R.id.recv);
        status = v.findViewById(R.id.tv);
        conn = v.findViewById(R.id.toolbarBtn);
        setClick(this, v, R.id.send, R.id.toolbarBtn, R.id.clear);

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        //IP地址
        TextView showIP = (TextView) v.findViewById(R.id.show_ip_tv);
        showIP.setText(ip);

        return v;
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                if (!isConnected) {
                    show("客户端未连接");
                    return;
                }
                String s = send.getText().toString();
                if (s.isEmpty()) {
                    show("要发送内容为空");
                    return;
                }
                server.send(s + '\n');
                break;
            case R.id.toolbarBtn:
                if (isBound) {
                    server.close();
                } else {
                    dlg();
                }
                break;
            case R.id.clear:
                recv.setText(null);
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case TcpMsg.CLINET_CONNECTED:
                        show("客户端连接");
                        isConnected = true;
                        break;
                    case TcpMsg.BIND_OK:
                        show("绑定端口成功");
                        isBound = true;
                        status.setText("绑定端口:" + sport);
                        conn.setText("关闭");
                        break;
                    case TcpMsg.BIND_ERR:
                        show("绑定端口失败");
                        break;
                    case TcpMsg.UNEXPECTED_CLOSE://TODO
                        server.close();
                        show("客户端已断开");
                        isConnected = false;
                        status.setText("");
                        conn.setText("绑定");
                        break;
                    case TcpMsg.SERVER_CLOSE_OK:
                        show("端口已释放");
                        isConnected = false;
                        isBound = false;
                        status.setText("");
                        conn.setText("绑定");
                        break;
                    case TcpMsg.SERVER_CLOSE_ERR:
                        show("端口释放失败");
                        break;
                    case TcpMsg.MSG:
                        recv.append((String) message.obj);

                        server.distributeMsg((String) message.obj);//TODO
                        break;
                }

                return false;
            }
        });
        server = new TcpServer(handler);
    }

    private void dlg() {
        if (dlg == null) {
            View v = getLayoutInflater().inflate(R.layout.dlg2, null);
            port = v.findViewById(R.id.etx2);
            dlg = new AlertDialog.Builder(getContext())
                    .setView(v)
                    .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String p = port.getText().toString();
                            sport = p;
                            if (p.isEmpty()) {
                                show("输入不正确");
                                return;
                            }
                            int n = Integer.valueOf(p);
                            server.start(n);
                            dlg.dismiss();
                        }
                    })
                    .create();
        }
        dlg.show();
    }

    private void show(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }


    private void setClick(View.OnClickListener listener, View v, int... ids) {
        for (int i : ids)
            v.findViewById(i).setOnClickListener(listener);
    }
}
