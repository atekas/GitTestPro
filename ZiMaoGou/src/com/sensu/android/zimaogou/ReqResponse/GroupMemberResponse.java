package com.sensu.android.zimaogou.ReqResponse;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/15.
 */
public class GroupMemberResponse extends BaseReqResponse {

    public String id;
    public GroupMemberData data;

    public class GroupMemberData {
        public List<MemberInfo> list;
    }

    public static class MemberInfo {
        public String avatar;
        public String uid;
    }
}
