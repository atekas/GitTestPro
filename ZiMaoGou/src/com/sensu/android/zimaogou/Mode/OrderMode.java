package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderMode implements Serializable {
    private int Type = 1;//订单类型
    private String orderNo;//订单号
    private ArrayList<ProductMode> pros = new ArrayList<ProductMode>();

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public ArrayList<ProductMode> getPros() {
        return pros;
    }

    public void setPros(ArrayList<ProductMode> pros) {
        this.pros = pros;
    }
}
