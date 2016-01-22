package com.sensu.android.zimaogou;

import com.sensu.android.zimaogou.utils.FileUtils;

/**
 * Created by zhangwentao on 2015/12/7.
 */
public class IConstants {

    /**
     * 默认时长
     */
    public static  int DEFAULT_DURATION_LIMIT = 15;
    /**
     * 默认码率
     */
    public static  int DEFAULT_BITRATE =2000 * 1000;
    /**
     * 默认Video保存路径，请开发者自行指定
     */
    public static  String VIDEOPATH = FileUtils.newOutgoingFilePath();
    /**
     * 默认缩略图保存路径，请开发者自行指定
     */
    public static  String THUMBPATH =  VIDEOPATH + ".jpg";
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static  String WATER_MARK_PATH ="res://drawable-xhdpi/head_pic_default.png";

    //ping++支付页面
    public static final String EXTRA_CHARGE = "com.pingplusplus.android.PaymentActivity.CHARGE";

    public static final int EXCEPTION_NO_INTERNET = 1;//没有网

    public static final int EXCEPTION_SHOP_IS_NULL = 2;//购物车为空

    public static final int EXCEPTION_ORDER_IS_NULL = 3;//订单为空

    public static final int EXCEPTION_COUPON_IS_NULL = 4;//我的优惠券为空

    public static final int EXCEPTION_ADDRESS_IS_NULL = 5;//我的收货地址为空

    public static final int EXCEPTION_GOODS_IS_NULL = 6;//商品为空

    public static final int EXCEPTION_TIPS_IS_NULL = 7;//我的收藏帖子为空

    public static final int EXCEPTION_MY_GOODS_IS_NULL = 8;//我的收藏商品为空

    public static final int EXCEPTION_MY_GROUP_IS_NULL = 9;//我的团购商品为空

    public static final int sUnpaid = 1;//待付款
    public static final int sUnreceived = 2;//待收货
    public static final int sReceived = 3;//已收货
    public static final int sCancel = 4;//取消订单
    //获取时间戳
    public static final String sTimeStamp = "comm/timestamp";
    //验证短信验证码
    public static final String sCheck = "comm/recode/check";
    //获取注册验证码
    public static final String sGetRegisterCode = "comm/recode/register";
    //获取忘记密码验证码
    public static final String sGetForgetPassCode = "comm/recode/password_forget";
    //获取绑定手机号验证码
    public static final String sGetBindPhoneCode = "comm/recode/register";

    //获取物流公司
    public static final String sGetLogisticsCompany = "conf/express_company";
    //登录
    public static final String sLogin = "user/login";
    //第三方登录
    public static final String sThirdLogin = "user/third_party_login";
    //获取时间戳
    public static final String sGetTimestamp = "comm/timestamp";
    //获签名
    public static final String sGetSign = "comm/generate/sign";
    //提交设备号
    public static final String sPostDeviceToken = "user/device";

    //退出登录
    public static final String sLoginOut = "user/logout";
    //忘记密码
    public static final String sForgetPass = "user/password_forget";
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
    public static final String sMyTb_list = "my/tb";
    //获取banner
    public static final String sGetBanner = "comm/banner";
    //订单
    public static final String sOrder = "order";
    //订单支付
    public static final String sOrderPay = "order/pay";
    //订单数量
    public static final String sOrderNum = "my/order/num";
    //退款订单
    public static final String sRefundOrder = "order/return";
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
    //删除我的足迹
    public static final String sDeleteTravel = "my/travels_delete";
    //获取支付charge
    public static final String sGetPayCharge = "pay";

    //搜索热词
    public static final String sHotKeywords = "goods/search/keywords";


    //我的优惠券
    public static final String sMyCoupon = "my/coupon";
    //激活优惠券
    public static final String sInvokeCoupon = "my/coupon/activate";

    public static final String sVersion = "android/version";

    //我的消息
    public static final String sMyMessage = "my/message";
}
