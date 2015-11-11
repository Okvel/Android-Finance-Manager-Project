package net.lion.okvel.financemanager.activity;

import android.content.ContentValues;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.adapter.RecyclerViewAdapter;
import net.lion.okvel.financemanager.bean.DateConverter;
import net.lion.okvel.financemanager.bean.DateStyle;
import net.lion.okvel.financemanager.database.FinanceManagerDBHelper;
import net.lion.okvel.financemanager.database.entry.MoneyEntry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
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
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    private int totalMoney = 0;
    private String[] data = new String[1];
    private net.lion.okvel.financemanager.bean.Date date;
    private DateConverter dateConverter;
    private FinanceManagerDBHelper databaseHelper;
    private SQLiteDatabase database;
    private String amount;
    private String category;
    private String addDate;

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
        databaseHelper = new FinanceManagerDBHelper(getApplicationContext());
        database = databaseHelper.getWritableDatabase();
    }

    /**
     * Initialize {@linkplain Toolbar}
     */
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        final String titleDate = dateConverter.convert(date.getDateStyle());

        toolbar.setTitle(titleDate);
        toolbar.setOnMenuItemClickListener(toolbarMenuItemClickListener);
        toolbar.inflateMenu(R.menu.menu_main);
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                drawerLayout.closeDrawers();
                return true;
            }
        });
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

        rvAdapter = new RecyclerViewAdapter(data);
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * Initialize bottom {@linkplain Toolbar} with {@linkplain FloatingActionButton}
     */
    private void intiBottomBar()
    {
        bottomBar = (Toolbar) findViewById(R.id.bottom_toolbar);

        String totalText = getResources().getString(R.string.bottom_bar_total) +
                " " + totalMoney + " " + getResources().getString(R.string.bottom_bar_RUB);
        bottomBar.setTitle(totalText);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.vector_plus);
        fab.setOnClickListener(this);
    }

    private Toolbar.OnMenuItemClickListener toolbarMenuItemClickListener =
            new Toolbar.OnMenuItemClickListener()
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            boolean result = false;
            switch (item.getItemId()) {
                case R.id.calendar_menu:
                    callActivityForResult(DialogActivity.class, REQUEST_CODE_DATE);
                    result = true;
                    break;
            }

            return result;
        }
    };

    @Override
    public void onClick(View v)
    {
        callActivityForResult(AddActivity.class, REQUEST_CODE_ADD);
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
                    amount = data.getStringExtra("amount");
                    category = data.getStringExtra("category");
                    addDate = data.getStringExtra("date");

                    int id = 1;
                    ContentValues values = new ContentValues();
                    values.put(MoneyEntry.COLUMN_NAME_ENTRY_ID, id);
                    values.put(MoneyEntry.COLUMN_NAME_AMOUNT, amount);
                    values.put(MoneyEntry.COLUMN_NAME_CATEGORY, category);
                    values.put(MoneyEntry.COLUMN_NAME_DATE, addDate);
                    break;
                case REQUEST_CODE_DATE:
                    toolbar.setTitle(data.getStringExtra("date"));
                    break;
            }
        }
    }
}
