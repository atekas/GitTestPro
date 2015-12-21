package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/21.
 */
public class HomeGridResponse extends BaseReqResponse {

    public List<HomeGridData> data;

    public class HomeGridData {
        public String id;
        public String name;
        public String alias;
        public String image;
    }
}
