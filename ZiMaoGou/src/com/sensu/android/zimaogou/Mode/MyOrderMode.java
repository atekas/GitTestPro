package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/29.
 */
public class MyOrderMode implements Serializable {
    private String id;
    private int state;
    private String amount_total;
    private String amount_express;
    private String amount_coupon;
    private String amount_tax;
    private String amount_real;
    private String amount_goods;
    private String deliver_address;
    private String created_at;
    private MyOrderReceiverInfo receiver_info;
    private ArrayList<MyOrderGoodsMode> goods;
    private String order_no;
    private String state_cn;
    private String receiver_address;

    private String pay_type;

    public String getAmount_goods() {
        return amount_goods;
    }

    public void setAmount_goods(String amount_goods) {
        this.amount_goods = amount_goods;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getAmount_real() {
        return amount_real;
    }

    public void setAmount_real(String amount_real) {
        this.amount_real = amount_real;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getState_cn() {
        return state_cn;
    }

    public void setState_cn(String state_cn) {
        this.state_cn = state_cn;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(String amount_total) {
        this.amount_total = amount_total;
    }

    public String getAmount_express() {
        return amount_express;
    }

    public void setAmount_express(String amount_express) {
        this.amount_express = amount_express;
    }

    public String getAmount_coupon() {
        return amount_coupon;
    }

    public void setAmount_coupon(String amount_coupon) {
        this.amount_coupon = amount_coupon;
    }

    public String getAmount_tax() {
        return amount_tax;
    }

    public void setAmount_tax(String amount_tax) {
        this.amount_tax = amount_tax;
    }

    public String getDeliver_address() {
        return deliver_address;
    }

    public void setDeliver_address(String deliver_address) {
        this.deliver_address = deliver_address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public MyOrderReceiverInfo getReceiver_info() {
        return receiver_info;
    }

    public void setReceiver_info(MyOrderReceiverInfo receiver_info) {
        this.receiver_info = receiver_info;
    }

    public ArrayList<MyOrderGoodsMode> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<MyOrderGoodsMode> goods) {
        this.goods = goods;
    }
}
