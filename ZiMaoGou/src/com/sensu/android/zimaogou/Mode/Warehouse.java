package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商品发货仓库类
 * Created by qi.yang on 2015/11/16.
 */
public class Warehouse implements Serializable{
    private String Name;
    private ArrayList<ProductMode> pros;

    private boolean isChoose;//是否选择该仓库

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
        for(int i = 0 ; i < pros.size() ; i++){
            pros.get(i).setChoose(isChoose);
        }
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<ProductMode> getPros() {
        return pros;
    }

    public void setPros(ArrayList<ProductMode> pros) {
        this.pros = pros;
    }
}
