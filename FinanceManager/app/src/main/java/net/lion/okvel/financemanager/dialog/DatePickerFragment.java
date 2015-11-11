package net.lion.okvel.financemanager.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.DatePicker;

import net.lion.okvel.financemanager.bean.DateConverter;
import net.lion.okvel.financemanager.bean.DateStyle;

import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private int year;
    private int month;
    private int day;
    private Button button;
    private int[] tmpData;
    private DateStyle dateStyle;
    private DateConverter dateConverter;

    public DatePickerFragment()
    {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateConverter = DateConverter.instance;
    }

    public void setDate(int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTmpData(int[] data)
    {
        tmpData = data;
    }

    public void setButton(Button button)
    {
        this.button = button;
    }

    public void setDateStyle(DateStyle style)
    {
        dateStyle = style;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        tmpData[0] = year;
        tmpData[1] = monthOfYear;
        tmpData[2] = dayOfMonth;
        button.setText(dateConverter.convert(dateStyle, year, monthOfYear, dayOfMonth));
    }
}
