package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.widget.RoundImageView;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class SpellOrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mUserHeadContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spell_order_details_activity);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.back).setOnClickListener(this);

        mUserHeadContainer = (LinearLayout) findViewById(R.id.user_photo_container);
        addUserPhoto();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void addUserPhoto() {
        for (int i = 0; i < 3; i++) {
            RoundImageView roundImageView = new RoundImageView(this);
            roundImageView.setPadding(5, 5, 5, 5);
            int size = DisplayUtils.dp2px(50);
            roundImageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            roundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            roundImageView.setImageResource(R.drawable.product1);
            mUserHeadContainer.addView(roundImageView);
        }
    }
    /**
     *
     * 输入口令
     *
     */
    Dialog mCommandInputDialog;
    public void CommandInput(View v){
        mCommandInputDialog = new Dialog(this,R.style.dialog);
        mCommandInputDialog.setCancelable(true);
        mCommandInputDialog.setContentView(R.layout.command_dialog);
        mCommandInputDialog.show();
    }
    /**
     *
     * 组团
     *
     */
    Dialog mCommandGroupDialog;
    public void CommandGroup(View v){
        mCommandGroupDialog = new Dialog(this,R.style.dialog);
        mCommandGroupDialog.setCancelable(true);
        mCommandGroupDialog.setContentView(R.layout.command_group_dialog);

        WindowManager m = getWindowManager();

        Window dialogWindow = mCommandGroupDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mCommandGroupDialog.show();
    }
}
