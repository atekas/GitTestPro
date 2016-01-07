package com.sensu.android.zimaogou.activity.mycenter;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductListResponse;
import com.sensu.android.zimaogou.ReqResponse.TravelResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.ProductsDetailsAdapter;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.widget.ExceptionLinearLayout;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 * Created by qi.yang on 2015/11/26.
 */
public class CollectActivity extends BaseActivity {

    List<View> mListView = new ArrayList<View>();
    private int offset = 0;// 偏移量
    private int currIndex = 0;// 对应不同的Tab
    private int bmpW;//Image的宽度
    private TextView mProductTitle,mTourTitle;
    private RelativeLayout mTitleRelativeLayout;
    private ImageView mCursorImageView,mBackImageView;
    private ViewPager mCollectViewPage;
    RefreshListView mTourBuyListView;
    ProductListResponse productListResponse = new ProductListResponse();
    TravelResponse travelResponse = new TravelResponse();
    UserInfo userInfo ;
    LinearLayout ll_NoDataShop;
    LinearLayout ll_noDataTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_activity);

        mProductTitle = (TextView) findViewById(R.id.product_title);
        mTourTitle = (TextView) findViewById(R.id.tour_title);
        mTitleRelativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
        mCursorImageView = (ImageView) findViewById(R.id.cursor);
        mCollectViewPage = (ViewPager) findViewById(R.id.viewPage_collect);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mProductTitle.setTextColor(getResources().getColor(R.color.red));
        mTourTitle.setTextColor(getResources().getColor(R.color.black_444444));
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        InitImageView();

        mProductTitle.setOnClickListener(new MyOnClickListener(0));
        mTourTitle.setOnClickListener(new MyOnClickListener(1));
        InitViewPager();

    }


    /**
     * 初始化cursor
     *
     */
    private void InitImageView() {

        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_cursor_small)
                .getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;
        int screenW = DisplayUtils.dp2px(180);
        offset = (screenW / 2 - bmpW) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        mCursorImageView.setImageMatrix(matrix);



    }
    private Handler mHandler = new Handler();
    /**
     *
     * 初始化ViewPager
     *
     */
    private void InitViewPager(){
        View productView = LayoutInflater.from(this).inflate(R.layout.collect_product_view,null);
        ll_NoDataShop = (LinearLayout) productView.findViewById(R.id.ll_noData);
        GridView mGridView = (GridView) productView.findViewById(R.id.product_list);
        ProductsDetailsAdapter mProductsDetailsAdapter = new ProductsDetailsAdapter(this);
        mGridView.setColumnWidth(DisplayUtils.getDisplayWidth() / 2);
        mGridView.setAdapter(mProductsDetailsAdapter);
        mListView.add(productView);
        View tourView = LayoutInflater.from(this).inflate(R.layout.collect_tour_buy_view,null);
        ll_noDataTour = (LinearLayout) tourView.findViewById(R.id.ll_noData);

        mTourBuyListView = (RefreshListView) tourView.findViewById(R.id.tour_list);
        TourBuyAdapter mTourBuyAdapter = new TourBuyAdapter(this);
        mTourBuyListView.setAdapter(mTourBuyAdapter);
        mTourBuyListView.setOnRefreshListener(mOnRefreshListener);
        mListView.add(tourView);
        mCollectViewPage.setAdapter(new MyPagerAdapter(mListView));
        mCollectViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                startAnimation(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setData(mProductsDetailsAdapter,mTourBuyAdapter);
    }
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTourBuyListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTourBuyListView.hideFooterView();
                }
            }, 2000);
        }
    };

    /**
     * ViewPager的Adapter
     *
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
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
                mProductTitle.setTextColor(getResources().getColor(R.color.red));
                mTourTitle.setTextColor(getResources().getColor(R.color.black_444444));

                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, one, 0, 0);

                }
                mTourTitle.setTextColor(getResources().getColor(R.color.red));
                mProductTitle.setTextColor(getResources().getColor(R.color.black_444444));
                break;


        }
        currIndex = index;
        animation.setFillAfter(true);
        animation.setDuration(300);
        mCollectViewPage.setCurrentItem(index);
        mCursorImageView.startAnimation(animation);
    }

    /**
     * 填充数据
     * @param productsDetailsAdapter
     * @param tourBuyAdapter
     */
    private void setData(final ProductsDetailsAdapter productsDetailsAdapter, final TourBuyAdapter tourBuyAdapter){
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("tab","1");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetCollect,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取我的收藏（商品）:",response.toString());
                productListResponse = JSON.parseObject(response.toString(),ProductListResponse.class);
                productsDetailsAdapter.setProductList(productListResponse);
                if(productListResponse.data.size() == 0){
                    ll_NoDataShop.setVisibility(View.VISIBLE);
                    View ExceptionView = View.inflate(CollectActivity.this, R.layout.exception_layout,null);
                    exceptionLinearLayout = (ExceptionLinearLayout) ExceptionView.findViewById(R.id.ll_exception);
                    exceptionLinearLayout.setException(IConstants.EXCEPTION_MY_GOODS_IS_NULL);
                    ll_NoDataShop.addView(ExceptionView);
                }else{
                    ll_NoDataShop.setVisibility(View.GONE);
                }
            }
        });
        RequestParams requestParams1 = new RequestParams();
        requestParams1.put("uid",userInfo.getUid());
        requestParams1.put("tab","2");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetCollect,requestParams1,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取我的收藏（游购）:",response.toString());
                travelResponse = JSON.parseObject(response.toString(),TravelResponse.class);
                tourBuyAdapter.flush(travelResponse.data);
                if(travelResponse.data.size() == 0){
                    ll_noDataTour.setVisibility(View.VISIBLE);
                    View ExceptionView = View.inflate(CollectActivity.this, R.layout.exception_layout,null);
                    exceptionLinearLayout = (ExceptionLinearLayout) ExceptionView.findViewById(R.id.ll_exception);
                    exceptionLinearLayout.setException(IConstants.EXCEPTION_TIPS_IS_NULL);
                    ll_noDataTour.addView(ExceptionView);
                }else{
                    ll_noDataTour.setVisibility(View.GONE);
                }
            }
        });

    }


}
