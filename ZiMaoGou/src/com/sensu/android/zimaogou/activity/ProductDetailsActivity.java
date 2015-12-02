package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.video.ProductShopCarActivity;
import com.sensu.android.zimaogou.adapter.ProductEvaluateAdapter;
import com.sensu.android.zimaogou.external.umeng.share.UmengShare;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ScrollViewContainer;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ScrollViewContainer mScrollViewContainer;
    private int mProductCount = 1;
    private UmengShare mUmengShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        initViews();
    }

    private void initViews() {
        mUmengShare = UmengShare.getInstance(this);
        mScrollViewContainer = (ScrollViewContainer) findViewById(R.id.scroll_view_container);
        mScrollViewContainer.setOnSlideFinish(new ScrollViewContainer.OnSlideFinish() {
            @Override
            public void slideFinish(boolean isBottomView) {
                if (isBottomView) {
                    findViewById(R.id.scroll_view_title).setVisibility(View.VISIBLE);

                } else {
                    findViewById(R.id.scroll_view_title).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.shopping_bag).setOnClickListener(this);
        findViewById(R.id.product_share).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.product_evaluate_list);
        listView.setAdapter(new ProductEvaluateAdapter(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.shopping_bag:
                Intent intent = new Intent(this, ProductShopCarActivity.class);
//                intent.putExtra(MainActivity.SELECT_TAB, MainActivity.SHOPPING_BAG_FM_CODE);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.product_share:
                PromptUtils.showToast("分享");
                mUmengShare.configPlatforms();
                mUmengShare.setShareContent();
                mUmengShare.mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
                mUmengShare.mController.openShare(this, false);
                break;
            case R.id.cancel:
                mChooseDialog.dismiss();
                break;
            case R.id.sure:
                mChooseDialog.dismiss();
                break;
            case R.id.type_1:
                mChooseDialog.findViewById(R.id.type_1).setSelected(true);
                mChooseDialog.findViewById(R.id.type_2).setSelected(false);
                break;
            case R.id.type_2:
                mChooseDialog.findViewById(R.id.type_1).setSelected(false);
                mChooseDialog.findViewById(R.id.type_2).setSelected(true);
                break;
            case R.id.bt_subtract:
                if (mProductCount > 1) {
                    mProductCount--;
                    ((EditText) mChooseDialog.findViewById(R.id.product_num)).setText(mProductCount + "");
                }
                break;
            case R.id.bt_add:
                mProductCount++;
                ((EditText) mChooseDialog.findViewById(R.id.product_num)).setText(mProductCount + "");
                break;
        }
    }
    /**
     * 选择型号和颜色
     */
    Dialog mChooseDialog;
    public void ChooseTypeAndColorClick(View v){
        mChooseDialog = new Dialog(this,R.style.dialog);
        mChooseDialog.setCancelable(true);
        mChooseDialog.setContentView(R.layout.product_details_choose_dialog);
        WindowManager m = getWindowManager();
        Window dialogWindow = mChooseDialog.getWindow();

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mChooseDialog.show();

        mChooseDialog.findViewById(R.id.cancel).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.type_1).setSelected(true);
        mChooseDialog.findViewById(R.id.type_2).setSelected(false);
        mChooseDialog.findViewById(R.id.sure).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.type_1).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.type_2).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.bt_subtract).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.bt_add).setOnClickListener(this);
    }

}
