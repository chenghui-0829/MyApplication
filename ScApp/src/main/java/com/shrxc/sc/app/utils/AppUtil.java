package com.shrxc.sc.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CH on 2018/8/17.
 */

public class AppUtil {


    /***
     * 判断网络是否连接
     */
    public static boolean IsNetConnect(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void setViewMargin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 日期转星期
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) w = 0;
        return weekDays[w];
    }

    /**
     * 格式化时间
     * <p>
     * dateString 要格式化的字符
     * <p>
     * formatString 格式化的格式
     */
    public static String formatString(String dateString, String formatString) {
        String now = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
            now = new SimpleDateFormat(formatString).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 四舍五入 num 要四舍五入的double，ws保留小数点的位数
     */
    public static String sswrNum(double num, int ws) {
        BigDecimal result = new BigDecimal(num).setScale(ws,
                BigDecimal.ROUND_HALF_UP);
        return result.toString();
    }

    /**
     * 获取app版本号
     */
    public static String getVersion(Context context) {
        PackageManager manager;
        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // info.versionCode;
        //
        // info.packageName;
        //
        // info.signatures;

        return info.versionName;

    }


    /***
     * 判断字符是否为手机号
     */
    public static boolean isMobile(String mobile) {

        mobile = mobile.replaceAll(" ", "");

        if (mobile.contains("-")) {
            mobile = mobile.replaceAll("-", "");
        }

        if (mobile.startsWith("+86")) {
            mobile = mobile.replace("+86", "");
        }
        Pattern pattern = Pattern.compile("[1][34578]\\d{9}");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
}
