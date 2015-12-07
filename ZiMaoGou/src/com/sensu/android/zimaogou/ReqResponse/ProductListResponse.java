package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/7.
 */
public class ProductListResponse  {

    public List<ProductListData> data;

    public static class ProductListData {
        public String id;
        public String name;
        public String category;
        public String category_sub;
        public String sale_title;
        public String origin;
        public String price;
        public String price_market;
        public String main_image;
    }
}
