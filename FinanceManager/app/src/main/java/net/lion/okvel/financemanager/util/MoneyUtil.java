package net.lion.okvel.financemanager.util;

public class MoneyUtil {
    private MoneyUtil() {

    }

    public static String moneyToString(String totalMoney) {
        String result = totalMoney;
        String[] moneyParts = result.split("\\.");

        if (moneyParts[0].length() > 3) {
            int count = moneyParts[0].length() / 3;
            if (moneyParts[0].length() % 3 == 0) {
                count--;
            }
            StringBuilder builder = new StringBuilder(moneyParts[0].substring(0, moneyParts[0].length() - 3 * count));
            builder.append(" ");
            for (int i = count; i > 0; i--) {
                builder.append(moneyParts[0].substring(moneyParts[0].length() - 3 * i,
                        moneyParts[0].length() - 3 * (i - 1)));
                if (i != 1) {
                    builder.append(" ");
                }
            }
            if (moneyParts.length > 1) {
                builder.append(".")
                        .append(moneyParts[1]);
            } else if (".".equals(totalMoney.substring(totalMoney.length() - 1))) {
                builder.append(".");
            }
            result = builder.toString();
        }

        return result;
    }

    public static String trim(String s) {
        String[] strings = s.split("\\.");
        StringBuilder builder = new StringBuilder(strings[0]);
        if (strings.length > 1) {
            builder.append(".");
            if (strings[1].length() > 2) {
                builder.append(strings[1].substring(0, 2));
            } else {
                builder.append(strings[1])
                        .append("0");
            }
        }

        return builder.toString();
    }
}
