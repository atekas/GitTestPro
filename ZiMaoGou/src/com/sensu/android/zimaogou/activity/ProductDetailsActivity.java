package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.*;
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
import com.sensu.android.zimaogou.adapter.ViewPagerAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.view.PhotoView;
import com.sensu.android.zimaogou.view.ProductTypeLinearLayout;
import com.sensu.android.zimaogou.widget.PullPushScrollView;
import com.sensu.android.zimaogou.widget.ScrollViewContainer;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/20.
 * 商品详情
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener, ProductTypeLinearLayout.OnProductChangedListener {

    public static final String PRODUCT_ID = "product_id";
    public static final String FROM_SOURCE = "from_source";
    public static final String THEME_ID = "theme_id";

    private String mProductId;
    private String mSource;
    private String mThemeId;

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

    private List<PhotoView> mPhotoViewList = new ArrayList<PhotoView>();
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private PullPushScrollView mPullPushScrollView;

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
                findViewById(R.id.no_review).setVisibility(View.GONE);
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
                findViewById(R.id.no_review).setVisibility(View.GONE);
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

                if (listView.getCount() == 0) {
                    findViewById(R.id.no_review).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.no_review).setVisibility(View.GONE);
                }

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

        mPullPushScrollView = (PullPushScrollView) findViewById(R.id.product_detail_top);
        mViewPager = (ViewPager) mPullPushScrollView.findViewById(R.id.viewpager);

        PhotoView photoView = (PhotoView) LayoutInflater.from(this).inflate(R.layout.photo_view_pager_item, null);
        photoView.setPhotoData("", false, "");
        photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPhotoViewList.add(photoView);
        mViewPagerAdapter = new ViewPagerAdapter(mPhotoViewList);
        mViewPagerAdapter.setShowPageCount(10);
        mViewPager.setAdapter(mViewPagerAdapter);

        mPullPushScrollView.findViewById(R.id.choose_color_size).setOnClickListener(this);

        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        mProductId = getIntent().getStringExtra(PRODUCT_ID);
        mSource = getIntent().getStringExtra(FROM_SOURCE);
        mThemeId = getIntent().getStringExtra(THEME_ID);
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
        findViewById(R.id.online_service).setOnClickListener(this);

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

    private String mIsBuyDirectly = "0";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.choose_color_size:
                if (mProductDetailsResponse != null) {
                    if (mProductDetailsResponse.data.state.equals("1") && mSaveNum > 0) {
                        ChooseTypeAndColorClick();
                        if (mChooseDialog != null) {
                            if (mProductDetailsResponse.data.spec.size() == 1) {
                                mChooseDialog.findViewById(R.id.dialog_line).setVisibility(View.GONE);
                            } else {
                                mChooseDialog.findViewById(R.id.dialog_line).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                break;
            case R.id.shopping_bag:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(this, ProductShopCarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.product_share:
                if (mProductDetailsResponse != null) {
                    shareDialog();
                }
                break;
            case R.id.cancel:
                mChooseDialog.dismiss();
                break;
            case R.id.sure:
                //todo 添加购物车
                if (TextUtils.isEmpty(mSpecId)) {
                    PromptUtils.showToast("请选择规格");
                    return;
                }

                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    String num = ((EditText) mChooseDialog.findViewById(R.id.product_num)).getText().toString();
                    if (mIsBuyDirectly.equals("0")) {
                        addToCart(mSpecId, num);
                    } else if (mIsBuyDirectly.equals("1")) {
                        SelectProductModel selectProductModel = getSelectProduct(num);
                        if (Integer.parseInt(num) > 1 && selectProductModel.getTotalMoney() > 1000.00) {
                            PromptUtils.showToast("海关规定购买多件的总价不能超过¥1000,请您分多次购买");
                            return;
                        }
                        Intent intent1 = new Intent(this, VerifyOrderActivity.class);
                        intent1.putExtra(VerifyOrderActivity.PRODUCT_FOR_PAY, selectProductModel);
                        startActivity(intent1);
                    }
                }
                break;
            case R.id.bt_subtract:
                if (mProductCount > 1) {
                    mProductCount--;
                    ((EditText) mChooseDialog.findViewById(R.id.product_num)).setText(mProductCount + "");
                }
                break;
            case R.id.bt_add:
                if (mProductCount < mCurrentNum) {
                    mProductCount++;
                    ((EditText) mChooseDialog.findViewById(R.id.product_num)).setText(mProductCount + "");
                }
                break;
            case R.id.add_to_buy_bag:
                if (mProductDetailsResponse.data.spec.size() == 1) {
                    if (mUserInfo == null) {
                        startActivity(new Intent(this, LoginActivity.class));
                    } else {
                        addToCart(mProductDetailsResponse.data.spec.get(0).id, "1");
                    }
                } else {
                    ChooseTypeAndColorClick();
                }
                break;
            case R.id.pay:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
//                    if (mSpecId == null) {
//                        if (mProductDetailsResponse.data.spec.size() == 1) {
//                            mSpecId = mProductDetailsResponse.data.spec.get(0).id;
//                            SelectProductModel selectProductModel = getSelectProduct("1");
//                            Intent intent1 = new Intent(this, VerifyOrderActivity.class);
//                            intent1.putExtra(VerifyOrderActivity.PRODUCT_FOR_PAY, selectProductModel);
//                            startActivity(intent1);
//                        } else {
//                            ChooseTypeAndColorClick();
//                            mIsBuyDirectly = "1";
//                        }
//                    } else {
//                        SelectProductModel selectProductModel = getSelectProduct("1");
//                        Intent intent1 = new Intent(this, VerifyOrderActivity.class);
//                        intent1.putExtra(VerifyOrderActivity.PRODUCT_FOR_PAY, selectProductModel);
//                        startActivity(intent1);
//                    }
                    mIsBuyDirectly = "1";
                    ChooseTypeAndColorClick();
                    if (mChooseDialog != null) {
                        if (mProductDetailsResponse.data.spec.size() == 1) {
                            mChooseDialog.findViewById(R.id.dialog_line).setVisibility(View.GONE);
                        } else {
                            mChooseDialog.findViewById(R.id.dialog_line).setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case R.id.online_service:
                startActivity(new Intent(this, OnlineServiceActivity.class));
                break;
        }
    }

    private SelectProductModel getSelectProduct(String num) {
        String source = "";
        String price = "";
        String spec1 = "";
        if (mSpecId != null) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (mSpecId.equals(spec.id)) {
                    source = spec.source;
                    price = spec.price;
                    if (!spec.color.equals("0")) {
                        spec1 = spec1 + spec.color;
                    }

                    if (!spec.capacity.equals("0")) {
                        spec1 = spec1 + spec.capacity;
                    }

                    if (!spec.size.equals("0")) {
                        spec1 = spec1 + spec.size;
                    }
                }
            }
        }
        SelectProductModel selectProductModel = new SelectProductModel();
        List<SelectProductModel.GoodsInfo> goodsInfoList = new ArrayList<SelectProductModel.GoodsInfo>();
        SelectProductModel.GoodsInfo goodsInfo = new SelectProductModel.GoodsInfo();

        goodsInfo.setGoodsId(mProductDetailsResponse.data.id);
        goodsInfo.setSpecId(mSpecId);
        goodsInfo.setNum(num);
        goodsInfo.setPrice(price);
        goodsInfo.setSource(source);
        goodsInfo.setName(mProductDetailsResponse.data.name);
        goodsInfo.setSpec(spec1);
        goodsInfo.setImage(mProductDetailsResponse.data.media.image.get(0));
        goodsInfo.setRate(mProductDetailsResponse.data.rate);
        goodsInfoList.add(goodsInfo);

        selectProductModel.setGoodsInfo(goodsInfoList);
        selectProductModel.setDeliverAddress(mProductDetailsResponse.data.deliver_address);
        selectProductModel.setIsUseCoupon(true);
        double totalMoney = Double.parseDouble(price) * Double.parseDouble(num);
        selectProductModel.setTotalMoney(totalMoney);

        return selectProductModel;
    }

    /**
     * 选择型号和颜色
     */
    Dialog mChooseDialog;

    public void ChooseTypeAndColorClick() {
        mChooseDialog = new Dialog(this, R.style.dialog);
        mChooseDialog.setCancelable(true);
        mChooseDialog.setContentView(R.layout.product_details_choose_dialog);
        LinearLayout ll_top = (LinearLayout) mChooseDialog.findViewById(R.id.ll_top);
        ll_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChooseDialog.dismiss();
            }
        });
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

        ((ProductTypeLinearLayout) mChooseDialog.findViewById(R.id.product_type_layout)).setProductDetailsResponse(mProductDetailsResponse, this);
    }

    private void getProductById(String productId, String source) {
        showLoading();
        final RequestParams requestParams = new RequestParams();
        requestParams.put("id", productId);
        requestParams.put("source", source);
        requestParams.put("uid", mUserInfo == null ? 0 : mUserInfo.getUid());
        requestParams.put("theme_id", mThemeId);

        HttpUtil.get(IConstants.sProduct_detail + productId, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取商品详情：", response.toString());
                ProductDetailsResponse productDetailsResponse = JSON.parseObject(response.toString(), ProductDetailsResponse.class);
                mProductDetailsResponse = productDetailsResponse;
                layoutUi();
                cancelLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("获取物品详情失败");
                cancelLoading();
            }
        });
    }

    int mSaveNum = 0;

    private void layoutUi() {
        mSaveNum = Integer.parseInt(mProductDetailsResponse.data.num);
        mSpecProductNum = mSaveNum;
        if (mProductDetailsResponse.data.state.equals("1")) {
            if (mSaveNum < 1) {
                findViewById(R.id.toast_num).setVisibility(View.VISIBLE);
                findViewById(R.id.add_to_buy_bag).setEnabled(false);
                findViewById(R.id.pay).setEnabled(false);
            } else {
                findViewById(R.id.toast_num).setVisibility(View.GONE);
                findViewById(R.id.add_to_buy_bag).setEnabled(true);
                findViewById(R.id.pay).setEnabled(true);
            }
        } else {
            findViewById(R.id.toast_num).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.toast_num)).setText("商品已下架，请看看其他商品吧");
            findViewById(R.id.add_to_buy_bag).setEnabled(false);
            findViewById(R.id.pay).setEnabled(false);
        }


        ((PullPushScrollView) findViewById(R.id.product_detail_top)).setProductDetailsResponse(mProductDetailsResponse
                , mViewPager, mViewPagerAdapter, mPhotoViewList);
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

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                int height = view.getContentHeight();
                height += view.getContentHeight();
                view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            }
        });
        mProductWebView.loadUrl(mProductDetailsResponse.data.description);
        mProductWebView.setVisibility(View.VISIBLE);
        mProductWebView.getContentHeight();


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
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                PromptUtils.showToast("商品已加入购物车");
                findViewById(R.id.cart_num).setVisibility(View.VISIBLE);
                mCartNum += Integer.parseInt(num);
                ((TextView) findViewById(R.id.cart_num)).setText(mCartNum + "");

                if (mChooseDialog != null) {
                    mChooseDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private String mSpecId = null;
    private int mSpecProductNum = 0;
    private int mCurrentNum = 0;

    @Override
    public void getProductColor() {
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

        if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(capacity)) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (spec.color.equals(color) && spec.capacity.equals(capacity)) {
                    mSpecId = spec.id;
                    ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + spec.num);
                    ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + spec.price);
                    mSpecProductNum = Integer.parseInt(spec.num);
                    break;
                }
            }
        } else if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(size)) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (spec.color.equals(color) && spec.size.equals(size)) {
                    mSpecId = spec.id;
                    ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + spec.num);
                    ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + spec.price);
                    mSpecProductNum = Integer.parseInt(spec.num);
                    break;
                }
            }
        } else if (!TextUtils.isEmpty(capacity) && !TextUtils.isEmpty(size)) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (spec.capacity.equals(capacity) && spec.size.equals(size)) {
                    mSpecId = spec.id;
                    ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + spec.num);
                    ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + spec.price);
                    mSpecProductNum = Integer.parseInt(spec.num);
                    break;
                }
            }
        }

        if (mProductDetailsResponse.data.spec_attr.size() == 0) {
            mSpecId = mProductDetailsResponse.data.spec.get(0).id;
            ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + mProductDetailsResponse.data.spec.get(0).num);
            ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + mProductDetailsResponse.data.spec.get(0).price);
            mSpecProductNum = Integer.parseInt(mProductDetailsResponse.data.spec.get(0).num);
        } else if (mProductDetailsResponse.data.spec_attr.size() == 1) {
            for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                if (!TextUtils.isEmpty(color)) {
                    if (spec.color.equals(color)) {
                        mSpecId = spec.id;
                        ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + spec.num);
                        ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + spec.price);
                        mSpecProductNum = Integer.parseInt(spec.num);
                    }
                } else if (!TextUtils.isEmpty(size)) {
                    if (spec.size.equals(size)) {
                        mSpecId = spec.id;
                        ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + spec.num);
                        ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + spec.price);
                        mSpecProductNum = Integer.parseInt(spec.num);
                    }
                } else if (!TextUtils.isEmpty(capacity)) {
                    if (spec.capacity.equals(capacity)) {
                        mSpecId = spec.id;
                        ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + spec.num);
                        ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + spec.price);
                        mSpecProductNum = Integer.parseInt(spec.num);
                    }
                }
            }
        }

        if (mCurrentNum != mSpecProductNum) {
            ((TextView) mChooseDialog.findViewById(R.id.product_num)).setText("1");
        }

        if (mSpecId == null) {
            ((TextView) mChooseDialog.findViewById(R.id.stock)).setText("库存 " + mSaveNum);
            if (mProductDetailsResponse.data.price_interval.min_price.equals(mProductDetailsResponse.data.price_interval.max_price)) {
                ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + StringUtils.deleteZero(mProductDetailsResponse.data.price_interval.min_price));
            } else {
                ((TextView) mChooseDialog.findViewById(R.id.tv_productPrice)).setText("¥" + StringUtils.deleteZero(mProductDetailsResponse.data.price_interval.min_price) + "-" +
                        StringUtils.deleteZero(mProductDetailsResponse.data.price_interval.max_price));
            }
        }

        ImageView imageView = (ImageView) mChooseDialog.findViewById(R.id.img_pro);
        String url = null;
        if (!TextUtils.isEmpty(color)) {
            for (ProductDetailsResponse.ColorImage colorImage : mProductDetailsResponse.data.color_image) {
                if (color.equals(colorImage.name)) {
                    url = colorImage.image;
                }
            }
            if (!TextUtils.isEmpty(url)) {
                ImageUtils.displayImage(url, imageView);
            } else {
                ImageUtils.displayImage(mProductDetailsResponse.data.media.image.get(0), imageView);
            }
        } else {
            url = mProductDetailsResponse.data.media.image.get(0);
            ImageUtils.displayImage(url, imageView);
        }

        mCurrentNum = mSpecProductNum;
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
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 分享
     */
    Dialog mShareDialog;

    private void shareDialog() {
        mShareDialog = new Dialog(this, R.style.dialog);
        mShareDialog.setCancelable(true);
        mShareDialog.setContentView(R.layout.share_dialog);
        LinearLayout ll_wx = (LinearLayout) mShareDialog.findViewById(R.id.ll_wx);
        LinearLayout ll_friends = (LinearLayout) mShareDialog.findViewById(R.id.ll_friends);
        LinearLayout ll_sina = (LinearLayout) mShareDialog.findViewById(R.id.ll_sina);
        ll_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(ProductDetailsActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withText(mProductDetailsResponse.data.sale_title)
                        .withTitle(mProductDetailsResponse.data.name)
                        .withTargetUrl("http://m.ftzgo365.com/v1/share/goods/" + mProductDetailsResponse.data.id)
                        .withMedia(new UMImage(ProductDetailsActivity.this, mProductDetailsResponse.data.media.image.get(0)))
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(ProductDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle(mProductDetailsResponse.data.name)
                        .withText(mProductDetailsResponse.data.sale_title)
                        .withTargetUrl("http://m.ftzgo365.com/v1/share/goods/" + mProductDetailsResponse.data.id)
                        .withMedia(new UMImage(ProductDetailsActivity.this, mProductDetailsResponse.data.media.image.get(0)))
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(ProductDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle(mProductDetailsResponse.data.name)
                        .withText(mProductDetailsResponse.data.sale_title)
                        .withTargetUrl("http://m.ftzgo365.com/v1/share/goods/" + mProductDetailsResponse.data.id)
                        .withMedia(new UMImage(ProductDetailsActivity.this, mProductDetailsResponse.data.media.image.get(0)))
                        .share();
                mShareDialog.dismiss();
            }
        });

        mShareDialog.show();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ProductDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ProductDetailsActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ProductDetailsActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }
        }
    };
}
