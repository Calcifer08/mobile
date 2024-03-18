package ru.mirea.lozhnichenkoas.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.lozhnichenkoas.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClick(View view) {
        new Thread(new Runnable() { // фоновый процесс
            @Override
            public void run() {
                if (binding.editTextPair.length() > 0 && binding.editTextMonth.length() > 0) {
                    Integer pair = Integer.parseInt(binding.editTextPair.getText().toString());
                    Integer month = Integer.parseInt(binding.editTextMonth.getText().toString());

                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (month == 0) {
                        runOnUiThread(() -> binding.textView.setText("Число дней не может быть равно 0"));
                        return;
                    }
                    int i = (int) Math.round((double) pair / month);

                    // runOnUiThread(() ->   ибо нельза из фона обновить ui
                    runOnUiThread(() -> binding.textView.setText("Среднее число пар в день = " + i));
                } else {
                    runOnUiThread(() -> binding.textView.setText("Заполните все поля"));
                }
            }
        }).start();
    }
}