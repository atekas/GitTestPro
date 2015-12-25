package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDBaseHelper;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.handler.UpdateTimeHandler;
import org.w3c.dom.Text;

/**
 * Created by qi.yang on 2015/12/25.
 */
public class UpdateBindPhoneActivity extends BaseActivity {
    private TextView mPhoneTextView;
    private TextView mGetAuthCode;
    private UpdateTimeHandler mUpdateTimeHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_bindphone_activity);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView(){
        mPhoneTextView = (TextView) findViewById(R.id.tv_phone);
        mPhoneTextView.setText(GDUserInfoHelper.getInstance(this).getUserInfo().getMobile());
        mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
        mUpdateTimeHandler = new UpdateTimeHandler(this);
        mGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetAuthCode.setClickable(false);
                mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);
            }
        });
    }
}
