package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/10.
 */
public class ProductDetailsResponse extends BaseReqResponse {
    public ProductDetailData data;

    public class ProductDetailData {
        public String sale_title;
        public String weight;
        public String state;
        public String deliver_address;
        public String origin;
        public String price_market;
        public String is_favorite;
        public String id;
        public String category;
        public String rate;
        public String price;
        public String description;
        public String is_7d_return;
        public String name;
        public String capacity;
        public String category_sub;

        public MediaData media;
        public List<NormalSpec> normal_spec;
        public List<ColorImage> color_image;
        public List spec_attr;
        public List spec;
    }

    public static class ColorImage {
        public String image;
        public String name;
    }

    public static class NormalSpec {
        public String value;
        public String id;
        public String name;
    }

    public static class MediaData {
        public String type;
        public String video;
        public String cover;
        public List<String> image;
    }
}
