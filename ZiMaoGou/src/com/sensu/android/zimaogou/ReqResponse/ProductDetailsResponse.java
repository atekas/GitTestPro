package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.ProductCommentMode;

import java.util.ArrayList;
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
        public String favorite_id;
        public String id;
        public String category;
        public String rate;
        public String price;
        public String description;
        public String is_7d_return;
        public String name;
        public String capacity;
        public String category_sub;

        public PriceInterval price_interval;
        public MediaData media;
        public List<NormalSpec> normal_spec;
        public List<ColorImage> color_image;
        public List<SpecAttr> spec_attr;
        public List<Spec> spec;
        public ArrayList<ProductCommentMode> comment;
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

    public static class SpecAttr {
        public String cn;
        public String en;
    }

    public static class Spec {
        public String id;
        public String source;
        public String capacity;
        public String color;
        public String size;
        public String price;
        public String price_market;
        public String num;
        public String seller_no;
    }

    public static class PriceInterval {
        public String min_price;
        public String max_price;
        public String min_price_market;
        public String max_price_market;
    }
}
