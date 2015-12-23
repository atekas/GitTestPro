package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/23.
 */
public class LandMode implements Serializable {
    private String id;
    private String name;
    private ArrayList<CountryMode> sub;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CountryMode> getSub() {
        return sub;
    }

    public void setSub(ArrayList<CountryMode> sub) {
        this.sub = sub;
    }
}
