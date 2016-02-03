package com.sensu.android.zimaogou.Mode;

import java.io.Serializable;

/**
 * Created by qi.yang on 2016/2/3.
 */
public class ExpressInfoMode implements Serializable {
    private String context;
    private String time;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
