package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CouponResponse;
import com.sensu.android.zimaogou.utils.StringUtils;

import java.util.List;

/**
 * Created by qi.yang on 2015/11/19.
 */
public class CouponInvalidListAdapter extends SimpleBaseAdapter {

    private List<CouponResponse.Coupon> mCouponList;

    public CouponInvalidListAdapter(Context context) {
        super(context);
    }

    public void setCouponData(List<CouponResponse.Coupon> couponData) {
        mCouponList = couponData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCouponList == null ? 0 : mCouponList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.coupon_list_item2,null);
        }

        CouponResponse.Coupon coupon = mCouponList.get(i);
        ImageView img_coupon = (ImageView)view.findViewById(R.id.img_coupon);
        if(coupon.state.equals("2")){
            img_coupon.setImageResource(R.drawable.y_06);
        }else{
            img_coupon.setImageResource(R.drawable.used_coupon);
        }
        ((TextView) view.findViewById(R.id.tv_money)).setText(StringUtils.deleteZero(coupon.amount));

        ((TextView) view.findViewById(R.id.tv_moneyUsed)).setText(coupon.condition);

        ((TextView) view.findViewById(R.id.tv_moneyTime)).setText(coupon.start_time.substring(0, 10) + "至" + coupon.end_time.substring(0, 10));

        return view;
    }
}
