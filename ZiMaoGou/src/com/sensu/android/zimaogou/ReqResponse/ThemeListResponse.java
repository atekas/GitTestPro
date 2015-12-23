package com.sensu.android.zimaogou.ReqResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangwentao on 2015/12/11.
 */
public class ThemeListResponse extends BaseReqResponse {

    public List<ThemeListData> data;

    public class ThemeListData implements Serializable {
        public String id;
        public String content;
        public String price;
        public Media media;
        public String name;
    }

    public static class Media implements Serializable {
        public String cover;
        public String video;
        public String type;
    }
}
