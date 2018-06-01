package com.desaco.localnetsocketserviceandclient.second_cs;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ClientFragment extends Fragment implements View.OnClickListener {
    private TcpClient client;
    private AlertDialog dlg;
    private EditText ip, port, send;
    private String sip, sport;
    private TextView recv, status;
    private Button conn;
    private boolean isConnected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client, null);

        send = v.findViewById(R.id.etx);
        recv = v.findViewById(R.id.recv);
        status = v.findViewById(R.id.tv);
        conn = v.findViewById(R.id.toolbarBtn);
        setClick(this, v, R.id.send, R.id.toolbarBtn, R.id.clear);
        return v;
    }

    private void setClick(View.OnClickListener listener, View v, int... ids) {
        for (int i : ids)
            v.findViewById(i).setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                if (!isConnected) {
                    show("未连接");
                    return;
                }
                String s = send.getText().toString();
                if (s.isEmpty()) {
                    show("要发送内容为空");
                    return;
                }
                client.send(s + '\n');
                break;
            case R.id.toolbarBtn:
                if (isConnected) {
                    client.close();
                } else
                    dlg();
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
                    case TcpMsg.CONNECT_OK:
                        show("连接成功");
                        isConnected = true;
                        status.setText(sip + ':' + sport);
                        conn.setText("断开");
                        break;
                    case TcpMsg.CONNECT_ERR:
                        show("连接失败");
                        break;
                    case TcpMsg.UNEXPECTED_CLOSE:
                        show("远程已断开");
                        isConnected = false;
                        status.setText("");
                        conn.setText("连接");
                        break;
                    case TcpMsg.CLIENT_CLOSE_OK:
                        show("断开成功");
                        isConnected = false;
                        status.setText("");
                        conn.setText("连接");
                        break;
                    case TcpMsg.CLIENT_CLOSE_ERR:
                        show("断开失败");
                        break;
                    case TcpMsg.MSG:
                        recv.append((String) message.obj);
                        break;
                }

                return false;
            }
        });
        client = new TcpClient(handler);
    }

    private void dlg() {
        if (dlg == null) {
            View v = getLayoutInflater().inflate(R.layout.dlg, null);
            ip = v.findViewById(R.id.etx1);
            port = v.findViewById(R.id.etx2);
            dlg = new AlertDialog.Builder(getContext())
                    .setView(v)
                    .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String s = ip.getText().toString();
                            String p = port.getText().toString();
                            sip = s;
                            sport = p;
                            if (s.isEmpty() || p.isEmpty()) {
                                show("输入不正确");
                                return;
                            }
                            int n = Integer.valueOf(p);
                            client.start(s, n);

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

}
