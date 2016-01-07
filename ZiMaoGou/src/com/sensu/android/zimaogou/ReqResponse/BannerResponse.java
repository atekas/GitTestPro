package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.BannerMode;
import com.sensu.android.zimaogou.Mode.ProvinceMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/27.
 */
public class BannerResponse extends BaseReqResponse implements Serializable{
    public ArrayList<BannerMode> data;
}
