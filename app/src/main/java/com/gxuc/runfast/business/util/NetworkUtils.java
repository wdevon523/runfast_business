package com.gxuc.runfast.business.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 * Created by Berial on 2016/11/2.
 */

public final class NetworkUtils {

    private NetworkUtils() {}

    /**
     * 检查网络是否可用
     */
    public static boolean isAvailable(Context context) {
        if (context == null) return true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }
}
