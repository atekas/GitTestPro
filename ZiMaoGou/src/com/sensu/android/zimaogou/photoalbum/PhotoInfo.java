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
