package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ExpressInfoMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

import java.util.ArrayList;

/**
 * 物流信息
 * Created by qi.yang on 2016/1/16.
 */
public class LogisticsMessageActivity extends BaseActivity{
    ListView mLogisticsListView;
    ArrayList<ExpressInfoMode> expressInfoModes = new ArrayList<ExpressInfoMode>();
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
        if(getIntent().getExtras() != null){
            expressInfoModes = (ArrayList<ExpressInfoMode>) getIntent().getExtras().get("express_info");
        }
        mLogisticsListView = (ListView) findViewById(R.id.lv_messages);
        mLogisticsListView.setAdapter(new LogisticsAdapter());
    }

    class LogisticsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return expressInfoModes.size();
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
            ExpressViewHolder holder;
            if(view == null) {
                view = LayoutInflater.from(LogisticsMessageActivity.this).inflate(R.layout.logistics_message_item_list, null);
                holder = new ExpressViewHolder();
                holder.tv_context = (TextView) view.findViewById(R.id.tv_context);
                holder.tv_time = (TextView) view.findViewById(R.id.tv_time);

                view.setTag(holder);
            }else{
                holder = (ExpressViewHolder) view.getTag();
            }
            holder.tv_context.setText(expressInfoModes.get(i).getContext());
            holder.tv_time.setText(expressInfoModes.get(i).getTime());
            return view;
        }
    }

    class ExpressViewHolder{
        TextView tv_context,tv_time;
    }
}
