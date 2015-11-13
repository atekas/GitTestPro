package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;

/**
 * 推荐专题MODEL
 * Created by qi.yang on 2015/11/13.
 */
public class LivelyMode implements Serializable {
    private int TestImage;//测试用图片
    private int lowPrice;//最低价
    private String Title;

    public int getTestImage() {
        return TestImage;
    }

    public void setTestImage(int testImage) {
        TestImage = testImage;
    }

    public int getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(int lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
