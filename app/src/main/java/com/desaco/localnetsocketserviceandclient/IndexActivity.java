package com.desaco.localnetsocketserviceandclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desaco.localnetsocketserviceandclient.fifth_cs.xsocket.activity.FifthMainActivity;
import com.desaco.localnetsocketserviceandclient.first_cs.FirstMainActivity;
import com.desaco.localnetsocketserviceandclient.second_cs.SecondMainActivity;
import com.desaco.localnetsocketserviceandclient.sixth_custom_socket.SixthMainActivity;
import com.desaco.localnetsocketserviceandclient.third_cs.Server.ThirdServerActivity;
import com.desaco.localnetsocketserviceandclient.third_cs.client.ThirdClientActivity;

/**
 * Created by desaco on 2018/6/1.
 * 客户端与服务器端，可以在一个手机上，也可以在不同的手机上
 * <p>
 * 局域网通信只是一对一，还不能一对多？？？
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
        //第1种
        Button firstBt = (Button) findViewById(R.id.first_bt);
        firstBt.setOnClickListener(this);
        //第2种
        Button secondBt = (Button) findViewById(R.id.second_bt);
        secondBt.setOnClickListener(this);
        //第三种 只有简单的Socket Activity界面，
        LinearLayout thirdLayout = (LinearLayout) findViewById(R.id.third_layout);
//        thirdLayout.setVisibility(View.VISIBLE);
        Button thirdServerBt = (Button) findViewById(R.id.third_server_bt);
        thirdServerBt.setOnClickListener(this);
        Button thirdClientBt = (Button) findViewById(R.id.third_client_bt);
        thirdClientBt.setOnClickListener(this);
        //第四种更是没有Activity界面

        //第5种更是没有Activity界面
        Button fifthClientBt = (Button) findViewById(R.id.fifth_cs_bt);
        fifthClientBt.setOnClickListener(this);
        // 第6种更是没有Activity界面
        Button sixthClientBt = (Button) findViewById(R.id.sixth_cs_bt);
        sixthClientBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_bt://第1种Socket实现 TODO
                jump(FirstMainActivity.class);
                break;
            case R.id.second_bt://第2种Socket实现 TODO
                jump(SecondMainActivity.class);
                break;
            case R.id.third_server_bt://第三种通信有Bug
                //ThirdServerActivity
                jump(ThirdServerActivity.class);
                break;
            case R.id.third_client_bt://第三种通信有Bug
                //ThirdClientActivity
                jump(ThirdClientActivity.class);
                break;
            case R.id.fifth_cs_bt:
                jump(FifthMainActivity.class);//第5种通信，自定义Socket协议 开源项目XAndroidSocket
                break;
            case R.id.sixth_cs_bt:
                jump(SixthMainActivity.class);//第6种通信，自定义Socket协议
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
