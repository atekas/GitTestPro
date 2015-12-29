package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/24.
 */
public class CartDataResponse extends BaseReqResponse {

    public List<CartDataGroup> data;

    public class CartDataGroup {
        public String deliver_address;
        public List<CartDataChild> data;

        private boolean mIsAllSelect;
        public boolean getIsAllSelect() {
            return mIsAllSelect;
        }
        public void setIsAllSelect(boolean isAllSelect) {
            mIsAllSelect = isAllSelect;
        }
    }

    public static class CartDataChild {
        public String spec;
        public String spec_id;
        public String title;
        public String num;
        public String price;
        public String state;
        public String deliver_address;
        public String image;
        public String real_num;
        public String goods_id;
        public String source;
        public String rate;

        private boolean mIsSelect;

        public void setIsSelect(boolean isSelect) {
            mIsSelect = isSelect;
        }

        public boolean getIsSelect() {
            return mIsSelect;
        }
    }

}
