package ru.mirea.lozhnichenkoas.timeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;

import ru.mirea.lozhnichenkoas.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String host = "time.nist.gov";
    private final int port= 13;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String timeResult = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); //пропускаем первую строку
                timeResult = reader.readLine();
                Log.d("Socket", timeResult);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.textView.setText(parseDateTime(result));
        }
    }

    private String parseDateTime(String dateTime) {
        String[] strings = dateTime.split(" ");
        String date = strings[1];
        String time = strings[2];
        String result = "Дата: " + date + "\nВремя: " + time;
        return result ;
    }
}