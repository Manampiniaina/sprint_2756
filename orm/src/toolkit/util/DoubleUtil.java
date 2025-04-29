package toolkit.util;

import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleUtil {
	public static String toString(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("0");
        return df.format(bd);
    }
}
