package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;

/**
 * Created by qi.yang on 2015/11/12.
 */
public class ProductMode implements Serializable {
    /**
     * 测试用本地图片
     */
    private int testImg;
    /**
     * 测试用标题
     *
     */
    private String testTitle;
    private boolean isChoose;//是否选择该商品
    private double price;
    private int num;//商品数量

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public int getTestImg() {
        return testImg;
    }

    public void setTestImg(int testImg) {
        this.testImg = testImg;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }
}
