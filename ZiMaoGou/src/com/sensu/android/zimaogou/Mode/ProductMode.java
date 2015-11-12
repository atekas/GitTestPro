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
