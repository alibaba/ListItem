package com.taobao.listitem.core.v2;

import android.util.Log;

/**
 * Created by fatian on 17/3/18.
 */
public class LogUtil {

    public static final boolean isDebug = true;

    public static void e(String tag, Throwable msg) {
        if (isDebug) {
            Log.e(tag, msg.toString());
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
}
