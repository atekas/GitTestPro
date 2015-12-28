package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.Mode.ReceiverAddressMode;
import com.sensu.android.zimaogou.adapter.ReceiverListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/27.
 */
public class ReceiverAddressResponse extends BaseReqResponse implements Serializable{
    public ArrayList<ReceiverAddressMode> data;
}
