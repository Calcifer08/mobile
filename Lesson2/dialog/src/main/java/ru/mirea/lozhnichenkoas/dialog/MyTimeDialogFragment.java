package ru.mirea.lozhnichenkoas.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

public class MyTimeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance(); // текущее время
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfDay);

                        // вызов снэкбара
                        showSnackbar("Выбранное время: " + selectedTime);
                    }
                },
                hour,
                minute,
                true
        );

        return timePickerDialog;
    }

    private void showSnackbar(String message) {
        View rootView = getActivity().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
