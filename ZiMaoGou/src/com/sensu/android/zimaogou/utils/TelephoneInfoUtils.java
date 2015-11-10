package com.sensu.android.zimaogou.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by winter on 2015/9/22.
 * <p/>
 * 读取手机信息
 * 需要加入权限 android.permission.READ_PHONE_STATE
 *
 * @author winter
 */
public class TelephoneInfoUtils {
    /**
     * 获取手机唯一标识码, *#06# 可以查询
     *
     * @return
     */
    public static String getIMEI() {
        return mIMEI;
    }

    /**
     * 获取唯一标识一张卡片的物理号码
     *
     * @return
     */
    public static String getICCID() {
        return mICCID;
    }

    /**
     * 获取国际移动用户号码标识，前5位为sim卡运营商信息
     *
     * @return
     */
    public static String getIMSI() {
        return mIMSI;
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 获取当前设置的电话号码,很可能为空
     */
    public static String getPhoneNumber() {
        return mPhoneNumber;
    }

    /**
     * 判断手机号 使用正则表达式判断下述手机字段
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     * <p/>
     * 13(0 - 9)
     * 14(0 - 9)
     * 15(0 - 9)
     * 17(0 - 9)
     * 18(0 - 9)
     */
    public static boolean isMobileNum(String str) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15([0-9]))|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 中国联通
     * <p/>
     * 获取手机服务商 <br>
     */
    public static String getProvidersName() {
        return mProviderName;
    }

    /**
     * China Unicom
     * <p/>
     * 按照字母次序的current registered operator(当前已注册的用户)的名字<br/>
     * 注意：仅当用户已在网络注册时有效。<br/>
     * 在CDMA网络中结果也许不可靠。
     *
     * @return
     */
    public static String getNetworkOperatorName() {
        return mNetworkOperatorName;
    }

    /**
     * 获取sim卡的状态：
     * TelephonyManager.SIM_STATE_READY（良好）、
     * TelephonyManager.SIM_STATE_ABSENT（无SIM卡）、
     * 其他情况
     *
     * @return SimState
     */
    public static int getSimState() {
        return mSimState;
    }

