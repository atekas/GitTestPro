package com.sensu.android.zimaogou.ReqResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2016/1/4.
 */
public class CouponResponse extends BaseReqResponse {

    //优惠券原始数据
    public List<Coupon> data;

    //优惠券未使用
    public List<Coupon> mNoUseCouponList = new ArrayList<Coupon>();

    //以使用优惠券
    public List<Coupon> mUsedCouponList = new ArrayList<Coupon>();

    //已过期优惠券
    public List<Coupon> mPastCouponList = new ArrayList<Coupon>();

    //不可使用
    public List<Coupon> mCannotUseCouponList = new ArrayList<Coupon>();


    //将所有优惠券分开, 分为未使用 已使用 已过期
    public void splitData() {
        if (data != null) {
            for (Coupon coupon : data) {
                if (coupon.state.equals("1")) {
                    mNoUseCouponList.add(coupon);
                } else if (coupon.state.equals("2")) {
                    mUsedCouponList.add(coupon);
                    mCannotUseCouponList.add(coupon);
                } else if (coupon.state.equals("3")) {
                    mPastCouponList.add(coupon);
                    mCannotUseCouponList.add(coupon);
                }
            }
        }
    }

    public class Coupon {
        public String id;
        public String name;
        public String amount;
        public String total_amount;
        public String condition;
        public String start_time;
        public String end_time;
        public String state;
    }
}
