package com.milos.nicke.mojaevidencija.AppHelpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 2/7/2018.
 */

public class DateFormatter {
    public static String getFormmatedDate() {
        Calendar calendar = Calendar.getInstance();

        Date currentLocale = calendar.getTime();

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");

        return date.format(currentLocale);
    }
}
