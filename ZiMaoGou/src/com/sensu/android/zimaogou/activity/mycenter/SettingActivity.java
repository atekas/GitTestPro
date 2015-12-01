package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.MainActivity;


/**
 * Created by qi.yang on 2015/11/26.
 */
public class SettingActivity extends BaseActivity {
    ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    /**
     *
     * 修改密码
     *
     */
    public void UpdatePassClick(View v){
        startActivity(new Intent(this,UpdatePasswordActivity.class));
    }
    /**
     *
     * 清除缓存
     *
     */
    public void CacheClick(View v){

    }
    /**
     *
     * 用户协议
     *
     */
    public void ServiceClick(View v){
        startActivity(new Intent(this,WebViewActivity.class).putExtra("title","用户协议"));
    }
    /**
     *
     * 用户去评分
     *
     */
    public void RatingClick(View v){

    }
    /**
     *
     * 关于我们
     *
     */
    public void AboutUsClick(View v){
        startActivity(new Intent(this,WebViewActivity.class).putExtra("title","关于自贸购"));
    }
    /**
     *
     * 退出账号
     *
     */
    public void LoginOutClick(View v){
        LoginOutDialog();
    }

    Dialog mLoginOutDialog;
    private void LoginOutDialog(){
        mLoginOutDialog = new Dialog(this,R.style.dialog);
        mLoginOutDialog.setCancelable(true);
        mLoginOutDialog.setContentView(R.layout.loginout_dialog);

        Button bt_sure = (Button) mLoginOutDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mLoginOutDialog.findViewById(R.id.bt_cancel);

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                mLoginOutDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginOutDialog.dismiss();
            }
        });
        mLoginOutDialog.show();
    }
}
