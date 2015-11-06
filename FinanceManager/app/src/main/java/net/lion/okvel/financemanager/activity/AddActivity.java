package net.lion.okvel.financemanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;

import net.lion.okvel.financemanager.R;

public class AddActivity extends AppCompatActivity
{
    private static final int LAYOUT = R.layout.activity_add;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        int[] ids = VectorDrawableCompat.findVectorResourceIdsByConvention(getResources(),
                R.drawable.class, Convention.ResourceNameHasVectorPrefixOrSuffix);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);

        initToolbar();
    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add);
    }
}
