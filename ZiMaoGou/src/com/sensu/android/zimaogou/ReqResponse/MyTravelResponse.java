package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.TravelMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/27.
 */
public class MyTravelResponse extends BaseReqResponse implements Serializable{
    public MyTravel data;
    public class MyTravel{
        public ArrayList<TravelMode> travel;
    }
}
