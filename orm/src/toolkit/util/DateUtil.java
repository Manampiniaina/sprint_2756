package toolkit.util;

import java.sql.Date;
import java.time.LocalDate;

public class DateUtil {
    public static Date toDate(String dateString) {
        String[] parses=dateString.split("-");
        int year = Integer.parseInt(parses[0]) ;
        int month = Integer.parseInt(parses[1]);
        int day = Integer.parseInt(parses[2]);
        LocalDate ld= LocalDate.of(year, month, day);
        return Date.valueOf(ld);
    }

    public static String toString(Date date) {
       return date.toString();
    }
}
