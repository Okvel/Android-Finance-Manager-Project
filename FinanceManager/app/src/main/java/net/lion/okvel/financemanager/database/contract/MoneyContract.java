package net.lion.okvel.financemanager.database.contract;

import net.lion.okvel.financemanager.database.entry.MoneyEntry;

public final class MoneyContract
{
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MoneyEntry.TABLE_NAME + " (" +
            MoneyEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," /*+ MoneyEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE +
            COMMA_SEP*/ + MoneyEntry.COLUMN_NAME_AMOUNT + INTEGER_TYPE + COMMA_SEP +
            MoneyEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP + MoneyEntry.COLUMN_NAME_DAY +
            INTEGER_TYPE + COMMA_SEP + MoneyEntry.COLUMN_NAME_MONTH + INTEGER_TYPE + COMMA_SEP +
            MoneyEntry.COLUMN_NAME_YEAR + INTEGER_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MoneyEntry.TABLE_NAME;
}
