package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.ReceiverListAdapter;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverAddressActivity extends BaseActivity {
    ImageView mBackImageView;
    ListView mReceiverAddressListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_activity);
        initView();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mReceiverAddressListView = (ListView) findViewById(R.id.lv_receiverAddress);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mReceiverAddressListView.setDivider(null);
        mReceiverAddressListView.setAdapter(new ReceiverListAdapter(this));
    }
}
