package com.desaco.localnetsocketserviceandclient.forth_cs;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desaco.localnetsocketserviceandclient.R;

public class ForthMainActivity extends Activity implements OnClickListener {

    private BroadcastReceiver bcReceiver;
    public static Context s_context;

    TextView textView;
    EditText editText;
    Button btn;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);
        s_context = this;

        textView = (TextView) this.findViewById(R.id.res_txt);
        editText = (EditText) this.findViewById(R.id.socket_txt);
        btn = (Button) this.findViewById(R.id.send_btn);
        btn.setOnClickListener(this);

        textView.setText("服务器返回消息显示在此");

        NetManager.instance().init(this);

        SocketThreadManager.sharedInstance();

        regBroadcast();

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        showMsg("发送socket失败");
                        break;

                    case 1:
                        showMsg("发送socket成功");
                        break;

                }
            }
        };
    }

    @Override
    protected void onDestroy() {

        if (bcReceiver != null) {
            unregisterReceiver(bcReceiver);
        }
        super.onDestroy();
    }

    public void regBroadcast() {
        bcReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String value = intent.getStringExtra("response");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        textView.setText(value);
                    }
                });

            }
        };

        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(Const.BC);
        registerReceiver(bcReceiver, intentToReceiveFilter);
    }

    public void showMsg(String str) {
        Toast.makeText(ForthMainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String str = editText.getText().toString();
        if (!TextUtils.isEmpty(str)) {

            SocketThreadManager.sharedInstance().sendMsg(str.getBytes(), handler);
        }
    }
}
