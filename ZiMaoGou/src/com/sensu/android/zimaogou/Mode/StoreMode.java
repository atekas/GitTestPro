package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;

/**
 * Created by qi.yang on 2015/11/13.
 */
public class StoreMode implements Serializable {
    private int TestStoreImage;//测试图片
    private String StoreName;
    private int TestCountryImage;
    private String TestCountryName;

    public int getTestStoreImage() {
        return TestStoreImage;
    }

    public void setTestStoreImage(int testStoreImage) {
        TestStoreImage = testStoreImage;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public int getTestCountryImage() {
        return TestCountryImage;
    }

    public void setTestCountryImage(int testCountryImage) {
        TestCountryImage = testCountryImage;
    }

    public String getTestCountryName() {
        return TestCountryName;
    }

    public void setTestCountryName(String testCountryName) {
        TestCountryName = testCountryName;
    }
}
