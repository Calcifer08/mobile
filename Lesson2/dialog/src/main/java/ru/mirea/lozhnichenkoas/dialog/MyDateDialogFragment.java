package ru.mirea.lozhnichenkoas.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

public class MyDateDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance(); // текущая дата
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

                        // вызов снэкбара
                        showSnackbar("Выбранная дата: " + selectedDate);
                    }
                },
                year,
                month,
                day
        );

        return datePickerDialog;
    }

    private void showSnackbar(String message) {
        View rootView = getActivity().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}