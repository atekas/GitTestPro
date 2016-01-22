package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.MessageMode;
import com.sensu.android.zimaogou.Mode.ProvinceMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/27.
 */
public class MessageResponse extends BaseReqResponse implements Serializable{
    public ArrayList<MessageMode> data;
}
