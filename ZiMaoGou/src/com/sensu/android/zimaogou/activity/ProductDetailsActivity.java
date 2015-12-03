package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.video.ProductShopCarActivity;
import com.sensu.android.zimaogou.adapter.ProductEvaluateAdapter;
import com.sensu.android.zimaogou.adapter.ProductSpecificationAdapter;
import com.sensu.android.zimaogou.external.umeng.share.UmengShare;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.UiUtils;
import com.sensu.android.zimaogou.widget.ScrollViewContainer;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ScrollViewContainer mScrollViewContainer;
    private int mProductCount = 1;
    private UmengShare mUmengShare;

    private int offset = 0;// 偏移量
    private int currIndex = 0;// 对应不同的Tab
    private int bmpW;//Image的宽度
    private ImageView mCursorImageView;
    private TextView mProductDetailTextView,mProductSpecificationTextView,mProductCommentTextView;
    private ListView listView,mProductSpecificationListView;
    private WebView mProductWebView;
    String URL = "http://www.sensu-sh.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        initViews();
        InitImageView();
    }

    /**
     * 初始化cursor
     *
     */
    private void InitImageView() {
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.spell_order_title)
                .getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
//        int screenW = DisplayUtils.dp2px(180);
        offset = (screenW / 3 - bmpW) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        mCursorImageView.setImageMatrix(matrix);
    }

    /**
     * 仿Tab键的事件监听器
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {

            startAnimation(index);
        }
    }
    /***
     *
     * cursor动画
     *
     */

    private void startAnimation(int index){
        if (index == currIndex) {
            return;
        }
        Animation animation = null;
        int one = offset * 2 + bmpW;
        int two = one * 2;
        switch (index) {
            case 0:
                if (currIndex == 1) {
                    /**
                     * 设置在X轴方向的动画。
                     * 第一个参数fromXDelta。Image的左上角为(0,0),该参数表示在X轴方向的动画开始位置距离Image左上角的距离
                     * 第二个参数toXDelta。该参数表示在X轴方向动画的结束位置距离Image左上角的距离。
                     * 第三和第四参数同上
                     * 位置的开始点是Image的原始位置，而不是setFillAfter(true)之后的位置
                     */
                    animation = new TranslateAnimation(one, 0, 0, 0);

                }
                if(currIndex == 2){
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                mProductWebView.setVisibility(View.VISIBLE);
                mProductSpecificationListView.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                mProductDetailTextView.setTextColor(getResources().getColor(R.color.red));
                mProductSpecificationTextView.setTextColor(getResources().getColor(R.color.black_444444));
                mProductCommentTextView.setTextColor(getResources().getColor(R.color.black_444444));
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, one, 0, 0);

                }
                if(currIndex == 2){
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                mProductWebView.setVisibility(View.GONE);
                mProductSpecificationListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                mProductDetailTextView.setTextColor(getResources().getColor(R.color.black_444444));
                mProductSpecificationTextView.setTextColor(getResources().getColor(R.color.red));
                mProductCommentTextView.setTextColor(getResources().getColor(R.color.black_444444));
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, two, 0, 0);

                }
                if(currIndex == 1){
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                mProductWebView.setVisibility(View.GONE);
                mProductSpecificationListView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                mProductDetailTextView.setTextColor(getResources().getColor(R.color.black_444444));
                mProductSpecificationTextView.setTextColor(getResources().getColor(R.color.black_444444));
                mProductCommentTextView.setTextColor(getResources().getColor(R.color.red));
                break;

        }
        currIndex = index;
        animation.setFillAfter(true);
        animation.setDuration(300);
        mCursorImageView.startAnimation(animation);
    }




    private void initViews() {
        mUmengShare = UmengShare.getInstance(this);
        mScrollViewContainer = (ScrollViewContainer) findViewById(R.id.scroll_view_container);
        mProductDetailTextView = (TextView) findViewById(R.id.tv_productDetail);
        mProductSpecificationTextView = (TextView) findViewById(R.id.tv_productSpecification);
        mProductCommentTextView = (TextView) findViewById(R.id.tv_productComment);
        mCursorImageView = (ImageView) findViewById(R.id.cursor);

        mProductDetailTextView.setTextColor(getResources().getColor(R.color.red));

        mProductWebView = (WebView) findViewById(R.id.productDetail_webView);
        mProductSpecificationListView = (ListView) findViewById(R.id.product_specification_list);

        mProductSpecificationListView.setAdapter(new ProductSpecificationAdapter(this));
        UiUtils.setListViewHeightBasedOnChilds(mProductSpecificationListView);
        mProductSpecificationListView.setVisibility(View.GONE);

        WebSettings webSettings = mProductWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setBuiltInZoomControls(true);
        mProductWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mProductWebView.loadUrl(URL);
        mProductWebView.setVisibility(View.VISIBLE);

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

        listView = (ListView) findViewById(R.id.product_evaluate_list);
        listView.setAdapter(new ProductEvaluateAdapter(this));
        listView.setVisibility(View.GONE);


        mProductDetailTextView.setOnClickListener(new MyOnClickListener(0));
        mProductSpecificationTextView.setOnClickListener(new MyOnClickListener(1));
        mProductCommentTextView.setOnClickListener(new MyOnClickListener(2));
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
