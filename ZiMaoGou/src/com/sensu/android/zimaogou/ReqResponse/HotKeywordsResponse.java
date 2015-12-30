package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/30.
 */
public class HotKeywordsResponse extends BaseReqResponse {

    public List<HotKeywordsData> data;

    public class HotKeywordsData {
        public String id;
        public String name;
    }
}
