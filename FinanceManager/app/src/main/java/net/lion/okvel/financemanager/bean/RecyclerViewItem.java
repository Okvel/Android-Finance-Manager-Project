package net.lion.okvel.financemanager.bean;

import android.content.res.Resources;

import net.lion.okvel.financemanager.R;

import java.util.Date;

public class RecyclerViewItem
{
    private String amount;
    private String category;
    private int colorId;
    private Date date;

    public RecyclerViewItem(String amount, String category, Date date)
    {
        this.amount = amount;
        this.category = category;
        colorId = R.color.accent_fm_theme;
        this.date = date;
    }

    public String getAmount()
    {
        return amount;
    }

    public String getCategory()
    {
        return category;
    }

    public int getColorId()
    {
        return colorId;
    }

    public Date getDate()
    {
        return date;
    }
}
