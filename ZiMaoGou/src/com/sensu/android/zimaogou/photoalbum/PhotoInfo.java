package com.sensu.android.zimaogou.photoalbum;

import java.io.Serializable;

/**
 * 本地相册图片bean<br>
 * {@link #mPathPath} 绝对路径<br>
 * {@link #mChoose} 是否被选择<br>
 */
public class PhotoInfo implements Serializable {

    private String mPathPath;
    private boolean mChoose = false;
    private String mUploadPath;

    public String getmPathPath() {
        return mPathPath;
    }

    public void setmPathPath(String mPathPath) {
        this.mPathPath = mPathPath;
    }

    public boolean ismChoose() {
        return mChoose;
    }

    public void setmChoose(boolean mChoose) {
        this.mChoose = mChoose;
    }

    public String getmUploadPath() {
        return mUploadPath;
    }

    public void setmUploadPath(String mUploadPath) {
        this.mUploadPath = mUploadPath;
    }

    public String getPicPath() {
        return mPathPath;
    }

    public void setPathPath(String path) {
        this.mPathPath = path;
    }

    public boolean isChoose() {
        return mChoose;
    }

    public void setChoose(boolean choose) {
        this.mChoose = choose;
    }
}
