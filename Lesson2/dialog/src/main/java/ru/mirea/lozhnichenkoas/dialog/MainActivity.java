package ru.mirea.lozhnichenkoas.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShowDialog(View view) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!", Toast.LENGTH_LONG).show();
    }

    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!", Toast.LENGTH_LONG).show();
    }

    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!", Toast.LENGTH_LONG).show();
    }


    // TimePickerDialog
    public void onTimePickerDialog(View view) {
        MyTimeDialogFragment timePickerFragment = new MyTimeDialogFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // DatePickerDialog
    public void onDatePickerDialog(View view) {
        MyDateDialogFragment datePickerFragment = new MyDateDialogFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // ProgressDialog
    public void onProgressDialog(View view) {
        MyProgressDialogFragment myProgressDialogFragment = new MyProgressDialogFragment();
        myProgressDialogFragment.show(getSupportFragmentManager(), "progressDialog");
    }
}