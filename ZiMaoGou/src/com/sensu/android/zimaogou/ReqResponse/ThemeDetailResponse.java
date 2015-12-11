package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/11.
 */
public class ThemeDetailResponse {

    public List<ThemeDetailData> data;

    public class ThemeDetailData {
        public String id;
        public String source;
        public String name;
        public String media_type;
        public String media;
        public String sale_title;
        public String price;
    }
}
