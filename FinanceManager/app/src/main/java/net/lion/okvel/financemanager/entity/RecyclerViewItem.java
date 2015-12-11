package net.lion.okvel.financemanager.entity;

import net.lion.okvel.financemanager.R;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

public class RecyclerViewItem {
    private long id;
    private BigDecimal amount;
    private Currency currency;
    private String type;
    private String category;
    private int imageId;
    private GregorianCalendar date;

    public RecyclerViewItem(long id, BigDecimal amount, Currency currency, String type, String category,
                            GregorianCalendar date) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.category = category;
        imageId = checkColorId();
        this.date = date;
    }

    private int checkColorId() {
        int result = 0;

        switch (AmountType.valueOf(type)) {
            case INCOMES:
                result = R.drawable.vector_plus_circle;
                break;
            case EXPENSES:
                result = R.drawable.vector_minus_circle;
        }

        return result;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public int getImageId() {
        return imageId;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        imageId = checkColorId();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }
}
