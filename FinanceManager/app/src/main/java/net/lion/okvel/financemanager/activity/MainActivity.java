package net.lion.okvel.financemanager.activity;

import android.app.DialogFragment;
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

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.adapter.RecyclerViewAdapter;
import net.lion.okvel.financemanager.bean.DateConverter;
import net.lion.okvel.financemanager.bean.DateStyle;
import net.lion.okvel.financemanager.dialog.DatePickerFragment;

public class MainActivity extends AppCompatActivity
{
    /**
     * Main application layout
     */
    private static final int LAYOUT = R.layout.activity_main;

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
    private DateStyle dateStyle;
    private DateConverter dateConverter;

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
        dateStyle = DateStyle.MONTH_YEAR;
        dateConverter = DateConverter.createInstance(getResources());

        initToolBar();
        initNavigationView();
        initRecyclerView();
        intiBottomBar();
    }

    /**
     * Initialize {@linkplain Toolbar}
     */
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        final String date = dateConverter.convert(dateStyle);

        toolbar.setTitle(date);
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
    }

    private Toolbar.OnMenuItemClickListener toolbarMenuItemClickListener =
            new Toolbar.OnMenuItemClickListener()
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            switch (item.getItemId()) {
                case R.id.calendar_menu:
                    DialogFragment dialog = new DatePickerFragment();
                    DatePickerFragment fragment = (DatePickerFragment) dialog;
                    fragment.setDate(date.getYear(), date.getMonth(), date.getDay());
                    fragment.setToolbar(toolbar);
                    fragment.setDateStyle(dateStyle);
                    dialog.show(getFragmentManager(), "datePicker");
                    return true;
                default:
                    return false;
            }
        }
    };
}
