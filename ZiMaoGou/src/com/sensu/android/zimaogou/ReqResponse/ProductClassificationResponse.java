package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/8.
 */
public class ProductClassificationResponse extends BaseReqResponse {

    public List<ProductCategory> data;

    public class ProductCategory {
        public String id;
        public String name;
        public String image;
        public List<CategoryList> sub;
    }

    public static class CategoryList {
        public String id;
        public String name;
        public String image;
    }
}
