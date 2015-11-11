package net.lion.okvel.financemanager.database.entry;

import android.provider.BaseColumns;

public abstract class MoneyEntry implements BaseColumns
{
    public static final String TABLE_NAME = "moneyEntry";
    public static final String COLUMN_NAME_ENTRY_ID = "entryID";
    public static final String COLUMN_NAME_AMOUNT = "amount";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_CATEGORY = "category";
}
