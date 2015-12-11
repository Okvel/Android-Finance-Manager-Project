package net.lion.okvel.financemanager.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.database.entry.MoneyEntry;
import net.lion.okvel.financemanager.entity.AmountType;
import net.lion.okvel.financemanager.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity {
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initDatabase();
        initToolbar();
        initContext();
    }

    private void initDatabase() {
        database = SQLiteDatabase.openDatabase(getIntent().getStringExtra("database"),
                null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.statistics_toolbar);
        toolbar.setTitle(getString(R.string.statistics_title));
    }

    private void initContext() {
        TextView currentMonthIncomes = (TextView) findViewById(R.id.current_month_incomes);
        TextView currentMonthExpenses = (TextView) findViewById(R.id.current_month_expenses);
        TextView lastMonthIncomes = (TextView) findViewById(R.id.last_month_incomes);
        TextView lastMonthExpenses = (TextView) findViewById(R.id.last_month_expenses);
        TextView currentYearIncomes = (TextView) findViewById(R.id.current_year_incomes);
        TextView currentYearExpenses = (TextView) findViewById(R.id.current_year_expenses);
        TextView lastYearIncomes = (TextView) findViewById(R.id.last_year_incomes);
        TextView lastYearExpenses = (TextView) findViewById(R.id.last_year_expenses);
        TextView totalIncomes = (TextView) findViewById(R.id.total_incomes);
        TextView totalExpenses = (TextView) findViewById(R.id.total_expenses);
        String selection;
        String[] selectionArgs;
        Calendar calendar = Calendar.getInstance();

        selection = MoneyEntry.COLUMN_NAME_YEAR + "=? AND " +
                MoneyEntry.COLUMN_NAME_MONTH + "=? AND " + MoneyEntry.COLUMN_NAME_TYPE + "=?";
        selectionArgs = new String[] {
                String.valueOf(calendar.get(Calendar.YEAR)),
                String.valueOf(calendar.get(Calendar.MONTH)),
                AmountType.INCOMES.toString()
        };
        currentMonthIncomes.setText(createText(selection, selectionArgs));
        selectionArgs[2] = AmountType.EXPENSES.toString();
        currentMonthExpenses.setText(createText(selection, selectionArgs));

        if (!selectionArgs[1].equals("0")) {
            selectionArgs[1] = String.valueOf(calendar.get(Calendar.MONTH) - 1);
        } else {
            selectionArgs[1] = String.valueOf(11);
        }
        lastMonthExpenses.setText(createText(selection, selectionArgs));
        selectionArgs[2] = AmountType.INCOMES.toString();
        lastMonthIncomes.setText(createText(selection, selectionArgs));

        selection = MoneyEntry.COLUMN_NAME_YEAR + "=? AND " + MoneyEntry.COLUMN_NAME_TYPE + "=?";
        selectionArgs = new String[] {
                String.valueOf(calendar.get(Calendar.YEAR)),
                AmountType.INCOMES.toString()
        };
        currentYearIncomes.setText(createText(selection, selectionArgs));
        selectionArgs[1] = AmountType.EXPENSES.toString();
        currentYearExpenses.setText(createText(selection, selectionArgs));

        selectionArgs[0] = String.valueOf(Integer.parseInt(selectionArgs[0]) - 1);
        lastYearExpenses.setText(createText(selection, selectionArgs));
        selectionArgs[1] = AmountType.INCOMES.toString();
        lastYearIncomes.setText(createText(selection, selectionArgs));

        selection = MoneyEntry.COLUMN_NAME_TYPE + "=?";
        selectionArgs = new String[] { AmountType.INCOMES.toString() };
        totalIncomes.setText(createText(selection, selectionArgs));
        selectionArgs[0] = AmountType.EXPENSES.toString();
        totalExpenses.setText(createText(selection, selectionArgs));
    }

    private String select(String selection, String[] selectionArgs) {
        BigDecimal amount = new BigDecimal(0);
        Cursor cursor = database.query(MoneyEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int amountColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_AMOUNT);
            do {
                amount = amount.add(new BigDecimal(cursor.getString(amountColIndex)));
            } while(cursor.moveToNext());
        }

        cursor.close();

        return amount.abs().toString();
    }

    private String createText(String selection, String[] selectionArgs) {
        String currency = getIntent().getStringExtra("currency");
        return MoneyUtil.moneyToString(select(selection, selectionArgs)) + " " + currency;
    }
}
