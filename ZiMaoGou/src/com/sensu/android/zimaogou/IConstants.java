package com.sensu.android.zimaogou;

/**
 * Created by zhangwentao on 2015/12/7.
 */
public class IConstants {

    //获取时间戳
    public static final String sTimeStamp = "comm/timestamp";
    //验证短信验证码
    public static final String sCheck = "comm/recode/check";
    //获取注册验证码
    public static final String sGetRegisterCode = "comm/recode/register";
    //获取忘记密码验证码
    public static final String sGetForgetPassCode = "comm/recode/password_forget";
    //登录
    public static final String sLogin = "user/login";
    //获取时间戳
    public static final String sGetTimestamp = "comm/timestamp";
    //获签名
    public static final String sGetSign = "comm/generate/sign";
    //退出登录
    public static final String sLoginOut = "user/loginout";
    //忘记密码
    public static final String sForgetPass = "user/password_modify";
    //修改用户信息
    public static final String sUpdateUserInfo = "user";
    //关于我们
    public static final String sAboutUs = "http://139.196.108.137:80/v1/conf/about_us";
    //商品列表
    public static final String sGoodList = "goods/list";
    public static final String sProduct_classification = "goods/category";
    //商品详情
    public static final String sProduct_detail = "goods/";
    public static final String sGoodTheme = "goods/theme";

    //游购列表
    public static final String sGetTravelList = "travel/list";
    //游购详情
    public static final String sGetTravelDetail = "travel/";
    //游购评论
    public static final String sTravelComment = "travel/comment";
    //游购点赞
    public static final String sTravelLike = "travel/like";

    public static final String sRegister = "user/register";



    //团购
    public static final String sTb_list = "tb/list";
    public static final String sJoin_group = "tb/join";
    public static final String sGroup_create = "tb/build";
    public static final String sTb_member = "tb/member";
    public static final String sTb_detail = "tb/";
}
