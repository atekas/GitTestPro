package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * Created by qi.yang on 2015/11/26.
 */
public class MyInformationActivity extends BaseActivity {
    ImageView mHeadImageView;
    EditText mNicknameEditText;
    TextView mSexTextView,mPhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_infor_activity);
        initView();
    }

    private void initView(){
        mHeadImageView = (ImageView) findViewById(R.id.img_head);
        mNicknameEditText = (EditText) findViewById(R.id.et_nickname);
        mSexTextView = (TextView) findViewById(R.id.tv_sex);
        mPhoneTextView = (TextView) findViewById(R.id.tv_phone);
    }
    /**
     *
     * 头像
     *
     */
    public void HeadClick(View v){

    }
    /**
     *
     * 性别
     *
     */
    public void SexClick(View v){
        chooseDialog();
    }

    /**
     *
     * 选择对话框
     *
     */
    Dialog mSexChooseDialog;
    public void chooseDialog(){
        mSexChooseDialog = new Dialog(this, R.style.notParentDialog);
        mSexChooseDialog.setCancelable(true);
        mSexChooseDialog.setContentView(R.layout.sex_choose_dialog);
        TextView tv_cancel = (TextView) mSexChooseDialog.findViewById(R.id.tv_cancel);
        TextView tv_man = (TextView) mSexChooseDialog.findViewById(R.id.tv_man);
        TextView tv_woman = (TextView) mSexChooseDialog.findViewById(R.id.tv_woman);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexChooseDialog.dismiss();
            }
        });
        tv_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexTextView.setText("男");
                mSexChooseDialog.dismiss();
            }
        });
        tv_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexTextView.setText("女");
                mSexChooseDialog.dismiss();
            }
        });
        WindowManager m = this.getWindowManager();

        Window dialogWindow = mSexChooseDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mSexChooseDialog.show();
    }
}
