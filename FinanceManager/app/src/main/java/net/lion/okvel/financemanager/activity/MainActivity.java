package net.lion.okvel.financemanager.activity;

import android.app.ActionBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;

import net.lion.okvel.financemanager.R;

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

    private int totalMoney = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        int[] ids = VectorDrawableCompat.findVectorResourceIdsByConvention(getResources(),
                R.drawable.class, Convention.ResourceNameHasVectorPrefixOrSuffix);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);

        initToolBar();
        initNavigationView();
        intiBottomBar();
    }

    /**
     * Initialize {@linkplain Toolbar}
     */
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.inflateMenu(R.menu.menu);
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
     * Initialize bottom {@linkplain Toolbar} with {@linkplain FloatingActionButton}
     */
    private void intiBottomBar()
    {
        bottomBar = (Toolbar) findViewById(R.id.bottom_toolbar);

        String totalText = getResources().getString(R.string.bottom_bar_total) +
                " " + totalMoney + " " + getResources().getString(R.string.bottom_bar_RUB);
        bottomBar.setTitle(totalText);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.plus_vector);
    }
}
