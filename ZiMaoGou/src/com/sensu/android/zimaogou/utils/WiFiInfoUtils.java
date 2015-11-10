package com.sensu.android.zimaogou.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by winter on 2015/9/22.
 * 需要权限 android.permission.ACCESS_WIFI_STATE
 *
 * @author winter
 */
public class WiFiInfoUtils {

    private static String mWifiMac;

    public static void init(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiMac = wifiManager.getConnectionInfo().getMacAddress();
        if (mWifiMac == null) {
            mWifiMac = "";
        }
    }

    public static String getWifiMac() {
        return mWifiMac;
    }

    private static final int IP_ADDRESS_MASK = 0xFF;
    private static final int SECOND_BYTE_SHIFT = 8;
    private static final int THIRD_BYTE_SHIFT = 16;
    private static final int FOURTH_BYTE_SHIFT = 24;

    public static String getLocalWifiIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if (0 == ipAddress) {
            return null;
        }
        String ip = (ipAddress & IP_ADDRESS_MASK)
                + "." + ((ipAddress >> SECOND_BYTE_SHIFT) & IP_ADDRESS_MASK)
                + "." + ((ipAddress >> THIRD_BYTE_SHIFT) & IP_ADDRESS_MASK)
                + "." + ((ipAddress >> FOURTH_BYTE_SHIFT) & IP_ADDRESS_MASK);
        return ip;
    }
}
