package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ScrollViewContainer;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ScrollViewContainer mScrollViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        initViews();
    }

    private void initViews() {
        mScrollViewContainer = (ScrollViewContainer) findViewById(R.id.scroll_view_container);
        mScrollViewContainer.setOnSlideFinish(new ScrollViewContainer.OnSlideFinish() {
            @Override
            public void slideFinish(boolean isBottomView) {
                if (isBottomView) {
                    PromptUtils.showToast("显示头");
                    findViewById(R.id.scroll_view_title).setVisibility(View.VISIBLE);

                } else {
                    PromptUtils.showToast("隐藏头");
                    findViewById(R.id.scroll_view_title).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
    /**
     *
     *
     * 选择型号和颜色
     *
     */
    Dialog mChooseDialog;
    public void ChooseTypeAndColorClick(View v){
        mChooseDialog = new Dialog(this,R.style.dialog);
        mChooseDialog.setCancelable(true);
        mChooseDialog.setContentView(R.layout.product_details_choose_dialog);

        WindowManager m = getWindowManager();

        Window dialogWindow = mChooseDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mChooseDialog.show();
    }

}
