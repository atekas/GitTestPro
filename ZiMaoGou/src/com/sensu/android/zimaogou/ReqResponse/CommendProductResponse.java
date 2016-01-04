package com.sensu.android.zimaogou.ReqResponse;

import com.sensu.android.zimaogou.Mode.ProductCommentMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/12/10.
 */
public class CommendProductResponse extends BaseReqResponse {
    public ArrayList<CommendProductMode> data  = new ArrayList<CommendProductMode>();
    public class CommendProductMode{
        public String id;
        public String name;
        public String category;
        public String category_sub;
        public String sale_title;
        public String origin;
        public String price;
        public String price_market;
        public ProductDetailsResponse.MediaData media;
        public String broad_image;
    }
}
