package com.sensu.android.zimaogou.Mode;

/**
 * Created by zhangwentao on 2015/12/27.
 */
public class ProductTypeModel {

    private String mType;
    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    private String mTypeName;
    public void setTypeName(String typeName) {
        mTypeName = typeName;
    }

    public String getTypeName() {
        return mTypeName;
    }

    private boolean mEnable;
    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public boolean getEnable() {
        return mEnable;
    }

    private boolean mIsSelect;
    public void setIsSelect(boolean isSelect) {
        mIsSelect = isSelect;
    }

    public boolean getIsSelect() {
        return mIsSelect;
    }

}
