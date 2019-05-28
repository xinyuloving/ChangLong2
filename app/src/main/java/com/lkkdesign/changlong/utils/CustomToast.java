package com.lkkdesign.changlong.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ---------------------------------------------------------------------
 * Copyright © 2018-2020, 深圳洛可可设计工业有限公司, All rights reserved.
 * ---------------------------------------------------------------------
 *
 * @File: CustomToast.java
 * @Author: admin
 * @Version: V1.0
 * @Create: 2018/2/23 10:21
 * @Description: //自定义Toast
 * <p>
 * ---------------------------------------------------------------------
 * @Project: ZuoYuoWeiLai
 * ---------------------------------------------------------------------
 */

public class CustomToast {
    private static Context context = null;
    private static Toast toast = null;

    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showLongToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
