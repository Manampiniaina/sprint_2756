package com.sprint.utils;

import java.text.SimpleDateFormat;
import java.sql.Time;
import java.text.ParseException;

public class TimeUtil {
    public static String toString(Time time) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(time);
    }
    public static Time toTime(String timeString) throws ParseException {
        if (timeString == null || timeString.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long millis = sdf.parse(timeString).getTime();
        return new Time(millis);
    }
}
