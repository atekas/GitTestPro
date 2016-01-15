package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.Mode.RefundReasonMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2016/1/14.
 */
public class OnlyListActivity extends BaseActivity {

    ArrayList<RefundReasonMode> listData = new ArrayList<RefundReasonMode>();
    ListView listView;
    RefundReasonMode refundReasonMode  = new RefundReasonMode();
    String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.only_list_activity);
        if(getIntent().getExtras() != null){
            listData = (ArrayList<RefundReasonMode>) getIntent().getExtras().get("list");
            type = getIntent().getExtras().getString("type","");
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(TextUtils.isEmpty(type)) {
            ((TextView) findViewById(R.id.tv_title)).setText("退款原因");
        }else{
            ((TextView) findViewById(R.id.tv_title)).setText("选择物流公司");
        }
        initView();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.listView);
        listView.setDivider(null);
        listView.setAdapter(new AddressAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                refundReasonMode.setReason(listData.get(i).getReason());
                refundReasonMode.setId(listData.get(i).getId());
                Intent intent = new Intent(OnlyListActivity.this,RefundOrderActivity.class);
                intent.putExtra("data",refundReasonMode);
                setResult(1001, intent);
                finish();
            }
        });
    }

    private class AddressAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return listData.size();
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
            ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(OnlyListActivity.this).inflate(R.layout.only_list_item,null);
                holder.tv_address = (TextView) view.findViewById(R.id.tv_tip);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_address.setText(listData.get(i).getReason());
            return view;
        }
    }
    private class ViewHolder{
        TextView tv_address;
    }

}
