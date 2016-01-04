package com.sensu.android.zimaogou.utils;

/**
 * Created by zhangwentao on 2015/12/29.
 *
 * 计算商品的税率
 *
 * 税费计算:  rate1 = 商品金额 * 数量 * 税率     单个商品的税费
 *
 * 总税费     单个商品税费相加
 */
public class RateUtil {

    /**
     *  用于将百分数转化为小数
     * @param rate 税率字符串   20%
     * @return
     */
    public static double getRate(String rate) {
        if (rate.contains("%")) {
            String s = rate.replace("%", "");
            double ratePercent =  Double.parseDouble(s) / 100;
            return ratePercent;
        } else {
            return 0.0;
        }
    }

    /**
     *   选择优惠券后的税费公式:  (选中商品的总金额 - 优惠金额) / 选中商品的总金额 * 未选择优惠券时得出的税费
     * @param amountMoney  选中商品的总金额
     * @param rate    未选择优惠券时得出的税费
     * @param coupon 优惠金额
     * @return  打折后的税费
     */
    public static double getRateMoneyWithCoupon(double amountMoney, double rate, double coupon) {
        if (StringUtils.getDoubleWithTwo(rate).equals("0.00")) {
            return 0.00;
        } else {
            return (amountMoney - coupon) / amountMoney * rate;
        }
    }

}
