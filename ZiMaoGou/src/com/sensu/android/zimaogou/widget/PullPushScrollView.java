package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
import com.sensu.android.zimaogou.adapter.ViewPagerAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.StringUtils;
import com.sensu.android.zimaogou.view.PhotoView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  商品详情头部
 */
public class PullPushScrollView extends ScrollView implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ProductDetailsResponse.ProductDetailData mProductDetailData;
    private ViewPagerAdapter mViewPagerAdapter;
    //导航点
    private View[] mViews;
    private List<PhotoView> mPhotoViewList = new ArrayList<PhotoView>();
    private ViewPager mViewPager;

    public PullPushScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private LayoutInflater mLayoutInflater;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.is_collect).setOnClickListener(this);
        findViewById(R.id.video_icon).setOnClickListener(this);
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse, ViewPager viewPager
            , ViewPagerAdapter viewPagerAdapter, List<PhotoView> photoViewList) {
        mViewPager = viewPager;
        mViewPagerAdapter = viewPagerAdapter;
        mPhotoViewList = photoViewList;
        mPhotoViewList.clear();
        ProductDetailsResponse.ProductDetailData productDetailData = productDetailsResponse.data;
        mProductDetailData = productDetailData;
        ((TextView) findViewById(R.id.product_name)).setText(productDetailData.name);

        if (productDetailData.is_7d_return.equals("1")) {
            ((TextView) findViewById(R.id.is_7day)).setText("7天无忧退货");
        } else {
            ((TextView) findViewById(R.id.is_7day)).setText("不支持7天无忧退货");
        }

        ImageUtils.displayImage(productDetailData.country.icon, ((ImageView) findViewById(R.id.country_icon)));
        ((TextView) findViewById(R.id.address)).setText(productDetailData.country.name + " " + productDetailData.deliver_address + "发货");
        ((TextView) findViewById(R.id.show_rate)).setText("本商品使用税率" + productDetailData.rate + ",订单关税≤50免征");

        if (productDetailData.price_interval.min_price.equals(productDetailData.price_interval.max_price)) {
            ((TextView) findViewById(R.id.now_price)).setText(StringUtils.deleteZero(productDetailData.price_interval.min_price));
        } else {
            ((TextView) findViewById(R.id.now_price)).setText(StringUtils.deleteZero(productDetailData.price_interval.min_price) + "-" +
                    StringUtils.deleteZero(productDetailData.price_interval.max_price));
        }

        if (productDetailData.price_interval.min_price_market.equals(productDetailData.price_interval.max_price_market)) {
            ((TextView) findViewById(R.id.price_market)).setText("¥" + StringUtils.deleteZero(productDetailData.price_interval.min_price_market));
        } else {
            ((TextView) findViewById(R.id.price_market)).setText("¥" + StringUtils.deleteZero(productDetailData.price_interval.min_price_market)
             + "-" + StringUtils.deleteZero(productDetailData.price_interval.max_price_market));
        }

        com.sensu.android.zimaogou.utils.TextUtils.addLineCenter(((TextView) findViewById(R.id.price_market)));
        if (TextUtils.isEmpty(productDetailData.sale_title)) {
            findViewById(R.id.sale_title).setVisibility(GONE);
        } else {
            findViewById(R.id.sale_title).setVisibility(VISIBLE);
            ((TextView) findViewById(R.id.sale_title)).setText(productDetailData.sale_title);
        }
        if (productDetailData.favorite_id.equals("0")) {
            findViewById(R.id.is_collect).setSelected(false);
        } else {
            findViewById(R.id.is_collect).setSelected(true);
        }

        int count = productDetailData.media.image.size();
        for (int i = 0; i < count; i++) {
            PhotoView photoView = (PhotoView) mLayoutInflater.inflate(R.layout.photo_view_pager_item, null);
            photoView.setPhotoData(productDetailData.media.image.get(i), false, "");
            mPhotoViewList.add(photoView);
        }

        //type为2时表示有视频, viewPager中数据位image.size +１
        if (productDetailData.media.type.equals("12")) {
            findViewById(R.id.video_icon).setVisibility(VISIBLE);
            mViews = new View[count + 1];
            PhotoView photoView = (PhotoView) mLayoutInflater.inflate(R.layout.photo_view_pager_item, null);
            photoView.setPhotoData(productDetailData.media.image.get(0), true, productDetailData.media.video);
            mPhotoViewList.add(photoView);
        } else if (productDetailData.media.type.equals("1")) {
            findViewById(R.id.video_icon).setVisibility(GONE);
            mViews = new View[count];
        }

        mViewPagerAdapter.setData(mPhotoViewList);
        mViewPagerAdapter.setShowPageCount(mPhotoViewList.size());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_collect:
                if (mProductDetailData.favorite_id.equals("0")) {
                    addGoodsToCollect();
                } else {
                    deleteGoodsCollect();
                }
                break;
            case R.id.video_icon:
                mViewPager.setCurrentItem(mPhotoViewList.size());
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (mProductDetailData.media.type.equals("12")) {
            if (i == mPhotoViewList.size() - 1) {
                findViewById(R.id.video_icon).setVisibility(GONE);
                PhotoView photoView = mPhotoViewList.get(i);
                photoView.playVideo();
            } else {
                findViewById(R.id.video_icon).setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void addGoodsToCollect() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(getContext()).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("gid", mProductDetailData.id);
        requestParams.put("uid", userInfo.getUid());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sGoodsCollect, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                PromptUtils.showToast("收藏成功");
                findViewById(R.id.is_collect).setSelected(true);
                mProductDetailData.favorite_id = "1";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void deleteGoodsCollect() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(getContext()).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", mProductDetailData.favorite_id);
        requestParams.put("uid", userInfo.getUid());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sGoodsCollect + "/" + mProductDetailData.id + "/delete", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                PromptUtils.showToast("取消收藏成功");
                findViewById(R.id.is_collect).setSelected(false);
                mProductDetailData.favorite_id = "0";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
