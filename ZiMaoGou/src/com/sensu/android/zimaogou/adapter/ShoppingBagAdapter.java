package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.view.CartLinearLayout;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/12/2.
 */
public class ShoppingBagAdapter extends SimpleBaseAdapter {

    private CartDataResponse mCartDataResponse;

    //true  为编辑状态  false为未编辑状态
    public boolean mIsEditProduct;
    public ListView mListView;

    public ShoppingBagAdapter(Context context, ListView listView) {
        super(context);
        mListView = listView;
    }

    public void setCartDataGroup(CartDataResponse cartDataResponse) {
        mCartDataResponse = cartDataResponse;
        notifyDataSetChanged();
    }

    public void setIsEditProduct(boolean isEditProduct) {
        mIsEditProduct = isEditProduct;
        notifyDataSetChanged();
    }

    public void isAllSelect(int i) {
        CartDataResponse.CartDataGroup cartDataGroup = mCartDataResponse.data.get(i);
        for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
            if (cartDataChild.getIsSelect().equals("1")) {
                cartDataGroup.setIsAllSelect(true);
            } else {
                cartDataGroup.setIsAllSelect(false);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void productIsEmpty(int i) {
        int size = mCartDataResponse.data.get(i).data.size();
        if (size == 0) {
            mCartDataResponse.data.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCartDataResponse == null ? 0 : mCartDataResponse.data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ShoppingBagViewHolder shoppingBagViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, null);
            shoppingBagViewHolder = new ShoppingBagViewHolder();
            shoppingBagViewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
            shoppingBagViewHolder.tv_warehouseName = (TextView) view.findViewById(R.id.tv_warehouseName);
            shoppingBagViewHolder.product_child = (CartLinearLayout) view.findViewById(R.id.product_child);
            view.setTag(shoppingBagViewHolder);
        } else {
            shoppingBagViewHolder = (ShoppingBagViewHolder) view.getTag();
        }
        //todo mGoodsOrderAdapter 只刷新数据
        final CartDataResponse.CartDataGroup cartDataGroup = mCartDataResponse.data.get(i);

        for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
            if (cartDataChild.getIsSelect().equals("0")) {
                cartDataGroup.setIsAllSelect(false);
                break;
            } else {
                cartDataGroup.setIsAllSelect(true);
            }
        }

        shoppingBagViewHolder.tv_warehouseName.setText(cartDataGroup.deliver_address);

        if (cartDataGroup.getIsAllSelect()) {
            shoppingBagViewHolder.img_choose.setSelected(true);
        } else {
            shoppingBagViewHolder.img_choose.setSelected(false);
        }

        shoppingBagViewHolder.product_child.setCartGroup(cartDataGroup, mIsEditProduct, mListView, this, i);

        shoppingBagViewHolder.img_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartDataGroup.getIsAllSelect()) {
                    cartDataGroup.setIsAllSelect(false);
                    cartDataGroup.setEnable(false);
                    for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
                        cartDataChild.setIsSelect("0");
                        changeStatues("0", cartDataChild.id);
                    }
                } else {
                    cartDataGroup.setIsAllSelect(true);
                    cartDataGroup.setEnable(true);
                    for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
                        if (!cartDataChild.state.equals("1") || Integer.parseInt(cartDataChild.num) > Integer.parseInt(cartDataChild.real_num)) {
                            cartDataChild.setIsSelect("0");
                            changeStatues("0", cartDataChild.id);
                        } else {
                            cartDataChild.setIsSelect("1");
                            changeStatues("1", cartDataChild.id);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    class ShoppingBagViewHolder {
        public ImageView img_choose;
        public TextView tv_warehouseName;
        public CartLinearLayout product_child;
    }

    private void changeStatues(final String isSelect, String id) {
        UserInfo userInfo = GDUserInfoHelper.getInstance(mContext).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("is_selected", isSelect);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sCart + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
