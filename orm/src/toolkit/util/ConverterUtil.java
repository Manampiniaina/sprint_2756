package toolkit.util;

import java.sql.Timestamp;
import java.sql.Time;

public class ConverterUtil {

    public static String toString(Object obj) {
        if(obj instanceof Double){
            return DoubleUtil.toString((Double)obj);
        }
        else if(obj instanceof Timestamp){
            return TimestampUtil.toStringDatetime((Timestamp)obj);
        }
        else if(obj instanceof Time){
            return TimeUtil.toString((Time)obj);
        }
        
        return obj == null ? null : obj.toString();
    }

}
