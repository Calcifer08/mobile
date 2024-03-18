package ru.mirea.lozhnichenkoas.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.mirea.lozhnichenkoas.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        SharedPreferences preferences = getSharedPreferences("Lesson6", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText textGroup = activityMainBinding.editTextGroup;
        EditText textList = activityMainBinding.editTextList;
        EditText textFilm = activityMainBinding.editTextFilm;
        Button button = activityMainBinding.button;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("GROUP", textGroup.getText().toString());
                editor.putString("LIST", textList.getText().toString());
                editor.putString("FILM", textFilm.getText().toString());
                editor.apply();
                Toast.makeText(MainActivity.this, "Сохранено", Toast.LENGTH_SHORT).show();
            }
        });

        textGroup.setText(preferences.getString("GROUP", ""));
        textList.setText(preferences.getString("LIST", ""));
        textFilm.setText(preferences.getString("FILM", ""));
    }
}