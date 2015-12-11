package net.lion.okvel.financemanager.activity;

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
import net.lion.okvel.financemanager.entity.AmountType;
import net.lion.okvel.financemanager.entity.FinanceManagerDate;
import net.lion.okvel.financemanager.entity.DateConverter;
import net.lion.okvel.financemanager.entity.DateStyle;
import net.lion.okvel.financemanager.fragment.DatePickerFragment;
import net.lion.okvel.financemanager.util.MoneyUtil;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private static final int LAYOUT = R.layout.activity_add;

    private EditText amountEditText;
    private Button dateButton;
    private Button incomesButton;
    private Button expensesButton;
    private Button categoryButton;

    private int[] tmpDate;
    private String amount;
    private AmountType type;
    private FinanceManagerDate financeManagerDate;
    private DateStyle dateStyle;
    private DateConverter converter;
    private int cursorPosition;
    private String previousAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        financeManagerDate = FinanceManagerDate.getInstance();
        tmpDate = new int[3];
        dateStyle = DateStyle.DAY_MONTH_YEAR;
        converter = DateConverter.instance;

        initToolbar();
        loadParams();
        initContext();
    }

    private void loadParams() {
        boolean isEdit = getIntent().getBooleanExtra("edit", false);

        if (isEdit) {
            initBottomToolbar();
            Intent intent = getIntent();
            amount = intent.getStringExtra("amount");
            if (Double.parseDouble(amount) < 0) {
                amount = amount.substring(1);
            }
            previousAmount = amount;
            tmpDate[0] = intent.getIntExtra("year", 1970);
            tmpDate[1] = intent.getIntExtra("month", 1);
            tmpDate[2] = intent.getIntExtra("day", 1);
            type = AmountType.valueOf(intent.getStringExtra("type"));
        } else {
            previousAmount = "";
            tmpDate[0] = financeManagerDate.getYear();
            tmpDate[1] = financeManagerDate.getMonth();
            tmpDate[2] = financeManagerDate.getDay();
            type = AmountType.INCOMES;
        }
    }

    private void initBottomToolbar() {
        Toolbar bottomToolbar = (Toolbar) findViewById(R.id.bottom_toolbar);
        bottomToolbar.setVisibility(View.VISIBLE);
        bottomToolbar.inflateMenu(R.menu.menu_delete);
        bottomToolbar.setOnMenuItemClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_activity_toolbar);
        toolbar.inflateMenu(R.menu.menu_add);
        toolbar.setNavigationIcon(R.drawable.vector_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(this);
    }

    private void initContext() {
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
        amountEditText.addTextChangedListener(textWatcher);
        amountEditText.setText(amount);

        dateButton = (Button) findViewById(R.id.date_button);
        dateButton.setText(converter.convert(dateStyle, tmpDate[0], tmpDate[1], tmpDate[2]));
        dateButton.setOnClickListener(this);

        incomesButton = (Button) findViewById(R.id.button_incomes);
        incomesButton.setOnClickListener(this);
        expensesButton = (Button) findViewById(R.id.button_expenses);
        expensesButton.setOnClickListener(this);
        if (type == AmountType.INCOMES) {
            incomesButton.setTextColor(getResources().getColor(R.color.black));
            expensesButton.setTextColor(getResources().getColor(R.color.button_text_inactive));
        } else {
            expensesButton.setTextColor(getResources().getColor(R.color.black));
            incomesButton.setTextColor(getResources().getColor(R.color.button_text_inactive));
        }

        String category = "No category";
        categoryButton = (Button) findViewById(R.id.category_button);
        categoryButton.setText(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_button:
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setDate(tmpDate[0], tmpDate[1], tmpDate[2]);
                dialog.setDateStyle(dateStyle);
                dialog.setTmpData(tmpDate);
                dialog.setButton(dateButton);
                dialog.show(getFragmentManager(), "datePicker");
                break;
            case R.id.button_incomes:
                swapButtonTextColor(incomesButton, expensesButton);
                type = AmountType.INCOMES;
                break;
            case R.id.button_expenses:
                swapButtonTextColor(expensesButton, incomesButton);
                type = AmountType.EXPENSES;
                break;
        }
    }

    private void swapButtonTextColor(Button activeButton, Button inactiveButton) {
        activeButton.setTextColor(getResources().getColor(R.color.black));
        inactiveButton.setTextColor(getResources().getColor(R.color.button_text_inactive));
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(s.toString())) {
                if (count == 0) {
                    cursorPosition = start;
                } else if ((s.length() - 1 != start) && count == 1) {
                    cursorPosition = start + 1;
                } else {
                    cursorPosition = s.length();
                }
            } else {
                cursorPosition = 0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!".".equals(s.toString())) {
                amountEditText.removeTextChangedListener(textWatcher);
                String money = MoneyUtil.moneyToString(removeSpaces(s));
                amountEditText.setText(money);
                amountEditText.addTextChangedListener(textWatcher);

                if (money.length() > s.length()) {
                    cursorPosition++;
                } else if (previousAmount.length() - money.length() > 1) {
                    cursorPosition--;
                }
                amountEditText.setSelection(cursorPosition);
                previousAmount = money;
            }
        }
    };

    private String removeSpaces(Editable s) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (!" ".equals(s.toString().substring(i, i + 1))) {
                result.append(s.charAt(i));
            }
        }

        return result.toString();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.add_menu_save:
                String date = tmpDate[0] + ":" + tmpDate[1] + ":" + tmpDate[2];
                intent = new Intent();
                intent.putExtra("amount", checkType());
                intent.putExtra("date", date);
                intent.putExtra("type", type);
                intent.putExtra("category", categoryButton.getText());
                intent.putExtra("delete", false);
                setResult(RESULT_OK, intent);
                finish();
            case R.id.activity_edit_delete:
                intent = new Intent();
                intent.putExtra("delete", true);
                setResult(RESULT_OK, intent);
                finish();
            default:
                return false;
        }
    }

    private String checkType() {
        StringBuilder stringBuilder = new StringBuilder(removeSpaces(amountEditText.getText()));
        if (type == AmountType.EXPENSES) {
            stringBuilder.insert(0, "-");
        }

        return stringBuilder.toString();
    }
}