    /**
     * 电话状态：<br/>
     * CALL_STATE_IDLE 无任何状态时<br/>
     * CALL_STATE_OFFHOOK 接起电话时<br/>
     * CALL_STATE_RINGING 电话进来时
     *
     * @param context context
     * @return
     */
    public static int getCallState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getCallState();
    }

    private static int mSimState;
    private static String mNetworkCountryIso;
    private static String mNetworkOperator;
    private static String mNetworkOperatorName;
    private static int mPhoneType;
    private static String mPhoneNumber;
    private static String mSimOperator;
    private static String mProviderName;
    private static String mSimOperatorName;
    private static String mIMEI;
    private static String mICCID;
    private static String mIMSI;
    private static String mSecondIMSI;
    private static String mSecondProviderName;

    private static String mModel;
    private static String mDevice;
    private static String mDeviceVersion;

    public static void init(Context context) {
        /**
         * TelephonyManager提供设备上获取通讯服务信息的入口。
         * 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。
         * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
         * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。
         */
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        mSimState = telephonyManager.getSimState();
        mNetworkCountryIso = telephonyManager.getNetworkCountryIso();
        mNetworkOperator = telephonyManager.getNetworkOperator();
        mNetworkOperatorName = telephonyManager.getNetworkOperatorName();
        mPhoneType = telephonyManager.getPhoneType();

        // 取出MSISDN，很可能为空(mobile subscriber ISDN用户号码，这个是我们说的139，136那个号码)
        mPhoneNumber = telephonyManager.getLine1Number();
        if (mPhoneNumber != null) {
            if (mPhoneNumber.startsWith("+86")) {
                mPhoneNumber = mPhoneNumber.substring(3);
            }
            if (mPhoneNumber.startsWith("86")) {
                mPhoneNumber = mPhoneNumber.substring(2);
            }
        }

        //sim卡运营商, 前面3位460是国家，后面2位00 02是中国移动，01是中国联通（MCC + 跨国公司的注册网络运营商）。
        mSimOperator = telephonyManager.getSimOperator();
        mProviderName = getProviderName(mSimOperator);

        mSimOperatorName = telephonyManager.getSimOperatorName();

        //取出IMEI(international mobile Equipment identity手机唯一标识码, *#06# 可以查询)
        mIMEI = telephonyManager.getDeviceId();

        //取出ICCID(ICC identity集成电路卡标识，这个是唯一标识一张卡片物理号码的)
        mICCID = telephonyManager.getSimSerialNumber();

        //取出IMSI(international mobiles subscriber identity国际移动用户号码标识，前5位为sim卡运营商信息)
        //IMSI共有15位，其结构如下： MCC+MNC+MIN MCC：Mobile Country Code，移动国家码，共3位，中国为460;
        //MNC:Mobile Network Code，移动网络码，共2位，电信03，移动02，联通GSM 01
        //MIN共有10位，其结构如下： 09+M0M1M2M3+ABCD 其中的M0M1M2M3和MDN号码中的H0H1H2H3可存在对应关系，ABCD四位为自由分配
        mIMSI = telephonyManager.getSubscriberId();

        getSecondSimInfo(telephonyManager, context);

        mModel = Build.MODEL;
        mDevice = Build.DEVICE;
        String fingerprint = Build.FINGERPRINT;
        int start = fingerprint.indexOf(mDevice + ":");
        if (start > 0) {
            int end = fingerprint.indexOf("/", start);
            if (end > 0) {
                mDeviceVersion = fingerprint.substring(start + mDevice.length() + 1, end);
            }
        }
        if (mDeviceVersion == null) {
            mDeviceVersion = fingerprint;
        }
    }

    private static String getProviderName(String simOperator) {
        String providerName;
        if (simOperator.startsWith("46000") || simOperator.startsWith("46002") || simOperator.startsWith("46007")) {
            providerName = "中国移动";
        } else if (simOperator.startsWith("46001")) {
            providerName = "中国联通";
        } else if (simOperator.startsWith("46003")) {
            providerName = "中国电信";
        } else {
            providerName = "";
        }
        return providerName;
    }

    private static void getSecondSimInfo(TelephonyManager tm, Context context) {
        // 一般放在卡1的SIM卡 普通方法即可获取
        // 卡2的SIM卡 一般需利用反射来获取
        // 高通手机没有亲测
        try {
            Class<?>[] resources = new Class<?>[]{int.class};
            Integer resourcesId = 1;
            if (mSecondIMSI == null || "".equals(mSecondIMSI)) {
                try {   //利用反射获取    MTK手机
                    Method addMethod = tm.getClass().getDeclaredMethod("getSubscriberIdGemini", resources);
                    addMethod.setAccessible(true);
                    mSecondIMSI = (String) addMethod.invoke(tm, resourcesId);
                } catch (Exception e) {
                    mSecondIMSI = null;
                }
            }
            if (mSecondIMSI == null || "".equals(mSecondIMSI)) {
                try {   //利用反射获取    展讯手机
                    Class<?> c = Class.forName("com.android.internal.telephony.PhoneFactory");
                    Method m = c.getMethod("getServiceName", String.class, int.class);
                    String spreadTmService = (String) m.invoke(c, Context.TELEPHONY_SERVICE, 1);
                    TelephonyManager tm1 = (TelephonyManager) context.getSystemService(spreadTmService);
                    mSecondIMSI = tm1.getSubscriberId();
                } catch (Exception e) {
                    mSecondIMSI = null;
                }
            }
            if (mSecondIMSI == null || "".equals(mSecondIMSI)) {
                try {   //利用反射获取    高通手机
                    Method addMethod2 = tm.getClass().getDeclaredMethod("getSimSerialNumber", resources);
                    addMethod2.setAccessible(true);
                    mSecondIMSI = (String) addMethod2.invoke(tm, resourcesId);
                } catch (Exception e) {
                    mSecondIMSI = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(mSecondIMSI)) {
            mSecondProviderName = getProviderName(mSecondIMSI);
        }
    }

    /**
     * cn
     * <p/>
     * 获取ISO标准的国家码，即国际长途区号。<br/>
     * 注意：仅当用户已在网络注册后有效。<br/>
     * 在CDMA网络中结果也许不可靠。<br/>
     *
     * @return
     */
    public static String getNetworkCountryIso() {
        return mNetworkCountryIso;
    }

    /**
     * 46001 -> (460 + 01)
     * <p/>
     * MCC+MNC(mobile country code + mobile network code)<br/>
     * 注意：仅当用户已在网络注册时有效。<br/>
     * 在CDMA网络中结果也许不可靠。<br/>
     *
     * @return
     */
    public static String getNetworkOperator() {
        return mNetworkOperator;
    }

    /**
     * GSM
     * <p/>
     * 返回移动终端的类型：<br/>
     * PHONE_TYPE_CDMA 手机制式为CDMA，电信<br/>
     * PHONE_TYPE_GSM 手机制式为GSM，移动和联通<br/>
     * PHONE_TYPE_NONE 手机制式未知<br/>
     *
     * @return
     */
    public static int getPhoneType() {
        return mPhoneType;
    }

    /**
     * 46001
     * <p/>
     * 手机服务商代号
     *
     * @return
     */
    public static String getSimOperator() {
        return mSimOperator;
    }

    /**
     * 服务商名称：<br/>
     * 例如：中国移动、联通<br/>
     * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @return
     */
    public static String getSimOperatorName() {
        return mSimOperatorName;
    }

    /**
     * 获取手机副卡的国际移动用户号码标识，前5位为sim卡运营商信息
     *
     * @return
     */
    public static String getSecondIMSI() {
        return mSecondIMSI;
    }

    /**
     * 获取手机服务商(副卡)
     *
     * @return
     */
    public static String getSecondProviderName() {
        return mSecondProviderName;
    }

    /**
     * 当前使用的网络类型：<br/>
     * NETWORK_TYPE_UNKNOWN 网络类型未知 0<br/>
     * NETWORK_TYPE_GPRS GPRS网络 1<br/>
     * NETWORK_TYPE_EDGE EDGE网络 2<br/>
     * NETWORK_TYPE_UMTS UMTS网络 3<br/>
     * NETWORK_TYPE_HSDPA HSDPA网络 8<br/>
     * NETWORK_TYPE_HSUPA HSUPA网络 9<br/>
     * NETWORK_TYPE_HSPA HSPA网络 10<br/>
     * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4<br/>
     * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5<br/>
     * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6<br/>
     * NETWORK_TYPE_1xRTT 1xRTT网络 7<br/>
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO<br/>
     *
     * @param context context
     * @return
     */
    public static int getNetworkType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkType();
    }

    /**
     * 是否漫游:(在GSM用途下)
     *
     * @param context context
     * @return
     */
    public static boolean isNetworkRoaming(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.isNetworkRoaming();
    }

    /**
     * 获取数据活动状态<br/>
     * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据<br/>
     * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据<br/>
     * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据<br/>
     * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受<br/>
     *
     * @param context context
     * @return
     */
    public static int getDataActivity(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDataActivity();
    }

    /**
     * 获取数据连接状态<br/>
     * DATA_CONNECTED 数据连接状态：已连接<br/>
     * DATA_CONNECTING 数据连接状态：正在连接<br/>
     * DATA_DISCONNECTED 数据连接状态：断开<br/>
     * DATA_SUSPENDED 数据连接状态：暂停<br/>
     *
     * @param context context
     * @return
     */
    public static int getDataState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDataState();
    }

    /**
     * 设备名称 mi 3 , HUAWEI MT7-TL00
     *
     * @return mi 3
     */
    public static String getDeviceName() {
        return mModel;
    }

    /**
     * pisces  hwmt7
     *
     * @return pisces
     */
    public static String getDevice() {
        return mDevice;
    }

    /**
     * 4.4.4  Xiaomi/pisces/pisces:4.4.4/KTU84P/V6.1.2.0.KXCCNBJ:user/release-keys
     *
     * @return 4.4.4
     */
    public static String getDeviceVersion() {
        return mDeviceVersion;
    }

    public static boolean isSamSung() {
        String board = Build.BRAND.toLowerCase();
        return TextUtils.equals("samsung", board);
    }
}

