package net.lion.okvel.financemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.bean.Date;
import net.lion.okvel.financemanager.bean.DateConverter;
import net.lion.okvel.financemanager.bean.DateStyle;

import java.util.Calendar;

public class DialogActivity extends AppCompatActivity
        implements View.OnClickListener, NumberPicker.OnValueChangeListener
{
    private static final int LAYOUT = R.layout.dialog_date;

    private NumberPicker numberPickerDay;
    private NumberPicker numberPickerMonth;
    private NumberPicker numberPickerYear;

    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.DialogTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        date = Date.getInstance();

        setTitle(R.string.date_dialog_title);

        boolean isDay = false;
        boolean isMonth = false;
        if (date.getDateStyle() == DateStyle.DAY_MONTH_YEAR) {
            isDay = true;
            isMonth = true;
        }
        else if (date.getDateStyle() == DateStyle.MONTH_YEAR) {
            isMonth = true;
        }
        initNumberPickers(isDay, isMonth);

        initPositiveDialogButton();
        initNegativeDialogButton();
    }

    private void initNumberPickers(boolean isDay, boolean isMonth)
    {
        Calendar calendar = Calendar.getInstance();

        numberPickerDay = (NumberPicker) findViewById(R.id.numberPicker_day);
        numberPickerDay.setMinValue(1);
        numberPickerDay.setMaxValue(31);
        numberPickerDay.setValue(date.getDay());

        numberPickerMonth = (NumberPicker) findViewById(R.id.numberPicker_month);
        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(date.getMonth() + 1);
        numberPickerMonth.setOnValueChangedListener(this);

        numberPickerYear = (NumberPicker) findViewById(R.id.numberPicker_year);
        numberPickerYear.setMinValue(1970);
        numberPickerYear.setMaxValue(calendar.get(Calendar.YEAR));
        numberPickerYear.setValue(date.getYear());
        numberPickerYear.setOnValueChangedListener(this);

        checkYear(date.getYear());

        numberPickerDay.setEnabled(isDay);
        numberPickerMonth.setEnabled(isMonth);
    }

    private void initPositiveDialogButton()
    {
        Button positiveDialogButton = (Button) findViewById(R.id.positive_button);
        positiveDialogButton.setOnClickListener(this);
    }

    private void initNegativeDialogButton()
    {
        Button negativeDialogButton = (Button) findViewById(R.id.negative_button);
        negativeDialogButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.positive_button:
                date.setYear(numberPickerYear.getValue());
                date.setMonth(numberPickerMonth.getValue() - 1);
                date.setDay(numberPickerDay.getValue());
                Intent intent = new Intent();
                String result = DateConverter.instance.convert(date.getDateStyle());
                intent.putExtra("date", result);
                setResult(RESULT_OK, intent);
                break;
            case R.id.negative_button:
                setResult(RESULT_CANCELED);
                break;
        }

        finish();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
    {
        switch (picker.getId()) {
            case R.id.numberPicker_month:
                checkMonth(newVal);
                break;
            case R.id.numberPicker_year:
                checkYear(newVal);
                break;
        }
    }

    private void checkMonth(int newVal)
    {
        final int FEBRUARY = 2;
        final int APRIL = 4;
        final int JUNE = 6;
        final int SEPTEMBER = 9;
        final int NOVEMBER = 11;

        Calendar calendar = Calendar.getInstance();
        boolean isCurrentDate = (newVal - 1) == calendar.get(Calendar.MONTH) &&
                numberPickerYear.getValue() == calendar.get(Calendar.YEAR);
        if (isCurrentDate) {
            numberPickerDay.setMaxValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
        else if (newVal == FEBRUARY) {
            if (checkLeapYear(numberPickerYear.getValue())) {
                numberPickerDay.setMaxValue(29);
            }
            else {
                numberPickerDay.setMaxValue(28);
            }
        }
        else if (newVal == APRIL || newVal == JUNE || newVal == SEPTEMBER || newVal == NOVEMBER) {
            numberPickerDay.setMaxValue(30);
        }
        else {
            numberPickerDay.setMaxValue(31);
        }
    }

    private void checkYear(int newVal)
    {
        Calendar calendar = Calendar.getInstance();
        if (newVal == calendar.get(Calendar.YEAR)) {
            numberPickerDay.setMaxValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            numberPickerMonth.setMaxValue(Calendar.getInstance().get(Calendar.MONTH) + 1);
        }
        else {
            checkMonth(numberPickerMonth.getValue());
            numberPickerMonth.setMaxValue(12);
        }
    }

    private boolean checkLeapYear(int year)
    {
        boolean result = false;

        if (year % 400 == 0) {
            result = true;
        }
        else if (year % 4 == 0) {
            result = true;
        }

        return result;
    }
}
