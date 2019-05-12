package com.example.compresomieda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utilidades para fecha y tiempo
 */

public class DateTimeUtils {

    private static final String UI_DATE_PATTERN = "dd 'de' MMMM 'del' yyyy";
    private static final String API_DATE_PATTERN = "yyyy-MM-dd";
    private static final String UI_TIME_PATTERN = "h:mma";
    private static final String API_TIME_PATTERN = "HH:mm:ss";
    private static final String API_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String FILE_NAME_PATTERN = "yyyyMMdd_HHmmss";

    private DateTimeUtils() {
    }

    public static Date getCurrentDate() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }

    public static String formatDateForUi(int year, int month, int dayOfMonth) {
        return formatDateForUi(createDate(year, month, dayOfMonth));
    }

    public static String formatDateForUi(Date date) {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat(UI_DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date createDate(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String formatDateForApi(Date date) {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat(API_DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String formatTimeForUi(String time) {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat(API_TIME_PATTERN, Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(UI_TIME_PATTERN);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Date parseUiTime(String timeSchedule) {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat(UI_TIME_PATTERN, Locale.getDefault());
        try {
            return simpleDateFormat.parse(timeSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String joinDateTime(Date datePicked, Date timeUi) {
        Calendar datePickedCal = Calendar.getInstance();
        Calendar timeUiCal = Calendar.getInstance();

        datePickedCal.setTime(datePicked);
        timeUiCal.setTime(timeUi);

        datePickedCal.add(Calendar.HOUR_OF_DAY, timeUiCal.get(Calendar.HOUR_OF_DAY));
        datePickedCal.add(Calendar.MINUTE, timeUiCal.get(Calendar.MINUTE));

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(API_DATETIME_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(datePickedCal.getTime());
    }

    public static String formatDateForFileName(Date date) {
        SimpleDateFormat sdf
                = new SimpleDateFormat(FILE_NAME_PATTERN, Locale.getDefault());
        return sdf.format(date);
    }
}
