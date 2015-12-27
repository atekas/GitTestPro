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
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.Mode.CityMode;
import com.sensu.android.zimaogou.Mode.DistrictMode;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;

import java.util.ArrayList;

/**
 * 选择省份
 * Created by qi.yang on 2015/12/27.
 */
public class ChooseDistrictActivity extends BaseActivity {
    ProvinceMode resultAddress ;
    CityMode mCityMode;
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
            mCityMode = (CityMode) getIntent().getExtras().get("address");
        }
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择城市");

        lv_address = (ListView) findViewById(R.id.lv_address);
        lv_address.setDivider(null);
        lv_address.setAdapter(new AddressAdapter(mCityMode.data));
        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                resultAddress = BaseApplication.mChooseAddress;
                ArrayList<DistrictMode> cityModes = new ArrayList<DistrictMode>();
                cityModes.add(mCityMode.data.get(i));
                resultAddress.data.get(0).data = cityModes;
                finish();
            }
        });
    }

    private class AddressAdapter extends BaseAdapter{
        ArrayList<DistrictMode> addresses = new ArrayList<DistrictMode>();

        public AddressAdapter(ArrayList<DistrictMode> addresses) {
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
                view = LayoutInflater.from(ChooseDistrictActivity.this).inflate(R.layout.address_choose_list_item,null);
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
