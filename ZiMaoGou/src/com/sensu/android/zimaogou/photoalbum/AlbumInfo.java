package com.sensu.android.zimaogou.photoalbum;

import java.io.Serializable;
import java.util.List;

/**
 * 相册bean<br>
 * {@link #mAlbumName} 相册名称<br>
 */
public class AlbumInfo implements Serializable {

    private String mAlbumName;
    private List<PhotoInfo> mList;

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String name) {
        this.mAlbumName = name;
    }

    public List<PhotoInfo> getList() {
        return mList;
    }

    public void setList(List<PhotoInfo> list) {
        this.mList = list;
    }
}
