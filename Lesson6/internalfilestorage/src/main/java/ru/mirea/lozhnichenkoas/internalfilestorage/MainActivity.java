package ru.mirea.lozhnichenkoas.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.lozhnichenkoas.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private String fileName = "date_russia.txt";
    private Button buttonWrite;
    private Button buttonRead;
    private TextView textView;
    private EditText editText;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textView = binding.textView;
        editText = binding.editTextText;
        buttonWrite = binding.buttonWrite;
        buttonRead = binding.buttonRead;

        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    fileOutputStream.write(editText.getText().toString().getBytes());
                    fileOutputStream.close();
                    Toast.makeText(MainActivity.this, "Записано", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(getTextFromFile());
                            }
                        });
                    }
                }).start();
            }
        });
    }

    // открытие файла
    public String getTextFromFile() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(fileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            String text = new String(bytes);
            Log.d("READ", text);
            return text;
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }
}