package com.sensu.android.zimaogou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ExceptionLinearLayout;
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

    private CartDataResponse mCartDataResponse;

    private LinearLayout mNoOrderView;

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
            if (mUserInfo != null) {
                getCart();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNoOrderView != null) {
            mNoOrderView.setVisibility(View.GONE);
        }
        initView();
    }

    @Override
    protected void initView() {
        mNoOrderView = (LinearLayout) mParentActivity.findViewById(R.id.no_order);
        mListView = (ListView) mParentActivity.findViewById(R.id.bag_goods_list);
        mNoOrderView.findViewById(R.id.bt_reload).setOnClickListener(this);
        mUserInfo = GDUserInfoHelper.getInstance(mParentActivity).getUserInfo();
        if (mUserInfo == null) {
            mNoOrderView.setVisibility(View.VISIBLE);
            ((ImageView) mNoOrderView.findViewById(R.id.img_exception)).setImageResource(R.drawable.exception_internet);
            ((TextView) mNoOrderView.findViewById(R.id.tv_exception)).setText("您还没有登录");
            ((Button) mNoOrderView.findViewById(R.id.bt_reload)).setText("登录");
            return;
        }
        getCart();
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
            case R.id.bt_reload:
                mParentActivity.startActivity(new Intent(mParentActivity, LoginActivity.class));
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
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                CartDataResponse cartDataResponse = JSON.parseObject(response.toString(), CartDataResponse.class);
                mCartDataResponse = cartDataResponse;
                mShoppingBagAdapter.setCartDataGroup(cartDataResponse);
                if (cartDataResponse.data.size() == 0) {
                    ((ExceptionLinearLayout) mNoOrderView.findViewById(R.id.ll_exception)).setException(IConstants.EXCEPTION_SHOP_IS_NULL);
                    mNoOrderView.setVisibility(View.VISIBLE);
                    mEditText.setVisibility(View.GONE);
                } else {
                    mNoOrderView.setVisibility(View.GONE);
                    mEditText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
