package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;

/**
 * Created by qi.yang on 2016/1/14.
 */
public class RefundReasonMode implements Serializable {
    private String id;
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}