package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import org.w3c.dom.Text;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverAddressEditActivity extends BaseActivity {
    ImageView mBackImageView;
    TextView mTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_edit_activity);
        initView();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        if(getIntent().getExtras() != null){
            mTitleTextView.setText(getIntent().getExtras().getString("title"));
        }

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}
