package net.lion.okvel.financemanager.database.contract;

import android.provider.BaseColumns;

public final class MoneyContract
{
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE" + MoneyEntry.TABLE_NAME + "(" +
            MoneyEntry._ID + "INTEGER PRIMARY KEY," + MoneyEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE +
            COMMA_SEP + MoneyEntry.COLUMN_NAME_AMOUNT + TEXT_TYPE + COMMA_SEP +
            MoneyEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP + MoneyEntry.COLUMN_NAME_CATEGORY +
            TEXT_TYPE + COMMA_SEP + ")";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MoneyEntry.TABLE_NAME;

    public static abstract class MoneyEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "moneyEntry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryID";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}
