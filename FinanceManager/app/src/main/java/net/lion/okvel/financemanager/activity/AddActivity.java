package net.lion.okvel.financemanager.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.bean.Date;
import net.lion.okvel.financemanager.bean.DateConverter;
import net.lion.okvel.financemanager.bean.DateStyle;
import net.lion.okvel.financemanager.dialog.DatePickerFragment;

public class AddActivity extends AppCompatActivity
{
    private static final int LAYOUT = R.layout.activity_add;

    private Toolbar toolbar;
    private EditText amountEditText;
    private Button dateButton;
    private Button categoryButton;

    private int[] tmpDate;
    private String category;
    private Date date;
    private DateStyle dateStyle;
    private DateConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        date = Date.getInstance();
        tmpDate = new int[3];
        tmpDate[0] = date.getYear();
        tmpDate[1] = date.getMonth();
        tmpDate[2] = date.getDay();
        dateStyle = DateStyle.DAY_MONTH_YEAR;
        converter = DateConverter.instance;

        initToolbar();
        initContext();
    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.add_activity_toolbar);
        toolbar.inflateMenu(R.menu.menu_add);
        toolbar.setNavigationIcon(R.drawable.vector_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private void initContext()
    {
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
        amountEditText.addTextChangedListener(textWatcher);

        dateButton = (Button) findViewById(R.id.date_button);
        dateButton.setText(converter.convert(dateStyle));
        dateButton.setOnClickListener(onClickListener);

        category = "No category";
        categoryButton = (Button) findViewById(R.id.category_button);
        categoryButton.setText(category);
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.date_button:
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.setDate(tmpDate[0], tmpDate[1], tmpDate[2]);
                    dialog.setDateStyle(dateStyle);
                    dialog.setTmpData(tmpDate);
                    dialog.setButton(dateButton);
                    dialog.show(getFragmentManager(), "datePicker");
            }
        }
    };

    TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener()
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            switch (item.getItemId()) {
                case R.id.add_menu_save:
                    String date = tmpDate[0] + ":" + tmpDate[1] + ":" + tmpDate[2];
                    Intent intent = new Intent();
                    intent.putExtra("amount", amountEditText.getText().toString());
                    intent.putExtra("date", date);
                    intent.putExtra("category", categoryButton.getText());
                    setResult(RESULT_OK, intent);
                    finish();
                default:
                    return false;
            }
        }
    };
}
