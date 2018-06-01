package com.desaco.localnetsocketserviceandclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desaco.localnetsocketserviceandclient.first_cs.MainActivity;
import com.desaco.localnetsocketserviceandclient.second_cs.SecondMainActivity;
import com.desaco.localnetsocketserviceandclient.third_cs.Server.ThirdServerActivity;
import com.desaco.localnetsocketserviceandclient.third_cs.client.ThirdClientActivity;

/**
 * Created by desaco on 2018/6/1.
 */

public class IndexActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
    }

    private void initView() {
        TextView showTv = (TextView) findViewById(R.id.show);
        showTv.setText("首页：");
        //first_bt
        Button firstBt = (Button) findViewById(R.id.first_bt);
        firstBt.setOnClickListener(this);
        Button secondBt = (Button) findViewById(R.id.second_bt);
        secondBt.setOnClickListener(this);
        //third_server_bt  third_client_bt
        Button thirdServerBt = (Button) findViewById(R.id.third_server_bt);
        thirdServerBt.setOnClickListener(this);
        Button thirdClientBt = (Button) findViewById(R.id.third_client_bt);
        thirdClientBt.setOnClickListener(this);
        //
        LinearLayout thirdLayout = (LinearLayout)findViewById(R.id.third_layout);
//        thirdLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_bt:
                //MainActivity
                jump(MainActivity.class);
                break;
            case R.id.second_bt:
                //SecondMainActivity
                jump(SecondMainActivity.class);
                break;
            case R.id.third_server_bt:
                //ThirdServerActivity
                jump(ThirdServerActivity.class);
                break;
            case R.id.third_client_bt:
                //ThirdClientActivity
                jump(ThirdClientActivity.class);
                break;
            default:
                break;
        }
    }

    private void jump(Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(IndexActivity.this, clazz);
        startActivity(intent);
    }
}
