package com.sensu.android.zimaogou.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by winter on 2015/9/22.
 * 需要权限 android.permission.ACCESS_NETWORK_STATE
 * @author winter
 */
public class NetworkTypeUtils {

    /** 无网络 */
    public static final int NETWORK_INVALID = -1;
    /** 2G网络 */
    public static final int NETWORK_2G = 0;
    /** wap网络 */
    public static final int NETWORK_WAP = 1;
    /** wifi网络 */
    public static final int NETWORK_WIFI = 2;
    /** 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORK_3G = 3;

    private static final int[] NETWORK_MATCH_TABLE = {
            NETWORK_2G // NETWORK_TYPE_UNKNOWN
            , NETWORK_2G // NETWORK_TYPE_GPRS
            , NETWORK_2G // NETWORK_TYPE_EDGE
            , NETWORK_3G // NETWORK_TYPE_UMTS
            , NETWORK_2G // NETWORK_TYPE_CDMA
            , NETWORK_3G // NETWORK_TYPE_EVDO_O
            , NETWORK_3G // NETWORK_TYPE_EVDO_A
            , NETWORK_2G // NETWORK_TYPE_1xRTT
            , NETWORK_3G // NETWORK_TYPE_HSDPA
            , NETWORK_3G // NETWORK_TYPE_HSUPA
            , NETWORK_3G // NETWORK_TYPE_HSPA
            , NETWORK_2G // NETWORK_TYPE_IDEN
            , NETWORK_3G // NETWORK_TYPE_EVDO_B
            , NETWORK_3G // NETWORK_TYPE_LTE
            , NETWORK_3G // NETWORK_TYPE_EHRPD
            , NETWORK_3G // NETWORK_TYPE_HSPAP
    };

    private static final String [] NETWORK_TYPE_TABLE = new String[] {
            "NETWORK_TYPE_UNKNOWN"
            , "NETWORK_TYPE_GPRS"
            , "NETWORK_TYPE_EDGE"
            , "NETWORK_TYPE_UMTS"
            , "NETWORK_TYPE_CDMA"
            , "NETWORK_TYPE_EVDO_O"
            , "NETWORK_TYPE_EVDO_A"
            , "NETWORK_TYPE_1xRTT"
            , "NETWORK_TYPE_HSDPA"
            , "NETWORK_TYPE_HSUPA"
            , "NETWORK_TYPE_HSPA"
            , "NETWORK_TYPE_IDEN"
            , "NETWORK_TYPE_EVDO_B"
            , "NETWORK_TYPE_LTE"
            , "NETWORK_TYPE_EHRPD"
            , "NETWORK_TYPE_HSPAP"
    };

    private static int mDefaultNetworkType;
    private static String mDefaultNetworkTypeStr;
    private static ConnectivityManager mConnectManager;

    /**
     * 初始化默认网络参数
     * @param context 上下文环境
     */
    public static void init(Context context) {
        // 手机2G 还是 3G
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        if (networkType < 0 || networkType >= NETWORK_MATCH_TABLE.length) {
            networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
        }
        mDefaultNetworkType = NETWORK_MATCH_TABLE[networkType];
        mDefaultNetworkTypeStr = NETWORK_TYPE_TABLE[networkType];

        mConnectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取网络类型
     * @return 网络类型
     */
    public static String getTypeStr() {
        return mDefaultNetworkTypeStr;
    }

    /**
     * 获取网络类型
     * @return 网络类型
     */
    public static int getType() {
        int networkType = mDefaultNetworkType;

        NetworkInfo networkInfo = mConnectManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            networkType = NETWORK_INVALID;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            networkType = NETWORK_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                && !TextUtils.isEmpty(android.net.Proxy.getDefaultHost())) {
            networkType = NETWORK_WAP;
        }
        return networkType;
    }

    /**
     * 是否存在有效的网络连接.
     * @return 存在有效的网络连接返回true，否则返回false
     */
    public static boolean isNetWorkAvailable() {
        NetworkInfo networkInfo = mConnectManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 获取本机IPv4地址
     * @return 本机IPv4地址
     */
    public static String ipv4() {
        return ipAddress(true);
    }

    /**
     * 获取本机IPv6地址
     * @return 本机IPv6地址
     */
    public static String ipv6() {
        return ipAddress(false);
    }

    private static String ipAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface netInterface = en.nextElement();
                for (Enumeration<InetAddress> iNetEnum = netInterface.getInetAddresses(); iNetEnum.hasMoreElements();) {
                    InetAddress inetAddress = iNetEnum.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String address = inetAddress.getHostAddress();
                        if (useIPv4 == InetAddressUtils.isIPv4Address(address)) {
                            return address;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
