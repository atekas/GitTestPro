package com.sensu.android.zimaogou.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.VerifyOrderActivity;
import com.sensu.android.zimaogou.adapter.ShoppingBagAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by qi.yang on 2015/12/2.
 */
public class ProductShopCarActivity extends BaseActivity implements View.OnClickListener{

    private ListView mListView;
    private ShoppingBagAdapter mShoppingBagAdapter;
    private boolean mIsEdit;

    private UserInfo mUserInfo;

    private TextView mEditText;

    private CartDataResponse mCartDataResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_shop_car_activity);
        initView();
    }
    private void initView(){

        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();

        mListView = (ListView) findViewById(R.id.bag_goods_list);
        mShoppingBagAdapter = new ShoppingBagAdapter(this, mListView);
        mListView.setAdapter(mShoppingBagAdapter);
        mEditText = (TextView) findViewById(R.id.goods_edit);
        mEditText.setOnClickListener(this);
        getCart();

        findViewById(R.id.back).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_edit:
                if (mIsEdit) {
                    //todo 编辑状态
                    mIsEdit = false;
                    mEditText.setText("编辑");
                } else {
                    //todo 非编辑状态
                    mIsEdit = true;
                    mEditText.setText("完成");
                }
                mShoppingBagAdapter.setIsEditProduct(mIsEdit);
                break;

            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void getCart() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sCart, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //todo 接口通,等数据
                CartDataResponse cartDataResponse = JSON.parseObject(response.toString(), CartDataResponse.class);
                mCartDataResponse = cartDataResponse;
                mShoppingBagAdapter.setCartDataGroup(cartDataResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
