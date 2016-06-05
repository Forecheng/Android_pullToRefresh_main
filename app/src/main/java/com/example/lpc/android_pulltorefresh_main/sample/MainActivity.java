package com.example.lpc.android_pulltorefresh_main.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

import com.example.lpc.android_pulltorefresh_main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * Created by lpc on 2016/6/2.
 */
public class MainActivity extends Activity implements RefreshListView.IRefreshMoreData{

    private RefreshListView listView;
    List<HashMap<String,Object>>  list;
    SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (RefreshListView)findViewById(R.id.refreshList);
        //数据源
        list = new ArrayList<>();
        for (int i = 0 ; i< 20 ; i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put("photo",R.drawable.ic_launcher);
            map.put("info","第" + (i+1) +"条消息");
            list.add(map);
        }

        mAdapter = new SimpleAdapter(this,list,R.layout.list_item,new String[]{"photo","info"},new int[]{R.id.imageView,R.id.infoShow});

        listView.setAdapter(mAdapter);

        listView.setRefreshData(this);
    }

    @Override
    public void onFresh() {
        Handler handler = new Handler() ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("photo",R.drawable.ic_launcher);
                map.put("info","更多的消息");
                list.add(0,map);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                listView.refreshFinish();
            }
        },2000);
    }
}
