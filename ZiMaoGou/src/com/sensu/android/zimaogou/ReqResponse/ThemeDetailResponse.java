package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/11.
 */
public class ThemeDetailResponse {

    public List<ThemeDetailData> data;

    public class ThemeDetailData {
        public String theme_id;
        public String id;
        public String source;
        public String name;
        public Media media;
        public String sale_title;
        public String price;
        public String price_market;
    }

    public static class Media {
        public String type;
        public String video;
        public String cover;
        public List<String> image;
    }
}
