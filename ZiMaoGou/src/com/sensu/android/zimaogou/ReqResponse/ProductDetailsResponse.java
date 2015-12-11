package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/10.
 */
public class ProductDetailsResponse extends BaseReqResponse {
    public ProductDetailData data;

    public class ProductDetailData {
        public String sale_title;
        public String media_type;
        public String weight;
        public String state;
        public String deliver_address;
        public String origin;
        public String price_market;
        public String id;
        public String category;
        public String rate;
        public String price;
        public String description;
        public String is_7d_return;
        public String name;
        public String capacity;
        public String category_sub;

        public List<NormalSpec> normal_spec;
        public List<ColorImage> color_image;
        public List spec;
        public List media;
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
}
