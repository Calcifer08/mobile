package ru.mirea.lozhnichenkoas.intentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        int i = 17*17;
        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        textView.setText("КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ: " + i +
                ", а текущее время: " + intent.getStringExtra("times"));
    }
}