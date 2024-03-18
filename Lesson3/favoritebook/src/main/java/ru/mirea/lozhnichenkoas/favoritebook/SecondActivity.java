package ru.mirea.lozhnichenkoas.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.stream.Stream;

public class SecondActivity extends AppCompatActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        editText = findViewById(R.id.editTextText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView ageView = findViewById(R.id.textViewBookDev);
            String university = extras.getString(MainActivity.KEY);
            ageView.setText(String.format("Моя любимая книга: %s", university));
        }
    }

    public void onSave(View view) {
        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, editText.getText().toString());
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}