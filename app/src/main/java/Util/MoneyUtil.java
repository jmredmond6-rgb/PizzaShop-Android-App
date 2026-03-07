
package Util;

import java.util.Locale;

/* Used to format the money values stored as dollars . */
public class MoneyUtil {

    public static String dollars(double amount) {
        return String.format(Locale.US, "$%.2f", amount);
    }


}
