package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.SelectProductModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.activity.VerifyOrderActivity;
import com.sensu.android.zimaogou.adapter.ShoppingBagAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/12/24.
 * <p/>
 * 购物车子项
 */
public class CartLinearLayout extends LinearLayout {

    //true 编辑状态    false 非编辑状态
    private boolean mIsEdit;

    private CartDataResponse.CartDataGroup mCartDataGroup;
    private ShoppingBagAdapter mShoppingBagAdapter;

    private int mFlag;

    private Button mSubmitBtm;
    private TextView mSelectNum;
    private TextView mSumPrice;

    public CartLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void setCartGroup(CartDataResponse.CartDataGroup cartDataGroup, boolean isEdit
            , ListView listView, ShoppingBagAdapter shoppingBagAdapter, int flag) {
        mFlag = flag;
        mShoppingBagAdapter = shoppingBagAdapter;
        mCartDataGroup = cartDataGroup;
        mIsEdit = isEdit;
        removeAllViews();
        for (int i = 0; i < cartDataGroup.data.size(); i++) {
            final View childView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_child_item, null);

            ImageUtils.displayImage(cartDataGroup.data.get(i).image, ((ImageView) childView.findViewById(R.id.img_pro)));
            ((TextView) childView.findViewById(R.id.tv_productName)).setText(cartDataGroup.data.get(i).title);
            ((TextView) childView.findViewById(R.id.tv_productPrice)).setText("¥" + cartDataGroup.data.get(i).price);
            ((TextView) childView.findViewById(R.id.tv_productNum)).setText("x" + cartDataGroup.data.get(i).num);
            ((TextView) childView.findViewById(R.id.product_type))
                    .setText(cartDataGroup.data.get(i).spec);
            ((EditText) childView.findViewById(R.id.et_productNum)).setText(cartDataGroup.data.get(i).num);
            if (cartDataGroup.data.get(i).getIsSelect()) {
                childView.findViewById(R.id.img_choose).setSelected(true);
            } else {
                childView.findViewById(R.id.img_choose).setSelected(false);
            }

            childView.setId(i);
            if (mIsEdit) {
                //编辑状态
                childView.findViewById(R.id.rl_showType).setVisibility(GONE);
                childView.findViewById(R.id.ll_editNum).setVisibility(VISIBLE);
            } else {
                childView.findViewById(R.id.rl_showType).setVisibility(VISIBLE);
                childView.findViewById(R.id.ll_editNum).setVisibility(GONE);
            }

            final int position = i;
            childView.findViewById(R.id.img_choose).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("点击了" + position + "个选择");
                    if (mCartDataGroup.data.get(position).getIsSelect()) {
                        mCartDataGroup.data.get(position).setIsSelect(false);
                    } else {
                        mCartDataGroup.data.get(position).setIsSelect(true);
                    }
                    mShoppingBagAdapter.isAllSelect(mFlag);
                }
            });

            childView.findViewById(R.id.bt_subtract).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("点击了第" + position + "项减号");
                    String num = ((EditText) childView.findViewById(R.id.et_productNum)).getText().toString();
                    int productNum = Integer.parseInt(num);
                    if (productNum > 1) {
                        productNum--;
                        changeNum(mCartDataGroup.data.get(position).spec_id, String.valueOf(productNum), position);
                    }
                }
            });

            childView.findViewById(R.id.bt_add).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("点击了第" + position + "项加号");
                    String num = ((EditText) childView.findViewById(R.id.et_productNum)).getText().toString();
                    int productNum = Integer.parseInt(num);
                    productNum++;
                    changeNum(mCartDataGroup.data.get(position).spec_id, String.valueOf(productNum), position);
                }
            });

            childView.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PromptUtils.showToast("删除第" + position + "项");
                    deleteProduct(mCartDataGroup.data.get(position).spec_id, position);
                }
            });

            new FrontViewToMove(childView.findViewById(R.id.content), listView);
            addView(childView);
        }

        View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_cart_bottom, null);

        mSubmitBtm = (Button) bottomView.findViewById(R.id.bt_submit);
        mSelectNum = (TextView) bottomView.findViewById(R.id.product_count);
        mSumPrice = (TextView) bottomView.findViewById(R.id.total_money);

        mSubmitBtm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptUtils.showToast("点击了第" + mFlag + "的结算按钮");

                SelectProductModel selectProductModel = new SelectProductModel();
                List<SelectProductModel.GoodsInfo> goodsInfoList = new ArrayList<SelectProductModel.GoodsInfo>();
                for (CartDataResponse.CartDataChild cartDataChild : mCartDataGroup.data) {
                    if (cartDataChild.getIsSelect()) {
                        SelectProductModel.GoodsInfo goodsInfo = new SelectProductModel.GoodsInfo();
                        goodsInfo.setGoodsId(cartDataChild.goods_id);
                        goodsInfo.setSpecId(cartDataChild.spec_id);
                        goodsInfo.setNum(cartDataChild.num);
                        goodsInfo.setPrice(cartDataChild.price);
                        goodsInfo.setSource(cartDataChild.source);
                        goodsInfo.setName(cartDataChild.title);
                        goodsInfo.setSpec(cartDataChild.spec);
                        goodsInfo.setImage(cartDataChild.image);
                        goodsInfoList.add(goodsInfo);
                    }
                }
                selectProductModel.setGoodsInfo(goodsInfoList);
                selectProductModel.setTotalMoney(getAllMoney());
                if (selectProductModel.getGoodsInfo().size() == 0) {
                    PromptUtils.showToast("请选择要付款的商品");
                    return;
                }

                Intent intent = new Intent(getContext(), VerifyOrderActivity.class);
                intent.putExtra(VerifyOrderActivity.PRODUCT_FOR_PAY, selectProductModel);
                getContext().startActivity(intent);
            }
        });

        mSelectNum.setText("已选商品" + getSelectNum() + "件");
        mSumPrice.setText("¥" + getAllMoney());

        addView(bottomView);
    }

    private void changeNum(String id, final String num, final int position) {
        UserInfo userInfo = GDUserInfoHelper.getInstance(getContext()).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("num", num);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sCart + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mCartDataGroup.data.get(position).num = num;
                mShoppingBagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void deleteProduct(String id, final int position) {
        UserInfo userInfo = GDUserInfoHelper.getInstance(getContext()).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sCart + "/" + id + "/delete", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //删除成功
                mCartDataGroup.data.remove(position);
                mShoppingBagAdapter.productIsEmpty(mFlag);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("删除失败");
            }
        });
    }

    private int getSelectNum() {
        int num = 0;
        for (CartDataResponse.CartDataChild cartDataChild : mCartDataGroup.data) {
            if (cartDataChild.getIsSelect()) {
                num += Integer.parseInt(cartDataChild.num);
            }
        }
        return num;
    }

    private int getAllMoney() {
        int allMoney = 0;
        for (CartDataResponse.CartDataChild cartDataChild : mCartDataGroup.data) {
            if (cartDataChild.getIsSelect()) {
                int price = Integer.parseInt(cartDataChild.price);
                int num = Integer.parseInt(cartDataChild.num);
                allMoney += price * num;
            }
        }
        return allMoney;
    }
}
