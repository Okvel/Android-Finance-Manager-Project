package net.lion.okvel.financemanager.bean;

import android.content.res.Resources;

import net.lion.okvel.financemanager.R;

public class DateConverter
{
    public static DateConverter instance = null;

    private Date date;
    private Resources resources;

    public static DateConverter createInstance(Resources resources)
    {
        if (instance == null)
        {
            instance = new DateConverter(resources);
        }

        return instance;
    }

    private DateConverter(Resources resources)
    {
        date = Date.getInstance();
        this.resources = resources;
    }

    public String convert(DateStyle style)
    {
        String result = "";

        if (style.equals(DateStyle.DAY_MONTH_YEAR)) {
            Integer day = date.getDay();
            result += day.toString() + " ";
        }

        if (style.equals(DateStyle.MONTH_YEAR)) {
            switch (date.getMonth()) {
                case 0:
                    result += resources.getString(R.string.january);
                    break;
                case 1:
                    result += resources.getString(R.string.february);
                    break;
                case 2:
                    result += resources.getString(R.string.march);
                    break;
                case 3:
                    result += resources.getString(R.string.april);
                    break;
                case 4:
                    result += resources.getString(R.string.may);
                    break;
                case 5:
                    result += resources.getString(R.string.june);
                    break;
                case 6:
                    result += resources.getString(R.string.july);
                    break;
                case 7:
                    result += resources.getString(R.string.august);
                    break;
                case 8:
                    result += resources.getString(R.string.september);
                    break;
                case 9:
                    result += resources.getString(R.string.october);
                    break;
                case 10:
                    result += resources.getString(R.string.november);
                    break;
                case 11:
                    result += resources.getString(R.string.december);
                    break;
            }

            result += " ";
        }

        Integer year = date.getYear();
        result += year.toString();

        return result;
    }
}
