package com.sensu.android.zimaogou.ReqResponse;

/**
 * Created by zhangwentao on 2015/12/7.
 */
public class CheckCodeResponse extends BaseReqResponse {

    public CheckCode data;

    public class CheckCode {
        public String is_pass;
    }
}
