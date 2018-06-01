package com.desaco.localnetsocketserviceandclient.third_cs.Server;

import android.util.Log;

/**
 * 管理debug输出
 *
 * @author thinkpad
 */
public class ShowLogManager {
    public static boolean outputDebug(String tag, String data) {
        if (Info.SHOW_LOG_FLAG) {
            Log.d(tag, data);
            return true;
        }
        return false;//调试未开启
    }
}
