package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;

/**
 * Created by qi.yang on 2015/12/23.
 */
public class CountryMode implements Serializable {
    private String id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
