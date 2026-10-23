package com.example.smartpantrymanager.util;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final long DAY_MS = 24L * 60 * 60 * 1000;

    public static String formatDate(long millis) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(millis));
    }

    /** Whole days from today (midnight) to the expiry day. Negative = already expired. */
    public static long daysUntil(long expiryMillis) {
        return (startOfDay(expiryMillis) - startOfDay(System.currentTimeMillis())) / DAY_MS;
    }

    public static String getExpiryStatus(long expiryMillis) {
        long days = daysUntil(expiryMillis);
        if (days < 0) {
            long ago = -days;
            return "Expired " + ago + (ago == 1 ? " day ago" : " days ago");
        } else if (days == 0) {
            return "Expires today";
        } else if (days == 1) {
            return "Expires tomorrow";
        } else {
            return "Expires in " + days + " days";
        }
    }

    public static int getExpiryColor(long expiryMillis) {
        long days = daysUntil(expiryMillis);
        if (days < 0) return Color.parseColor("#D32F2F");   // red
        if (days <= 3) return Color.parseColor("#F57C00");  // orange
        return Color.parseColor("#388E3C");                 // green
    }

    private static long startOfDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
}
