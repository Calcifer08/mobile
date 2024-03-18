package ru.mirea.lozhnichenkoas.looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import ru.mirea.lozhnichenkoas.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private String TAG = "lesson 4 Main";
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG,msg.getData().getString("RESULT"));
                Log.d(TAG,msg.getData().getString("DATA"));
            }
        };
        MyLooper myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.text.setText("Мой номер по списку № 17");
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("AGE", binding.editTextAge.getText().toString());
                bundle.putString("WORK", binding.editTextWork.getText().toString());
                bundle.putLong("TIME", System.currentTimeMillis());
                msg.setData(bundle);
                myLooper.mHandler.sendMessage(msg);
            }
        });
    }
}