package ru.mirea.lozhnichenkoas.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.lozhnichenkoas.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFileName;
    private EditText editTextCitata;
    private Button buttonWrite;
    private Button buttonRead;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextCitata = binding.editTextCitata;
        editTextFileName = binding.editTextFileName;
        buttonWrite = binding.buttonWrite;
        buttonRead = binding.buttonRead;

        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageWritable()) {
                    String fileName = editTextFileName.getText().toString();
                    String citata = editTextCitata.getText().toString();
                    writeFileToExternalStorage(fileName, citata);
                }
            }
        });

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageWritable()) {
                    String fileName = editTextFileName.getText().toString();
                    readFileFromExternalStorage(fileName);
                }
            }
        });
    }

    //проверка хранилища на чтение и запись
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void writeFileToExternalStorage(String fileName, String citata) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName +".txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            //запись
            output.write(citata);
            output.close();
            Toast.makeText(MainActivity.this, "Цитата добавлена в /storage/emulated/0/Documents", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    public void readFileFromExternalStorage(String fileName) {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName + ".txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            editTextCitata.setText(lines.get(0).toString());
            Log.w("ExternalStorage", String.format("Read from file %s successful", lines.toString()));
        } catch (Exception e) {
            Log.w("ExternalStorage", String.format("Read from file %s failed", e.getMessage()));
        }
    }
}