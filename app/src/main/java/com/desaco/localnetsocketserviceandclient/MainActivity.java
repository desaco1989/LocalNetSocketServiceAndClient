package com.desaco.localnetsocketserviceandclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.desaco.localnetsocketserviceandclient.client.ClientPageActivity;
import com.desaco.localnetsocketserviceandclient.second_cs.SecondMainActivity;
import com.desaco.localnetsocketserviceandclient.server.ServerPageActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
    }

    private void initView() {
        //first_bt
        Button firstBt = (Button) findViewById(R.id.first_bt);
        firstBt.setText("第一种：cs Socket通信服务器端跳转");
        firstBt.setOnClickListener(this);

        Button secondBt = (Button) findViewById(R.id.second_bt);
        secondBt.setText("第一种：cs Socket通信客户端跳转");
        secondBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_bt:
                //cs模式的服务器端
                jump(ServerPageActivity.class);
                break;
            case R.id.second_bt:
                //cs模式的客户端
                jump(ClientPageActivity.class);
                break;
            default:
                break;
        }
    }

    private void jump(Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, clazz);
        startActivity(intent);
    }
}
