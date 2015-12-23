package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
import com.sensu.android.zimaogou.adapter.ViewPagerAdapter;
import com.sensu.android.zimaogou.view.PhotoView;

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.is_collect).setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        ProductDetailsResponse.ProductDetailData productDetailData = productDetailsResponse.data;
        mProductDetailData = productDetailData;
        ((TextView) findViewById(R.id.product_name)).setText(productDetailData.name);
        ((TextView) findViewById(R.id.now_price)).setText(productDetailData.price);
        ((TextView) findViewById(R.id.price_market)).setText(productDetailData.price_market);
        if (TextUtils.isEmpty(productDetailData.sale_title)) {
            findViewById(R.id.sale_title).setVisibility(GONE);
        } else {
            findViewById(R.id.sale_title).setVisibility(VISIBLE);
            ((TextView) findViewById(R.id.sale_title)).setText(productDetailData.sale_title);
        }
        if (productDetailData.is_favorite.equals("0")) {
            findViewById(R.id.is_collect).setSelected(false);
        } else if (productDetailData.is_favorite.equals("1")) {
            findViewById(R.id.is_collect).setSelected(true);
        }

        int count = productDetailData.media.image.size();
        for (int i = 0; i < count; i++) {
            PhotoView photoView = (PhotoView) LayoutInflater.from(getContext()).inflate(R.layout.photo_view_pager_item, null);
            photoView.setPhotoData(productDetailData.media.image.get(i), false, "");
            mPhotoViewList.add(photoView);
        }

        //type为2时表示有视频, viewPager中数据位image.size +１
        if (productDetailData.media.type.equals("2")) {
            mViews = new View[count + 1];
            PhotoView photoView = (PhotoView) LayoutInflater.from(getContext()).inflate(R.layout.photo_view_pager_item, null);
            photoView.setPhotoData(productDetailData.media.cover, true, productDetailData.media.video);
            mPhotoViewList.add(photoView);
        } else if (productDetailData.media.type.equals("1")) {
            mViews = new View[count];
        }

        mViewPagerAdapter = new ViewPagerAdapter(mPhotoViewList);
        mViewPagerAdapter.setShowPageCount(mPhotoViewList.size());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_collect:
                if (mProductDetailData.is_favorite.equals("0")) {
                    //TODO 进行收藏
                } else if (mProductDetailData.is_favorite.equals("1")) {
                    //todo 取消收藏
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
