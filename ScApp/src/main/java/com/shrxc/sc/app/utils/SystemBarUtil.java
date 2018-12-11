package com.shrxc.sc.app.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


/***
 * 系统栏和状态栏一体化
 */
public class SystemBarUtil {

    public static void SetStatusColor(Activity activity, int color) {
        // 设置状态栏一体化
        SystemBarUtil.Setstatus(activity);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(color);
    }

    /***
     * 状态栏一体化的方法
     */
    private static void Setstatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
