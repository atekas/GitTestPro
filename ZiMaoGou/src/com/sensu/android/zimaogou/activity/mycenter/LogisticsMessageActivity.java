package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * 物流信息
 * Created by qi.yang on 2016/1/16.
 */
public class LogisticsMessageActivity extends BaseActivity{
    ListView mLogisticsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_message_activity);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView(){
        mLogisticsListView = (ListView) findViewById(R.id.lv_messages);
        mLogisticsListView.setAdapter(new LogisticsAdapter());
    }

    class LogisticsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(LogisticsMessageActivity.this).inflate(R.layout.logistics_message_item_list,null);
            return view;
        }
    }
}
