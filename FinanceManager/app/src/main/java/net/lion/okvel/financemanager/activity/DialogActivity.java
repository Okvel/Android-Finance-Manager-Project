package net.lion.okvel.financemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.entity.DateConverter;
import net.lion.okvel.financemanager.entity.DateStyle;
import net.lion.okvel.financemanager.entity.FinanceManagerDate;

import java.util.Calendar;

public class DialogActivity extends AppCompatActivity
        implements View.OnClickListener, NumberPicker.OnValueChangeListener
{
    private static final int LAYOUT = R.layout.dialog_date;

    private NumberPicker numberPickerDay;
    private NumberPicker numberPickerMonth;
    private NumberPicker numberPickerYear;

    private FinanceManagerDate financeManagerDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.DialogTheme);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        financeManagerDate = FinanceManagerDate.getInstance();

        setTitle(R.string.date_dialog_title);

        boolean isDay = false;
        boolean isMonth = false;
        if (financeManagerDate.getDateStyle() == DateStyle.DAY_MONTH_YEAR) {
            isDay = true;
            isMonth = true;
        }
        else if (financeManagerDate.getDateStyle() == DateStyle.MONTH_YEAR) {
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
        numberPickerDay.setValue(financeManagerDate.getDay());

        numberPickerMonth = (NumberPicker) findViewById(R.id.numberPicker_month);
        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(financeManagerDate.getMonth() + 1);
        numberPickerMonth.setOnValueChangedListener(this);

        numberPickerYear = (NumberPicker) findViewById(R.id.numberPicker_year);
        numberPickerYear.setMinValue(1970);
        numberPickerYear.setMaxValue(calendar.get(Calendar.YEAR));
        numberPickerYear.setValue(financeManagerDate.getYear());
        numberPickerYear.setOnValueChangedListener(this);

        checkYear(financeManagerDate.getYear());

        numberPickerDay.setEnabled(isDay);
        numberPickerMonth.setEnabled(isMonth);
    }

    private void initPositiveDialogButton()
    {
        Button positiveDialogButton = (Button) findViewById(R.id.positive_button);
        positiveDialogButton.setOnClickListener(this);
        positiveDialogButton.setTextColor(getResources().getColor(R.color.accent_fm_theme));
    }

    private void initNegativeDialogButton()
    {
        Button negativeDialogButton = (Button) findViewById(R.id.negative_button);
        negativeDialogButton.setOnClickListener(this);
        negativeDialogButton.setTextColor(getResources().getColor(R.color.accent_fm_theme));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.positive_button:
                financeManagerDate.setYear(numberPickerYear.getValue());
                financeManagerDate.setMonth(numberPickerMonth.getValue() - 1);
                financeManagerDate.setDay(numberPickerDay.getValue());
                Intent intent = new Intent();
                String result = DateConverter.instance.convert(financeManagerDate.getDateStyle());
                intent.putExtra("financeManagerDate", result);
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
