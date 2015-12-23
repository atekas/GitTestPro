package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.CountryMode;
import com.sensu.android.zimaogou.Mode.LandMode;
import com.sensu.android.zimaogou.Mode.TravelTagMode;

import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/12/7.
 */
public class TravelSendResponse extends BaseReqResponse {

    public Comm data;
    public class Comm{
        public ArrayList<TravelTagMode> tag;

        public ArrayList<LandMode> country;
    }
}
