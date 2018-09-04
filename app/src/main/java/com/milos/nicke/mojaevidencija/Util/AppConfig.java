package com.milos.nicke.mojaevidencija.Util;

import android.content.Context;

/**
 * Created on 3/10/2018.
 */

public class AppConfig {

    public static final String SUGAR_WHERE_NAME = "name = ?";

    public static String getSystemString(Context context, int string) {
        return context.getResources().getString(string);
    }
}
