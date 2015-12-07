package com.sensu.android.zimaogou.ReqResponse;

import java.io.Serializable;

/**
 * Created by zhangwentao on 2015/11/12.
 */
public class BaseReqResponse implements Serializable {

    public BaseReqResponse() {

    }

    private String ret;
    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    private String msg;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
