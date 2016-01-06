package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProductTypeModel;
import com.sensu.android.zimaogou.Mode.SelectProductModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.video.ProductShopCarActivity;
import com.sensu.android.zimaogou.adapter.ProductEvaluateAdapter;
import com.sensu.android.zimaogou.adapter.ProductSpecificationAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.view.ProductTypeLinearLayout;
import com.sensu.android.zimaogou.widget.ProductTypeListView;
import com.sensu.android.zimaogou.widget.PullPushScrollView;
import com.sensu.android.zimaogou.widget.ScrollViewContainer;
import com.umeng.socialize.bean.SHARE_MEDIA;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/20.
 * 商品详情
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener, ProductTypeLinearLayout.OnProductColorListener {

    public static final String PRODUCT_ID = "product_id";
    public static final String FROM_SOURCE = "from_source";

    private String mProductId;
    private String mSource;

    private ScrollViewContainer mScrollViewContainer;
    private int mProductCount = 1;

    private int offset = 0;// 偏移量
    private int currIndex = 0;// 对应不同的Tab
    private int bmpW;//Image的宽度
    private ImageView mCursorImageView;
    private TextView mProductDetailTextView, mProductSpecificationTextView, mProductCommentTextView;
    private ListView listView, mProductSpecificationListView;
    private WebView mProductWebView;

    private ProductSpecificationAdapter mProductSpecificationAdapter;
    private ProductEvaluateAdapter mProductEvaluateAdapter;

    private ProductDetailsResponse mProductDetailsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        initViews();
        InitImageView();
    }

    /**
     * 初始化cursor
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

    /**
     * cursor动画
     */

    private void startAnimation(int index) {
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
                if (currIndex == 2) {
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
                if (currIndex == 2) {
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
                if (currIndex == 1) {
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
        mProductId = getIntent().getStringExtra(PRODUCT_ID);
        mSource = getIntent().getStringExtra(FROM_SOURCE);
        if (mProductId != null) {
            getProductById(mProductId, mSource);
        }
        mScrollViewContainer = (ScrollViewContainer) findViewById(R.id.scroll_view_container);
        mProductDetailTextView = (TextView) findViewById(R.id.tv_productDetail);
        mProductSpecificationTextView = (TextView) findViewById(R.id.tv_productSpecification);
        mProductCommentTextView = (TextView) findViewById(R.id.tv_productComment);
        mCursorImageView = (ImageView) findViewById(R.id.cursor);

        findViewById(R.id.scroll_view_title).setVisibility(View.GONE);

        mProductDetailTextView.setTextColor(getResources().getColor(R.color.red));

        mProductWebView = (WebView) findViewById(R.id.productDetail_webView);
        mProductSpecificationListView = (ListView) findViewById(R.id.product_specification_list);
        mProductSpecificationAdapter = new ProductSpecificationAdapter(this);
        mProductSpecificationListView.setAdapter(mProductSpecificationAdapter);

        UiUtils.setListViewHeightBasedOnChilds(mProductSpecificationListView);
        mProductSpecificationListView.setVisibility(View.GONE);

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
        findViewById(R.id.add_to_buy_bag).setOnClickListener(this);
        findViewById(R.id.pay).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.product_evaluate_list);

        mProductDetailTextView.setOnClickListener(new MyOnClickListener(0));
        mProductSpecificationTextView.setOnClickListener(new MyOnClickListener(1));
        mProductCommentTextView.setOnClickListener(new MyOnClickListener(2));
    }

    UserInfo mUserInfo;

    @Override
    protected void onResume() {
        super.onResume();

        if (mCartNum != 0) {
            findViewById(R.id.cart_num).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.cart_num)).setText(mCartNum + "");
        } else {
            findViewById(R.id.cart_num).setVisibility(View.GONE);
        }

        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (mUserInfo != null) {
            getCart();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.shopping_bag:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(this, ProductShopCarActivity.class);
                startActivity(intent);
                break;
            case R.id.product_share:
                PromptUtils.showToast("分享");
                break;
            case R.id.cancel:
                mChooseDialog.dismiss();
                break;
            case R.id.sure:
                //todo 添加购物车
                if (TextUtils.isEmpty(getSpecId())) {
                    PromptUtils.showToast("请选择规格");
                    return;
                }

                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    String num = ((EditText) mChooseDialog.findViewById(R.id.product_num)).getText().toString();
                    addToCart(getSpecId(), num);
                }
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
            case R.id.add_to_buy_bag:
                ChooseTypeAndColorClick(findViewById(R.id.add_to_buy_bag));
                break;
            case R.id.pay:
                SelectProductModel selectProductModel = new SelectProductModel();
                Intent intent1 = new Intent(this, VerifyOrderActivity.class);
                intent1.putExtra(VerifyOrderActivity.PRODUCT_FOR_PAY, selectProductModel);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 选择型号和颜色
     */
    Dialog mChooseDialog;

    public void ChooseTypeAndColorClick(View v) {
        mChooseDialog = new Dialog(this, R.style.dialog);
        mChooseDialog.setCancelable(true);
        mChooseDialog.setContentView(R.layout.product_details_choose_dialog);
        WindowManager m = getWindowManager();
        Window dialogWindow = mChooseDialog.getWindow();

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) d.getWidth(); // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mChooseDialog.show();

        mChooseDialog.findViewById(R.id.cancel).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.sure).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.bt_subtract).setOnClickListener(this);
        mChooseDialog.findViewById(R.id.bt_add).setOnClickListener(this);
        ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + mProductDetailsResponse.data.price);

        ((ProductTypeLinearLayout) mChooseDialog.findViewById(R.id.product_type_layout)).setProductDetailsResponse(mProductDetailsResponse, this);
    }

    private String getSpecId() {
        List<ProductTypeModel> mColorList = null;
        List<ProductTypeModel> mSizeList = null;
        List<ProductTypeModel> mCapacityList = null;
        for (ProductDetailsResponse.SpecAttr specAttr : mProductDetailsResponse.data.spec_attr) {
            if (specAttr.en.equals("color")) {
                mColorList = ((ProductTypeLinearLayout) mChooseDialog.findViewById(R.id.product_type_layout)).getColor();
            } else if (specAttr.en.equals("size")) {
                mSizeList = ((ProductTypeLinearLayout) mChooseDialog.findViewById(R.id.product_type_layout)).getSize();
            } else if (specAttr.en.equals("capacity")) {
                mCapacityList = ((ProductTypeLinearLayout) mChooseDialog.findViewById(R.id.product_type_layout)).getCapacity();
            }
        }

        String color = "";
        String size = "";
        String capacity = "";
        if (mColorList != null) {
            for (ProductTypeModel productTypeModel : mColorList) {
                if (productTypeModel.getIsSelect()) {
                    color = productTypeModel.getTypeName();
                }
            }
        }

        if (mSizeList != null) {
            for (ProductTypeModel productTypeModel : mSizeList) {
                if (productTypeModel.getIsSelect()) {
                    size = productTypeModel.getTypeName();
                }
            }
        }

        if (mCapacityList != null) {
            for (ProductTypeModel productTypeModel : mCapacityList) {
                if (productTypeModel.getIsSelect()) {
                    capacity = productTypeModel.getTypeName();
                }
            }
        }

        String id = null;

        if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(capacity)) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (!TextUtils.isEmpty(color)) {
                    if (spec.color.equals(color)) {
                        for (ProductDetailsResponse.Spec spec1 : mProductDetailsResponse.data.spec) {
                            if (!TextUtils.isEmpty(capacity)) {
                                if (spec1.capacity.equals(capacity)) {
                                    id = spec1.id;
                                }
                            }
                        }
                    }
                }
            }
        } else if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(size)) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (!TextUtils.isEmpty(color)) {
                    if (spec.color.equals(color)) {
                        for (ProductDetailsResponse.Spec spec1 : mProductDetailsResponse.data.spec) {
                            if (!TextUtils.isEmpty(size)) {
                                if (spec1.size.equals(size)) {
                                    id = spec1.id;
                                }
                            }
                        }
                    }
                }
            }
        } else if (!TextUtils.isEmpty(capacity) && !TextUtils.isEmpty(size)) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (!TextUtils.isEmpty(capacity)) {
                    if (spec.capacity.equals(capacity)) {
                        for (ProductDetailsResponse.Spec spec1 : mProductDetailsResponse.data.spec) {
                            if (!TextUtils.isEmpty(size)) {
                                if (spec1.size.equals(size)) {
                                    id = spec1.id;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (mProductDetailsResponse.data.spec_attr.size() == 0) {
            id = mProductDetailsResponse.data.spec.get(0).id;
        } else if (mProductDetailsResponse.data.spec_attr.size() == 1) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (!TextUtils.isEmpty(color)) {
                    if (spec.color.equals(color)) {
                        id = spec.id;
                    }
                } else if (!TextUtils.isEmpty(size)) {
                    if (spec.size.equals(size)) {
                        id = spec.id;
                    }
                } else if (!TextUtils.isEmpty(capacity)) {
                    if (spec.capacity.equals(capacity)) {
                        id = spec.id;
                    }
                }
            }
        }
        return id;
    }

    private void getProductById(String productId, String source) {
        final RequestParams requestParams = new RequestParams();
        requestParams.put("id", productId);
        requestParams.put("source", source);
        HttpUtil.get(IConstants.sProduct_detail + productId, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取商品详情：", response.toString());
                ProductDetailsResponse productDetailsResponse = JSON.parseObject(response.toString(), ProductDetailsResponse.class);
                mProductDetailsResponse = productDetailsResponse;
                layoutUi();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("获取物品详情失败");
            }
        });
    }

    private void layoutUi() {
        ((PullPushScrollView) findViewById(R.id.product_detail_top)).setProductDetailsResponse(mProductDetailsResponse);
        mProductSpecificationAdapter.setProductDetailData(mProductDetailsResponse.data);
        UiUtils.setListViewHeightBasedOnChilds(mProductSpecificationListView);


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
        mProductWebView.loadUrl(mProductDetailsResponse.data.description);
        mProductWebView.setVisibility(View.VISIBLE);

        mProductEvaluateAdapter = new ProductEvaluateAdapter(ProductDetailsActivity.this, mProductDetailsResponse.data.comment);
        listView.setAdapter(mProductEvaluateAdapter);
        listView.setVisibility(View.GONE);
        mProductCommentTextView.setText("评价" + mProductDetailsResponse.data.comment.size());
    }

    private void addToCart(String id, final String num) {

        RequestParams requestParams = new RequestParams();

        requestParams.put("uid", mUserInfo.getUid());
        requestParams.put("source", mSource);
        requestParams.put("num", num);
        requestParams.put("spec_id", id);
        HttpUtil.postWithSign(mUserInfo.getToken(), IConstants.sCart, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mChooseDialog.dismiss();
                findViewById(R.id.cart_num).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.cart_num)).setText(mCartNum + Integer.parseInt(num) + "");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void getProductColor(String color) {
        if (!TextUtils.isEmpty(color)) {
            ImageView imageView = (ImageView) mChooseDialog.findViewById(R.id.img_pro);
            String url = null;
            for (ProductDetailsResponse.ColorImage colorImage : mProductDetailsResponse.data.color_image) {
                if (color.equals(colorImage.name)) {
                    url = colorImage.image;
                }
            }
            if (!TextUtils.isEmpty(url)) {
                ImageUtils.displayImage(url, imageView);
            }
        }
    }

    int mCartNum;

    private void getCart() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sCart, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //todo 接口通,等数据
                CartDataResponse cartDataResponse = JSON.parseObject(response.toString(), CartDataResponse.class);

                mCartNum = 0;

                for (CartDataResponse.CartDataGroup cartDataGroup : cartDataResponse.data) {
                    for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
                        mCartNum += Integer.parseInt(cartDataChild.num);
                    }
                }

                if (mCartNum != 0) {
                    findViewById(R.id.cart_num).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.cart_num)).setText(mCartNum + "");
                } else {
                    findViewById(R.id.cart_num).setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
