package ru.mirea.lozhnichenkoas.sharer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSend(View view) {
        startActivity(new Intent(MainActivity.this, ActivitySEND.class));
    }

    public void onPick(View view) {
        startActivity(new Intent(MainActivity.this, ActivityPICK.class));
    }
}