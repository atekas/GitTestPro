package com.sensu.android.zimaogou.ReqResponse;

import java.io.Serializable;

/**
 * Created by zhangwentao on 2015/12/15.
 */
public class GroupDetailsResponse extends BaseReqResponse {

    public GroupDetailData data;

    public class GroupDetailData implements Serializable {
        public String content;
        public String id;
        public String media_type;
        public String price;
        public String min_num;
        public String name;
        public String max_num;
        public String price_market;
        public String media;
        public String member_num;
        public String description;
        public String is_join;
        public String code;
        public String price_goods;
        public String end_time;
        public String goods_id;
        public String state;
    }
}
