package com.sensu.android.zimaogou.ReqResponse;

/**
 * Created by zhangwentao on 2015/12/7.
 */
public class AuthCodeResponse extends BaseReqResponse {

    public AuthCode data;

    public class AuthCode {
        public String mobile;
        public String recode;
    }
}
