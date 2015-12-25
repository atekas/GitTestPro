package com.sensu.android.zimaogou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.adapter.ShoppingBagAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class ShoppingBagFragment extends BaseFragment implements View.OnClickListener {

    private ListView mListView;
    private ShoppingBagAdapter mShoppingBagAdapter;
    private boolean mIsEdit;

    private UserInfo mUserInfo;

    private TextView mEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopping_bag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    @Override
    protected void initView() {

        mUserInfo = GDUserInfoHelper.getInstance(mParentActivity).getUserInfo();
        if (mUserInfo == null) {
            mParentActivity.startActivity(new Intent(mParentActivity, LoginActivity.class));
        } else {
            getCart();
        }

        mListView = (ListView) mParentActivity.findViewById(R.id.bag_goods_list);
        mShoppingBagAdapter = new ShoppingBagAdapter(mParentActivity, mListView);
        mListView.setAdapter(mShoppingBagAdapter);
        mEditText = (TextView) mParentActivity.findViewById(R.id.goods_edit);
        mEditText.setOnClickListener(this);
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
                mShoppingBagAdapter.setCartDataGroup(cartDataResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
