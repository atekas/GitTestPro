package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.Mode.RefundReasonMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/27.
 */
public class LogisticsCompanyResponse extends BaseReqResponse implements Serializable{
    public ArrayList<RefundReasonMode> data;
}
