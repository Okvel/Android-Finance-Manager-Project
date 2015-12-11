package net.lion.okvel.financemanager.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import net.lion.okvel.financemanager.entity.DateConverter;
import net.lion.okvel.financemanager.entity.DateStyle;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
        GregorianCalendar calendar = new GregorianCalendar(year, month, day, 23, 59, 59);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
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
