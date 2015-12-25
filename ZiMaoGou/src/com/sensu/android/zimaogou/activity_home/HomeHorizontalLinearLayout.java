package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.Mode.StoreMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CommendProductResponse;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
import com.sensu.android.zimaogou.activity.GoodShopActivity;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;
import com.sensu.android.zimaogou.activity.SpellOrderActivity;
import com.sensu.android.zimaogou.activity.SpellOrderDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsAdapter;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/12.
 */
public class HomeHorizontalLinearLayout extends LinearLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    HorizontalListView mhListView;
    HorizontalListViewAdapter mhListViewAdapter;
    StoreHorizontalListViewAdapter mStoreListAdapter;
    TextView mTitle;
    int mType = 1;//哪个部分调用 1，每日推荐 2，拼单特价，3.发现好店铺
    ArrayList<ProductMode> pros = new ArrayList<ProductMode>();//产品数组
    ArrayList<StoreMode> stors = new ArrayList<StoreMode>();//店铺数组
    CommendProductResponse mCommendProduct = new CommendProductResponse();
    public HomeHorizontalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mhListView = (HorizontalListView) findViewById(R.id.lv_recommend);
        mTitle = (TextView) findViewById(R.id.tv_title);

        findViewById(R.id.more).setOnClickListener(this);

        mhListView.setOnItemClickListener(this);
    }

    public void setData(int Type){
        mType = Type;

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mhListView.getLayoutParams();
        int width = DisplayUtils.getDisplayWidth();//获得屏幕宽度

        ProductMode mode1 = new ProductMode();
        mode1.setTestImg(R.drawable.pt_1);
        mode1.setTestTitle("抹茶手工皂精油");
        pros.add(mode1);
        ProductMode mode2 = new ProductMode();
        mode2.setTestImg(R.drawable.pt_2);
        mode2.setTestTitle("抹茶手工皂精油");
        pros.add(mode2);
        ProductMode mode3 = new ProductMode();
        mode3.setTestImg(R.drawable.pt_1);
        mode3.setTestTitle("抹茶手工皂精油");
        pros.add(mode3);
        ProductMode mode4 = new ProductMode();
        mode4.setTestImg(R.drawable.pt_2);
        mode4.setTestTitle("抹茶手工皂精油");
        pros.add(mode4);
        ProductMode mode5 = new ProductMode();
        mode5.setTestImg(R.drawable.pt_1);
        mode5.setTestTitle("抹茶手工皂精油");
        pros.add(mode5);

        StoreMode store1 = new StoreMode();
        store1.setStoreName("百年印花老店");
        store1.setTestStoreImage(R.drawable.store1);
        store1.setTestCountryImage(R.drawable.index_14);
        store1.setTestCountryName("法国巴黎");
        stors.add(store1);
        StoreMode store2 = new StoreMode();
        store2.setStoreName("百年印花老店");
        store2.setTestStoreImage(R.drawable.store2);
        store2.setTestCountryImage(R.drawable.index_16);
        store2.setTestCountryName("日本北海道");
        stors.add(store2);
        StoreMode store3 = new StoreMode();
        store3.setStoreName("百年印花老店");
        store3.setTestStoreImage(R.drawable.store3);
        store3.setTestCountryImage(R.drawable.index_16);
        store3.setTestCountryName("韩国明洞");
        stors.add(store3);
        switch (Type){
            case 1://每日推荐
                mTitle.setText(getResources().getText(R.string.daily_recommend));
                findViewById(R.id.title_layout).setVisibility(GONE);
                //设置水平listview的高度
                linearParams.height = width/3 + DisplayUtils.dp2px(48);
                mhListView.setLayoutParams(linearParams);
//                mhListView.setPadding(0,0,DisplayUtils.dp2px(6),0);
                getCommendProducts();
                break;
            case 2://拼单特价
                mTitle.setText(getResources().getText(R.string.group_special));
                linearParams.height = DisplayUtils.dp2px(235);
                mhListViewAdapter = new HorizontalListViewAdapter(getContext(),mCommendProduct.data,Type);
                mhListView.setAdapter(mhListViewAdapter);
                break;
            case 3://发现好店铺
                mTitle.setText(getResources().getText(R.string.find_shop));
                linearParams.height = width/3 + DisplayUtils.dp2px(28);
                mhListView.setLayoutParams(linearParams);
//                mhListView.setPadding(DisplayUtils.dp2px(6),0,0,0);
                mStoreListAdapter = new StoreHorizontalListViewAdapter(getContext(),stors);
                mhListView.setAdapter(mStoreListAdapter);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (mType) {
            case 1:
                PromptUtils.showToast("商品详情");
                getContext().startActivity(new Intent(getContext(), ProductDetailsActivity.class));
                break;
            case 2:
                PromptUtils.showToast("拼单详情");
                getContext().startActivity(new Intent(getContext(), SpellOrderDetailsActivity.class));
                break;
            case 3:
                PromptUtils.showToast("好店铺详情");
                getContext().startActivity(new Intent(getContext(), TourBuyDetailsActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more:
                if (mType == 1) {
                    //没有更多按钮
                } else if (mType == 2) {
                    getContext().startActivity(new Intent(getContext(), SpellOrderActivity.class));
                } else if (mType ==3) {
                    getContext().startActivity(new Intent(getContext(), GoodShopActivity.class));
                }
                break;
        }
    }
    private void getCommendProducts(){
        final RequestParams requestParams = new RequestParams();
        requestParams.put("tag","4");
        HttpUtil.get(IConstants.sGoodList,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("推荐好物返回：",response.toString());
                mCommendProduct = JSON.parseObject(response.toString(), CommendProductResponse.class);
                mhListViewAdapter = new HorizontalListViewAdapter(getContext(),mCommendProduct.data,mType);
                mhListView.setAdapter(mhListViewAdapter);
            }
        });
    }
}
