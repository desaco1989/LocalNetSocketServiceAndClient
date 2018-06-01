package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desaco.localnetsocketserviceandclient.R;

public class ThirdServerActivity extends Activity {
    private ListView lv;
    private TextView tv;
    private Button btn;
    private Intent intent;
    private ShowIPtAdapter adapter;
    private List<String> dataResource;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_server);
        lv = (ListView) findViewById(R.id.lv);
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.textView2);
        dataResource = new ArrayList<String>();
        adapter = new ShowIPtAdapter(this);
        adapter.bindData(dataResource);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        intent = new Intent(this, SocketService.class);
        handleMethod();
        showIP();
    }

    private void showIP() {
        // TODO Auto-generated method stub
        if (ConnectionManager.hasActivityConn(getApplicationContext())) {
            String ip = "Not connectivity";
            String networkType = "";
            try {
                ip = ConnectionManager.getLocalIP();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            switch (ConnectionManager.getIPType(getApplicationContext())) {
                case Info.NETWORK_TYPE_WIFI:
                    networkType = "WIFI";
                    tv.setText("本机IP地址：" + ip);
                    startService(intent);// 启动服务
                    break;
                case Info.NETWORK_TYPE_PHONE:
                    networkType = "手机网络";
                    tv.setText(networkType + "，请打开WIFI");
                    break;
                case Info.NETWORK_TYPE_OTHER:
                    networkType = "未识别的网络";
                    tv.setText(networkType + "，请打开WIFI");
                    break;
            }

        } else {
            Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void handleMethod() {
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!Info.SERVICE_IS_ALIVE) {
                    startService(intent);
                    btn.setText("关闭服务");
                } else {
                    stopService(intent);
                    btn.setText("打开服务");
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("updata");
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }

    // 我这里把服务关掉了
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopService(intent);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("str");
            switch (intent.getIntExtra("flag", 0)) {
                case Info.CONNECT_SUCCESS:
                    adapter.addData(str);
                    str = "客户端IP:" + str;
                    break;
                case Info.CONNECT_FAIL:
                    adapter.removeData(str);
                    str = "客户端：" + str + "断开";
                    break;
                case Info.CONNECT_GETDATA:
                    str = "信息：" + str;
                    break;
            }
            tv.setText("" + str);
        }

    };
}
