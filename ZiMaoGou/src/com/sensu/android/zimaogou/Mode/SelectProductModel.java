package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangwentao on 2015/12/28.
 */
public class SelectProductModel implements Serializable {

    private List<GoodsInfo> mGoodsInfo;
    public void setGoodsInfo(List<GoodsInfo> goodsInfo) {
        mGoodsInfo = goodsInfo;
    }

    public List<GoodsInfo> getGoodsInfo() {
        return mGoodsInfo;
    }

    private double mTotalMoney;
    public void setTotalMoney(double totalMoney) {
        mTotalMoney = totalMoney;
    }

    public double getTotalMoney() {
        return mTotalMoney;
    }

    public static class GoodsInfo implements Serializable {
        private String mGoodsId;

        public void setGoodsId(String goodsId) {
            mGoodsId = goodsId;
        }

        public String getGoodsId() {
            return mGoodsId;
        }

        private String mSource;

        public void setSource(String source) {
            mSource = source;
        }

        public String getSource() {
            return mSource;
        }

        private String mSpecId;

        public void setSpecId(String specId) {
            mSpecId = specId;
        }

        public String getSpecId() {
            return mSpecId;
        }

        private String mPrice;

        public void setPrice(String price) {
            mPrice = price;
        }

        public String getPrice() {
            return mPrice;
        }

        private String mNum;

        public void setNum(String num) {
            mNum = num;
        }

        public String getNum() {
            return mNum;
        }

        public String mName;
        public void setName(String name) {
            mName = name;
        }
        public String getName() {
            return mName;
        }

        public String mImage;
        public void setImage(String image) {
            mImage = image;
        }
        public String getImage() {
            return mImage;
        }

        public String mSpec;
        public void setSpec(String spec) {
            mSpec = spec;
        }

        public String getSpec() {
            return mSpec;
        }

        public String mRate;
        public void setRate(String rate) {
            mRate = rate;
        }

        public String getRate() {
            return mRate;
        }
    }

}
