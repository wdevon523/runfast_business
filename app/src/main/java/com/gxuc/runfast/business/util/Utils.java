package com.gxuc.runfast.business.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static String nullToValue(String text, String value) {
        return text == null ? value : text;
    }

    public static String emptyToValue(String text, String value) {
        return TextUtils.isEmpty(text) ? value : text;
    }

    public static String formatFloorNumber(double number, int digit, boolean auto) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(digit);
        format.setMinimumFractionDigits(auto ? 0 : digit);
        format.setRoundingMode(RoundingMode.FLOOR);
        return format.format(number);
    }

    public static String formatFloorNumber(double number, int digit) {
        return formatFloorNumber(number, digit, false);
    }

    public static float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取应用当前版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.d(e);
        }
        return 0;
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        return format.format(new Date());
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
        String filePath = "";
        String[] filePathColumn = { MediaStore.MediaColumns.DATA };

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        return filePath;
    }
}
