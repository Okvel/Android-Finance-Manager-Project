package net.lion.okvel.financemanager.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import net.lion.okvel.financemanager.bean.DateConverter;
import net.lion.okvel.financemanager.bean.DateStyle;
import net.lion.okvel.financemanager.bean.RecyclerViewItem;
import net.lion.okvel.financemanager.database.FinanceManagerDBHelper;
import net.lion.okvel.financemanager.database.entry.MoneyEntry;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener,
        NavigationView.OnNavigationItemSelectedListener
{
    /**
     * Main application layout
     */
    private static final int LAYOUT = R.layout.activity_main;

    private final int REQUEST_CODE_DATE = 1;
    private final int REQUEST_CODE_ADD = 2;

    private Toolbar toolbar;
    private Toolbar bottomBar;
    private FloatingActionButton fab;
    private MenuItem backArrowMenuItem;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    private int totalMoney = 0;
    private List<RecyclerViewItem> content = new LinkedList<>();
    private net.lion.okvel.financemanager.bean.Date date;
    private DateConverter dateConverter;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        int[] ids = VectorDrawableCompat.findVectorResourceIdsByConvention(getResources(),
                R.drawable.class, Convention.ResourceNameHasVectorPrefixOrSuffix);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);
        date = net.lion.okvel.financemanager.bean.Date.getInstance();
        date.setDateStyle(DateStyle.MONTH_YEAR);
        dateConverter = DateConverter.createInstance(getResources());

        initDataBase();
        initToolBar();
        initNavigationView();
        initRecyclerView();
        intiBottomBar();
    }

    private void initDataBase()
    {
        FinanceManagerDBHelper databaseHelper = new FinanceManagerDBHelper(getApplicationContext());
        database = databaseHelper.getWritableDatabase();
    }

    /**
     * Initialize {@linkplain Toolbar}
     */
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        final String titleDate = dateConverter.convert(date.getDateStyle());

        toolbar.setTitle(titleDate);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.menu_main);
        Menu menu = toolbar.getMenu();
        backArrowMenuItem = menu.getItem(0);
        backArrowMenuItem.setVisible(false);
    }

    /**
     * Initialize {@linkplain NavigationView}
     */
    private void initNavigationView()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Initialize {@linkplain RecyclerView}
     */
    private void initRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        rvAdapter = new RecyclerViewAdapter(content);
        recyclerView.setAdapter(rvAdapter);

        select();
    }

    /**
     * Initialize bottom {@linkplain Toolbar} with {@linkplain FloatingActionButton}
     */
    private void intiBottomBar()
    {
        bottomBar = (Toolbar) findViewById(R.id.bottom_toolbar);

        setBottomBarTitle();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.vector_plus);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.calendar_menu:
                callActivityForResult(DialogActivity.class, REQUEST_CODE_DATE);
                result = true;
                break;
            case R.id.back_to_current_date_menu:
                date.setCurrentDate();
                select();
                setBottomBarTitle();
                final String titleDate = dateConverter.convert(date.getDateStyle());
                toolbar.setTitle(titleDate);
                item.setVisible(false);
                result = true;
                break;
        }

        return result;
    }

    @Override
    public void onClick(View v)
    {
        callActivityForResult(AddActivity.class, REQUEST_CODE_ADD);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        boolean result = false;

        switch (item.getItemId()) {
            case R.id.month_menu_navigation_item:
                date.setDateStyle(DateStyle.MONTH_YEAR);
                select();
                result = true;
                break;
        }

        drawerLayout.closeDrawers();
        return result;
    }

    private void callActivity(Class activityClass)
    {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void callActivityForResult(Class activityClass, int requestCode)
    {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD:
                    String amount = data.getStringExtra("amount");
                    String category = data.getStringExtra("category");
                    String addDate = data.getStringExtra("date");

                    if (insert(amount, category, addDate) != -1) {
                        int[] intDate = stringDateToInt(addDate);
                        switch (date.getDateStyle()) {
                            case DAY_MONTH_YEAR:
                                if (checkDate(intDate[0], intDate[1], intDate[2])) {
                                    totalMoney += Integer.parseInt(amount);
                                    setBottomBarTitle();
                                    addItem(amount, category, new Date(intDate[0], intDate[1], intDate[2]));
                                }
                                break;
                            case MONTH_YEAR:
                                if (checkDate(intDate[0], intDate[1])) {
                                    totalMoney += Integer.parseInt(amount);
                                    setBottomBarTitle();
                                    addItem(amount, category, new Date(intDate[0], intDate[1], intDate[2]));
                                }
                                break;
                            case YEAR:
                                if (checkDate(intDate[0])) {
                                    totalMoney += Integer.parseInt(amount);
                                    setBottomBarTitle();
                                    addItem(amount, category, new Date(intDate[0], intDate[1], intDate[2]));
                                }
                                break;
                        }
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.main_insert_error),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case REQUEST_CODE_DATE:
                    toolbar.setTitle(data.getStringExtra("date"));
                    if (!checkDate()) {
                        backArrowMenuItem.setVisible(true);
                    }
                    else {
                        backArrowMenuItem.setVisible(false);
                    }
                    select();
                    setBottomBarTitle();
                    break;
            }
        }
    }

    private void setBottomBarTitle()
    {
        String totalText = getResources().getString(R.string.bottom_bar_total) +
                " " + totalMoney + " " + getResources().getString(R.string.bottom_bar_RUB);
        bottomBar.setTitle(totalText);
    }

    private boolean checkDate()
    {
        Calendar calendar = Calendar.getInstance();

        return (date.getDay() == calendar.get(Calendar.DAY_OF_MONTH)) &&
                (date.getMonth() == calendar.get(Calendar.MONTH)) &&
                (date.getYear() == calendar.get(Calendar.YEAR));
    }

    private boolean checkDate(int year)
    {
        return date.getYear() == year;
    }

    private boolean checkDate(int year, int month)
    {
        return (date.getYear() == year) && (date.getMonth() == month);
    }

    private boolean checkDate(int year, int month, int day)
    {
        return (date.getYear() == year) && (date.getMonth() == month) && (date.getDay() == day);
    }

    private long insert(String amount, String category, String stringDate)
    {
        String[] dateComponents = stringDate.split(":");

        ContentValues values = new ContentValues();
        values.put(MoneyEntry.COLUMN_NAME_AMOUNT, Integer.parseInt(amount));
        values.put(MoneyEntry.COLUMN_NAME_CATEGORY, category);
        values.put(MoneyEntry.COLUMN_NAME_DAY, Integer.parseInt(dateComponents[2]));
        values.put(MoneyEntry.COLUMN_NAME_MONTH, Integer.parseInt(dateComponents[1]));
        values.put(MoneyEntry.COLUMN_NAME_YEAR, Integer.parseInt(dateComponents[0]));

        return database.insert(MoneyEntry.TABLE_NAME, null, values);
    }

    private void select()
    {
        content.clear();
        totalMoney = 0;
        rvAdapter.notifyDataSetChanged();

        String selection = getSelection();
        String[] selectionArgs = getSelectionArgs();
        Cursor cursor = database.query(MoneyEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int amountColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_AMOUNT);
            int categoryColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_CATEGORY);
            int dayColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_DAY);
            int monthColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_MONTH);
            int yearColIndex = cursor.getColumnIndex(MoneyEntry.COLUMN_NAME_YEAR);

            do {
                totalMoney += cursor.getInt(amountColIndex);

                addItem(cursor.getString(amountColIndex), cursor.getString(categoryColIndex),
                        new Date(cursor.getInt(yearColIndex), cursor.getInt(monthColIndex),
                                cursor.getInt(dayColIndex)));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    private String getSelection()
    {
        String result = null;

        switch (date.getDateStyle()) {
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

    private String[] getSelectionArgs()
    {
        String[] result = null;

        switch (date.getDateStyle()) {
            case DAY_MONTH_YEAR:
                result = new String[] {
                        ((Integer) date.getYear()).toString(),
                        ((Integer) date.getMonth()).toString(),
                        ((Integer) date.getDay()).toString()
                };
                break;
            case MONTH_YEAR:
                result = new String[] {
                        ((Integer) date.getYear()).toString(),
                        ((Integer) date.getMonth()).toString()
                };
                break;
            case YEAR:
                result = new String[] {
                        ((Integer) date.getYear()).toString()
                };
                break;
        }

        return result;
    }

    private void addItem(String amount, String category, Date chosenDate)
    {
        content.add(new RecyclerViewItem(amount + " " + getResources().getString(R.string.bottom_bar_RUB),
                getResources().getString(R.string.recycler_view_item_category) + " " + category, chosenDate));
        rvAdapter.notifyDataSetChanged();
    }

    private int[] stringDateToInt(String dateString)
    {
        String[] dateComponents = dateString.split(":");
        return new int[] {
                Integer.parseInt(dateComponents[0]),
                Integer.parseInt(dateComponents[1]),
                Integer.parseInt(dateComponents[2])
        };
    }
}
