package com.sprint.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimestampUtil {

    public static Timestamp toTimestampDatetime(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString, e);
        }
    }

    public static String toStringDatetime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }

    public static Timestamp toTimestampTime(String timeString) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            java.util.Date parsedTime = timeFormat.parse(timeString);
            return new Timestamp(parsedTime.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString, e);
        }
    }

    public static String toStringTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(timestamp);
    }

}
