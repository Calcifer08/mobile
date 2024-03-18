package ru.mirea.lozhnichenkoas.sharer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivitySEND extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TEXT, "MIREA");
        startActivity(Intent.createChooser(intent, "Выбор за Вами!"));
    }

    public void onBack(View view) {
        startActivity(new Intent(ActivitySEND.this, MainActivity.class));
    }
}