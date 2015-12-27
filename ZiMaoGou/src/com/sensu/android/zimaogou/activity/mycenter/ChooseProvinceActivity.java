package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;

import java.util.ArrayList;

/**
 * 选择省份
 * Created by qi.yang on 2015/12/27.
 */
public class ChooseProvinceActivity extends BaseActivity {
    ProvinceMode resultAddress ;
    AddressResponse addressResponse;
    private TextView tv_title;
    private ListView lv_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_choose_activity);
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
            addressResponse = (AddressResponse) getIntent().getExtras().get("address");
        }
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择省份");

        lv_address = (ListView) findViewById(R.id.lv_address);
        lv_address.setDivider(null);
        lv_address.setAdapter(new AddressAdapter(addressResponse.data));
        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resultAddress =  BaseApplication.mChooseAddress;
                resultAddress = addressResponse.data.get(i);
                BaseApplication.mChooseAddress.setId(addressResponse.data.get(i).getId());
                BaseApplication.mChooseAddress.setName(addressResponse.data.get(i).getName());
                Intent intent = new Intent(ChooseProvinceActivity.this,ChooseCityActivity.class);
                intent.putExtra("address",addressResponse.data.get(i));
                startActivity(intent);
                finish();
            }
        });
    }

    private class AddressAdapter extends BaseAdapter{
        ArrayList<ProvinceMode> addresses = new ArrayList<ProvinceMode>();

        public AddressAdapter(ArrayList<ProvinceMode> addresses) {
            this.addresses = addresses;
        }

        @Override
        public int getCount() {
            return addresses.size();
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
                view = LayoutInflater.from(ChooseProvinceActivity.this).inflate(R.layout.address_choose_list_item,null);
                holder.tv_address = (TextView) view.findViewById(R.id.tv_addressName);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_address.setText(addresses.get(i).getName());
            return view;
        }
    }
    private class ViewHolder{
        TextView tv_address;
    }
}
