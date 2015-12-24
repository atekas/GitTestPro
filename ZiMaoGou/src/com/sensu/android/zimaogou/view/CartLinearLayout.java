package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/12/24.
 *
 * 购物车子项
 */
public class CartLinearLayout extends LinearLayout {

    //true 编辑状态    false 非编辑状态
    private boolean mIsEdit;
    private View[] mViews;

    private CartDataResponse.CartDataGroup mCartDataGroup;

    public CartLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void setCartGroup(CartDataResponse.CartDataGroup cartDataGroup, boolean isEdit
            , ListView listView) {
        mCartDataGroup = cartDataGroup;
        mIsEdit = isEdit;
        removeAllViews();
        mViews = null;
        mViews = new View[cartDataGroup.data.size()];
        for (int i = 0; i < cartDataGroup.data.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_child_item, null);

            ImageUtils.displayImage(cartDataGroup.data.get(i).image, ((ImageView) view.findViewById(R.id.img_pro)));
            ((TextView) view.findViewById(R.id.tv_productName)).setText(cartDataGroup.data.get(i).title);
            ((TextView) view.findViewById(R.id.tv_productPrice)).setText("¥" + cartDataGroup.data.get(i).price);
            ((TextView) view.findViewById(R.id.tv_productNum)).setText("x" + cartDataGroup.data.get(i).num);
            ((TextView) view.findViewById(R.id.product_type))
                    .setText(cartDataGroup.data.get(i).spec);

            view.setId(i);
            if (mIsEdit) {
                //编辑状态
                view.findViewById(R.id.rl_showType).setVisibility(GONE);
                view.findViewById(R.id.ll_editNum).setVisibility(VISIBLE);
            } else {
                view.findViewById(R.id.rl_showType).setVisibility(VISIBLE);
                view.findViewById(R.id.ll_editNum).setVisibility(GONE);
                //非编辑状态
                if (cartDataGroup.getIsAllSelect()) {
                    view.findViewById(R.id.img_choose).setSelected(true);
                } else {
                    view.findViewById(R.id.img_choose).setSelected(false);
                }
            }

            final int position = i;
            view.findViewById(R.id.img_choose).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("点击了" + position + "个选择");
                    if (mCartDataGroup.data.get(position).getIsSelect()) {
                        mCartDataGroup.data.get(position).setIsSelect(false);
                        view.findViewById(R.id.img_choose).setSelected(false);
                    } else {
                        mCartDataGroup.data.get(position).setIsSelect(true);
                        view.findViewById(R.id.img_choose).setSelected(true);
                    }
                }
            });

            view.findViewById(R.id.bt_subtract).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("点击了第" + position + "项减号");
                }
            });

            view.findViewById(R.id.bt_add).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("点击了第" + position + "项加号");
                }
            });

            view.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("删除第" + position + "项");
                    deleteProduct(mCartDataGroup.data.get(position).id);
                }
            });

            new FrontViewToMove(view.findViewById(R.id.content), listView);
            addView(view);
        }

        View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_cart_bottom, null);

        bottomView.findViewById(R.id.bt_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptUtils.showToast("去结算");
            }
        });

        addView(bottomView);
    }

    private void changeNum(String id, String num) {
        UserInfo userInfo = GDUserInfoHelper.getInstance(getContext()).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("num", num);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sCart + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void deleteProduct(String id) {
        UserInfo userInfo = GDUserInfoHelper.getInstance(getContext()).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sCart + "/" + id+ "/delete", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
