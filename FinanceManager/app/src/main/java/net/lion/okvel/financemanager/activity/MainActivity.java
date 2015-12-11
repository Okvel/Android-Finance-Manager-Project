package net.lion.okvel.financemanager.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.adapter.RecyclerViewAdapter;
import net.lion.okvel.financemanager.entity.Currency;
import net.lion.okvel.financemanager.entity.DateConverter;
import net.lion.okvel.financemanager.entity.DateStyle;
import net.lion.okvel.financemanager.entity.FinanceManagerDate;
import net.lion.okvel.financemanager.entity.RecyclerViewItem;
import net.lion.okvel.financemanager.database.FinanceManagerDBHelper;
import net.lion.okvel.financemanager.database.entry.MoneyEntry;
import net.lion.okvel.financemanager.listener.RecyclerViewItemClickListener;
import net.lion.okvel.financemanager.util.DateComparator;
import net.lion.okvel.financemanager.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final int LAYOUT = R.layout.activity_main;

    private final int REQUEST_CODE_DATE = 1;
    private final int REQUEST_CODE_ADD = 2;
    private final int REQUEST_CODE_EDIT = 3;

    private Toolbar toolbar;
    private Toolbar bottomBar;
    private MenuItem backArrowMenuItem;
    private DrawerLayout drawerLayout;
    private RecyclerView.Adapter rvAdapter;

    private BigDecimal totalMoney = new BigDecimal(0);
    private Currency currency = Currency.BYR;
    private List<RecyclerViewItem> content = new LinkedList<>();
    private FinanceManagerDate financeManagerDate;
    private DateConverter dateConverter;
    private SQLiteDatabase database;
    private int itemIndex;
    private DateStyle dateStyle = DateStyle.MONTH_YEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        int[] ids = VectorDrawableCompat.findVectorResourceIdsByConvention(getResources(),
                R.drawable.class, Convention.ResourceNameHasVectorPrefixOrSuffix);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);

        applySettings();
        financeManagerDate = FinanceManagerDate.getInstance();
        financeManagerDate.setDateStyle(dateStyle);
        dateConverter = DateConverter.createInstance(getResources());

        initDataBase();
        initToolBar();
        initNavigationView();
        initRecyclerView();
        intiBottomBar();

        select();
    }

    private void applySettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dateType = preferences.getString(getString(R.string.settings_start_type_key),
                getString(R.string.start_type_default_entry));
        if (dateType.equals(getString(R.string.menu_navigation_day))) {
            dateStyle = DateStyle.DAY_MONTH_YEAR;
        } else if (dateType.equals(getString(R.string.menu_navigation_month))) {
            dateStyle = DateStyle.MONTH_YEAR;
        } else {
            dateStyle = DateStyle.YEAR;
        }
    }

    private void initDataBase() {
        FinanceManagerDBHelper databaseHelper = new FinanceManagerDBHelper(getApplicationContext());
        database = databaseHelper.getWritableDatabase();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setToolbarTitle();
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.menu_main);
        Menu menu = toolbar.getMenu();
        backArrowMenuItem = menu.getItem(0);
        backArrowMenuItem.setVisible(false);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        rvAdapter = new RecyclerViewAdapter(content, getResources());
        recyclerView.setAdapter(rvAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getApplicationContext(),
                rvItemClickListener));
    }

    private void intiBottomBar() {
        bottomBar = (Toolbar) findViewById(R.id.bottom_toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.vector_plus);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.calendar_menu:
                callActivityForResult(DialogActivity.class, REQUEST_CODE_DATE);
                result = true;
                break;
            case R.id.back_to_current_date_menu:
                financeManagerDate.setCurrentDate();
                select();
                final String titleDate = dateConverter.convert(financeManagerDate.getDateStyle());
                toolbar.setTitle(titleDate);
                item.setVisible(false);
                result = true;
                break;
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        callActivityForResult(AddActivity.class, REQUEST_CODE_ADD);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean result = false;

        switch (item.getItemId()) {
            case R.id.day_menu_navigation_item:
                financeManagerDate.setDateStyle(DateStyle.DAY_MONTH_YEAR);
                setToolbarTitle();
                select();
                result = true;
                break;
            case R.id.month_menu_navigation_item:
                financeManagerDate.setDateStyle(DateStyle.MONTH_YEAR);
                setToolbarTitle();
                select();
                result = true;
                break;
            case R.id.year_menu_navigation_item:
                financeManagerDate.setDateStyle(DateStyle.YEAR);
                setToolbarTitle();
                select();
                result = true;
                break;
            case R.id.statistics_menu_navigation_item:
                callStatisticsActivity(StatisticsActivity.class);
                break;
            case R.id.settings_menu_navigation_item:
                callActivity(SettingsActivity.class);
                break;
        }

        drawerLayout.closeDrawers();
        return result;
    }

    private RecyclerViewItemClickListener.OnItemClickListener rvItemClickListener =
            new RecyclerViewItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    itemIndex = position;
                    callEditActivityForResult(AddActivity.class, position);
                }
            };

    private void callActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void callActivityForResult(Class activityClass, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, requestCode);
    }

    private void callEditActivityForResult(Class activityClass, int listItemIndex) {
        RecyclerViewItem item = content.get(listItemIndex);
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("edit", true);
        intent.putExtra("amount", item.getAmount().toString());
        intent.putExtra("year", item.getDate().get(Calendar.YEAR));
        intent.putExtra("month", item.getDate().get(Calendar.MONTH));
        intent.putExtra("day", item.getDate().get(Calendar.DAY_OF_MONTH));
        intent.putExtra("type", item.getType());
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    private void callStatisticsActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("database", database.getPath());
        intent.putExtra("currency", currency.toString());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            BigDecimal amount;
            String type;
            String category;
            String addDate;

            switch (requestCode) {
                case REQUEST_CODE_ADD:
                    amount = new BigDecimal(MoneyUtil.trim(data.getStringExtra("amount")));
                    type = data.getSerializableExtra("type").toString();
                    category = data.getStringExtra("category");
                    addDate = data.getStringExtra("date");
                    long id = insert(amount, type, category, addDate);

                    if (id != -1) {
                        addInformation(id, amount, type, category, addDate);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.main_insert_error),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case REQUEST_CODE_DATE:
                    toolbar.setTitle(data.getStringExtra("financeManagerDate"));
                    if (!checkDate()) {
                        backArrowMenuItem.setVisible(true);
                    } else {
                        backArrowMenuItem.setVisible(false);
                    }
                    select();
                    break;
                case REQUEST_CODE_EDIT:
                    Boolean isDeleted = data.getBooleanExtra("delete", false);
                    if (!isDeleted) {
                        amount = new BigDecimal(MoneyUtil.trim(data.getStringExtra("amount")));
                        type = data.getSerializableExtra("type").toString();
                        category = data.getStringExtra("category");
                        addDate = data.getStringExtra("date");

                        updateInformation(amount, type, category, addDate);
                    } else {
                        removeInformation();
                    }
                    break;
            }
        }
    }

    private void addInformation(long id, BigDecimal amount, String type, String category, String addDate) {
        int[] intDate = stringDateToInt(addDate);
        boolean add = false;
        switch (financeManagerDate.getDateStyle()) {
            case DAY_MONTH_YEAR:
                if (checkDate(intDate[0], intDate[1], intDate[2])) {
                    totalMoney = totalMoney.add(amount);
                    add = true;
                }
                break;
            case MONTH_YEAR:
                if (checkDate(intDate[0], intDate[1])) {
                    totalMoney = totalMoney.add(amount);
                    add = true;
                }
                break;
            case YEAR:
                if (checkDate(intDate[0])) {
                    totalMoney = totalMoney.add(amount);
                    add = true;
                }
                break;
        }

        setBottomBarTitle();
        if (add) {
            addItem(id, amount, type, category, new GregorianCalendar(intDate[0], intDate[1], intDate[2]));
        }
    }

    private void updateInformation(BigDecimal amount, String type, String category, String addDate) {
        int[] intDate = stringDateToInt(addDate);

        RecyclerViewItem item = content.get(itemIndex);
        item.setType(type);
        item.setAmount(amount);
        item.setCategory(category);
        item.setDate(new GregorianCalendar(intDate[0], intDate[1], intDate[2]));

        switch (financeManagerDate.getDateStyle()) {
            case DAY_MONTH_YEAR:
                if (!checkDate(intDate[0], intDate[1], intDate[2])) {
                    content.remove(item);
                }
                break;
            case MONTH_YEAR:
                if (!checkDate(intDate[0], intDate[1])) {
                    content.remove(item);
                }
                break;
            case YEAR:
                if (!checkDate(intDate[0])) {
                    content.remove(item);
                }
                break;
        }

        totalMoney = new BigDecimal(0);
        for (RecyclerViewItem viewItem : content) {
            totalMoney = totalMoney.add(viewItem.getAmount());
        }
        setBottomBarTitle();

        Collections.sort(content, new DateComparator());
        rvAdapter.notifyDataSetChanged();
        update(item);
    }

    private void removeInformation() {
        BigDecimal amount = content.get(itemIndex).getAmount();
        remove(content.get(itemIndex).getId());
        content.remove(itemIndex);
        rvAdapter.notifyDataSetChanged();
        totalMoney = totalMoney.subtract(amount);
        setBottomBarTitle();
    }

    private void setToolbarTitle() {
        String titleDate = dateConverter.convert(financeManagerDate.getDateStyle());
        toolbar.setTitle(titleDate);
    }

    private void setBottomBarTitle() {
        String money = MoneyUtil.moneyToString(totalMoney.toString());
        bottomBar.setTitle(getResources().getString(R.string.bottom_bar_total) + " " + money + " " + currency);
    }

    private boolean checkDate() {
        Calendar calendar = Calendar.getInstance();

        return  (financeManagerDate.getDay() == calendar.get(Calendar.DAY_OF_MONTH)) &&
                (financeManagerDate.getMonth() == calendar.get(Calendar.MONTH)) &&
                (financeManagerDate.getYear() == calendar.get(Calendar.YEAR));
    }

    private boolean checkDate(int year) {
        return financeManagerDate.getYear() == year;
    }

    private boolean checkDate(int year, int month) {
        return (financeManagerDate.getYear() == year) && (financeManagerDate.getMonth() == month);
    }

    private boolean checkDate(int year, int month, int day) {
        return  (financeManagerDate.getYear() == year) &&
                (financeManagerDate.getMonth() == month) &&
                (financeManagerDate.getDay() == day);
    }

    private long insert(BigDecimal amount, String type, String category, String stringDate) {
        int[] intDate = stringDateToInt(stringDate);

        ContentValues values = new ContentValues();
        values.put(MoneyEntry.COLUMN_NAME_AMOUNT, amount.toString());
        values.put(MoneyEntry.COLUMN_NAME_CURRENCY, currency.toString());
        values.put(MoneyEntry.COLUMN_NAME_TYPE, type);
        values.put(MoneyEntry.COLUMN_NAME_CATEGORY, category);
        values.put(MoneyEntry.COLUMN_NAME_DAY, intDate[2]);
        values.put(MoneyEntry.COLUMN_NAME_MONTH, intDate[1]);
        values.put(MoneyEntry.COLUMN_NAME_YEAR, intDate[0]);

        return database.insert(MoneyEntry.TABLE_NAME, null, values);
    }

    private void select() {
        content.clear();
        totalMoney = new BigDecimal(0);

        String selection = takeSelection();
        String[] selectionArgs = takeSelectionArgs();
        Cursor cursor = database.query(MoneyEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(MoneyEntry._ID);
            int amountColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_AMOUNT);
            int currencyColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_CURRENCY);
            int typeColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_TYPE);
            int categoryColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_CATEGORY);
            int dayColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_DAY);
            int monthColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_MONTH);
            int yearColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_YEAR);

            do {
                BigDecimal amount = new BigDecimal(cursor.getString(amountColIndex));
                content.add(new RecyclerViewItem(cursor.getLong(idColIndex),
                        amount,
                        Currency.valueOf(cursor.getString(currencyColIndex)),
                        cursor.getString(typeColIndex),
                        cursor.getString(categoryColIndex),
                        new GregorianCalendar(cursor.getInt(yearColIndex),
                                cursor.getInt(monthColIndex), cursor.getInt(dayColIndex))));
                totalMoney = totalMoney.add(amount);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Collections.sort(content, new DateComparator());
        rvAdapter.notifyDataSetChanged();
        setBottomBarTitle();
    }

    private String takeSelection() {
        String result = null;

        switch (financeManagerDate.getDateStyle()) {
            case DAY_MONTH_YEAR:
                result = MoneyEntry.COLUMN_NAME_YEAR + "=? AND " +
                        MoneyEntry.COLUMN_NAME_MONTH + "=? AND " +
                        MoneyEntry.COLUMN_NAME_DAY + "=?";
                break;
            case MONTH_YEAR:
                result = MoneyEntry.COLUMN_NAME_YEAR + "=? AND " +
                        MoneyEntry.COLUMN_NAME_MONTH + "=?";
                break;
            case YEAR:
                result = MoneyEntry.COLUMN_NAME_YEAR + "=?";
                break;
        }

        return result;
    }

    private String[] takeSelectionArgs() {
        String[] result = null;

        switch (financeManagerDate.getDateStyle()) {
            case DAY_MONTH_YEAR:
                result = new String[]{
                        String.valueOf(financeManagerDate.getYear()),
                        String.valueOf(financeManagerDate.getMonth()),
                        String.valueOf(financeManagerDate.getDay())
                };
                break;
            case MONTH_YEAR:
                result = new String[]{
                        String.valueOf(financeManagerDate.getYear()),
                        String.valueOf(financeManagerDate.getMonth())
                };
                break;
            case YEAR:
                result = new String[]{
                        String.valueOf(financeManagerDate.getYear())
                };
                break;
        }

        return result;
    }

    private void update(RecyclerViewItem item) {
        ContentValues values = new ContentValues();
        values.put(MoneyEntry.COLUMN_NAME_AMOUNT, item.getAmount().toString());
        values.put(MoneyEntry.COLUMN_NAME_CURRENCY, currency.toString());
        values.put(MoneyEntry.COLUMN_NAME_TYPE, item.getType());
        values.put(MoneyEntry.COLUMN_NAME_CATEGORY, item.getCategory());
        values.put(MoneyEntry.COLUMN_NAME_DAY, item.getDate().get(Calendar.DAY_OF_MONTH));
        values.put(MoneyEntry.COLUMN_NAME_MONTH, item.getDate().get(Calendar.MONTH));
        values.put(MoneyEntry.COLUMN_NAME_YEAR, item.getDate().get(Calendar.YEAR));

        String selection = MoneyEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(item.getId()) };

        database.update(MoneyEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private void remove(long id) {
        String selection = MoneyEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(MoneyEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void addItem(long id, BigDecimal amount, String type, String category, GregorianCalendar chosenDate) {
        content.add(new RecyclerViewItem(id, amount, currency, type, category, chosenDate));
        Collections.sort(content, new DateComparator());
        rvAdapter.notifyDataSetChanged();
    }

    private int[] stringDateToInt(String dateString) {
        String[] dateComponents = dateString.split(":");
        return new int[]{
                Integer.parseInt(dateComponents[0]),
                Integer.parseInt(dateComponents[1]),
                Integer.parseInt(dateComponents[2])
        };
    }
}
