package com.desaco.localnetsocketserviceandclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.desaco.localnetsocketserviceandclient.second_cs.SecondMainActivity;

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
        TextView showTv = (TextView)findViewById(R.id.show);
        showTv.setText("首页");
        //first_bt
        Button firstBt = (Button) findViewById(R.id.first_bt);
        firstBt.setOnClickListener(this);
        Button secondBt = (Button) findViewById(R.id.second_bt);
        secondBt.setOnClickListener(this);
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
