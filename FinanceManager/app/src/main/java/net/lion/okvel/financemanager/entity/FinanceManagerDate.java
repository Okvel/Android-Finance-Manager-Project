package net.lion.okvel.financemanager.entity;

import java.util.Calendar;

public class FinanceManagerDate
{
    private static FinanceManagerDate instance = null;

    private int year;
    private int month;
    private int day;
    private DateStyle dateStyle;

    private FinanceManagerDate()
    {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public synchronized static FinanceManagerDate getInstance()
    {
        if (instance == null) {
            instance = new FinanceManagerDate();
        }

        return instance;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getYear()
    {
        return year;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getMonth()
    {
        return month;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getDay()
    {
        return day;
    }

    public void setDateStyle(DateStyle style)
    {
        dateStyle = style;
    }

    public DateStyle getDateStyle()
    {
        return dateStyle;
    }

    public void setCurrentDate()
    {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
