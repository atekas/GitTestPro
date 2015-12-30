package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/30.
 */
public class ExpressRuleResponse extends BaseReqResponse {

    public List<ExpressRuleData> data;

    public class ExpressRuleData {
        public String id;
        public String deliver_address;
        public String start_amount;
        public String end_amount;
        public String express_amount;
    }

}
