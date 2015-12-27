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
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
import com.sensu.android.zimaogou.activity.GoodShopActivity;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;
import com.sensu.android.zimaogou.activity.SpellOrderActivity;
import com.sensu.android.zimaogou.activity.SpellOrderDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDBaseHelper;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
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
    HomeGroupAdapter homeGroupAdapter;
    StoreHorizontalListViewAdapter mStoreListAdapter;
    TextView mTitle;
    int mType = 1;//哪个部分调用 1，每日推荐 2，拼单特价，3.发现好店铺
    ArrayList<ProductMode> pros = new ArrayList<ProductMode>();//产品数组
    ArrayList<StoreMode> stors = new ArrayList<StoreMode>();//店铺数组
    CommendProductResponse mCommendProduct = new CommendProductResponse();
    GroupBuyListResponse mGroupProduct = new GroupBuyListResponse();
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
                getGroupProducts();
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
                if (mCommendProduct == null) {
                    return;
                }
                CommendProductResponse.CommendProductMode commendProductMode = mCommendProduct.data.get(i);
                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, commendProductMode.id);
                intent.putExtra(ProductDetailsActivity.FROM_SOURCE, "0");
                getContext().startActivity(intent);
                break;
            case 2:
                PromptUtils.showToast("拼单详情");
                if (mGroupProduct == null) {
                    return;
                }
                GroupBuyListResponse.GroupBuyListData groupBuyListData = mGroupProduct.data.get(i);
                Intent intent1 = new Intent(getContext(), SpellOrderDetailsActivity.class);
                intent1.putExtra(SpellOrderActivity.TB_ID, groupBuyListData.id);
                getContext().startActivity(intent1);
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

    /**
     * 推荐好物
     */
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

    private void getGroupProducts(){

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", GDUserInfoHelper.getInstance(getContext()).getUserInfo() == null?"0":GDUserInfoHelper.getInstance(getContext()).getUserInfo().getUid());
        HttpUtil.get(IConstants.sTb_list,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("拼单特价返回：",response.toString());
                mGroupProduct = JSON.parseObject(response.toString(), GroupBuyListResponse.class);
                homeGroupAdapter = new HomeGroupAdapter(getContext(),mGroupProduct.data);
                mhListView.setAdapter(homeGroupAdapter);
            }


        });
    }
}
