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
    public static final String sGoodsCollect = "goods/favorite";

    //游购列表
    public static final String sGetTravelList = "travel/list";
    //游购详情
    public static final String sGetTravelDetail = "travel/";
    //游购评论
    public static final String sTravelComment = "travel/comment";
    //游购点赞
    public static final String sTravelLike = "travel/like";
    //游购收藏
    public static final String sFavorite = "travel/favorite";
    public static final String sRegister = "user/register";
    //上传图片
    public static final String sImageUpload = "image/upload";
    //上传视频
    public static final String sVideoUpload = "video/upload";
    //获取配置：国家
    public static final String sGetComm = "conf/comm";
    //获取游购标签
    public static final String sGetTravelTags = "travel/taglist";
    //发布游购
    public static final String sSendTravel = "travel";
    //获取城市
    public static final String sGetProvinceAndCity = "conf/city";
    //团购
    public static final String sTb_list = "tb/list";
    public static final String sJoin_group = "tb/join";
    public static final String sGroup_create = "tb/build";
    public static final String sTb_member = "tb/member";
    public static final String sTb_detail = "tb/";

    //订单
    public static final String sOrder = "order";
    //运费规则
    public static final String sExpressRule = "conf/express_rule";


    //首页
    public static final String sHome_grid = "goods/tag6";


    //购物车
    public static final String sCart = "cart";


    //获取我的收货地址
    public  static final String sGetReceiverAddress = "my/address";
    //添加收货地址
    public  static final String sAddAddress = "user/address";

    //获取我的订单
    public static final String sGetMyOrder = "my/order";
    //获取我的收藏
    public static final String sGetCollect = "my/favorite";
    //获取我的游购
    public static final String sGetMyTravel = "my/travels";

    //获取支付charge
    public static final String sGetPayCharge = "pay";

    //搜索热词
    public static final String sHotKeywords = "goods/search/keywords";

}
