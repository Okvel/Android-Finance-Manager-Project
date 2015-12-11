package net.lion.okvel.financemanager.database.entry;

import android.provider.BaseColumns;

public abstract class MoneyEntry implements BaseColumns
{
    public static final String TABLE_NAME = "MONEY";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_TYPE = "TYPE";
    public static final String COLUMN_NAME_DAY = "DAY";
    public static final String COLUMN_NAME_MONTH = "MONTH";
    public static final String COLUMN_NAME_YEAR = "YEAR";
    public static final String COLUMN_NAME_CATEGORY = "CATEGORY";
}
