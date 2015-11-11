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

        if (style.equals(DateStyle.DAY_MONTH_YEAR) || style.equals(DateStyle.MONTH_YEAR)) {
            result += convertMonth(date.getMonth()) + " ";
        }

        Integer year = date.getYear();
        result += year.toString();

        return result;
    }

    public String convert(DateStyle style, Integer year, Integer month, Integer day)
    {
        String result = "";

        if (style.equals(DateStyle.DAY_MONTH_YEAR)) {
            result += day.toString() + " ";
        }

        if (style.equals(DateStyle.DAY_MONTH_YEAR) || style.equals(DateStyle.MONTH_YEAR)) {
            result += convertMonth(month) + " ";
        }

        result += year.toString();

        return result;
    }

    private String convertMonth(int month)
    {
        switch (month) {
            case 0:
                return resources.getString(R.string.january);
            case 1:
                return resources.getString(R.string.february);
            case 2:
                return resources.getString(R.string.march);
            case 3:
                return resources.getString(R.string.april);
            case 4:
                return resources.getString(R.string.may);
            case 5:
                return resources.getString(R.string.june);
            case 6:
                return resources.getString(R.string.july);
            case 7:
                return resources.getString(R.string.august);
            case 8:
                return resources.getString(R.string.september);
            case 9:
                return resources.getString(R.string.october);
            case 10:
                return resources.getString(R.string.november);
            default:
                return resources.getString(R.string.december);
        }
    }
}
