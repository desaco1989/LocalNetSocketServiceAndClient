package com.desaco.localnetsocketserviceandclient.second_cs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.desaco.localnetsocketserviceandclient.R;

public class SecondMainActivity extends FragmentActivity {
    //com.desaco.localnetsocketserviceandclient.second_cs.SecondMainActivity

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_cs);

        //滑动页面有惊喜
        pager = findViewById(R.id.pager);
        Fragment fragment1, fragment2;
//        fragment1 = new ClientFragment();//客户端在前面
//        fragment2 = new ServerFragment();

        fragment2 = new ClientFragment();//服务器端在前面
        fragment1 = new ServerFragment();
        pager.setAdapter(new MyAdapter(getSupportFragmentManager(), new Fragment[]{fragment1, fragment2}));
    }

}
