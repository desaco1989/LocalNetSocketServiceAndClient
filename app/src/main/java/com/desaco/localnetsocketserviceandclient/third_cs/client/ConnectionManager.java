package com.desaco.localnetsocketserviceandclient.third_cs.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络管理类
 *
 * @author thinkpad
 */
public class ConnectionManager {

    public static boolean hasActivityConn(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return true;
    }

    /**
     * 获得本地IP
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalIP() throws SocketException {
        for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
                .getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
            NetworkInterface intf = mEnumeration.nextElement();
            for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr
                    .hasMoreElements(); ) {
                InetAddress inetAddress = enumIPAddr.nextElement();
                // 如果不是回环地址
                if (!inetAddress.isLoopbackAddress()
                        && !inetAddress.isLinkLocalAddress()) {
                    // 直接返回本地IP地址
                    return inetAddress.getHostAddress().toString();
                }
            }
        }
        return "";
    }

    /**
     * 获得网络制式
     *
     * @return
     */
    public static int getIPType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            // "网络类型：WIFI";
            return Info.NETWORK_TYPE_WIFI;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            // "网络类型：手机";
            return Info.NETWORK_TYPE_PHONE;
        } else {
            return Info.NETWORK_TYPE_OTHER;
        }
    }
}
