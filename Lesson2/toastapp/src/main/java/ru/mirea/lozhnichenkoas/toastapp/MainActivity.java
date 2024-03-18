package ru.mirea.lozhnichenkoas.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext = findViewById(R.id.editTextText);
    }

    public void OnSendToast(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "СТУДЕНТ № 17 ГРУППА БСБО-11-21 Количество символов - " + edittext.length(),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}