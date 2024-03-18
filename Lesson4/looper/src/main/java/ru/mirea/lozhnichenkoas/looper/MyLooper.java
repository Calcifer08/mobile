package ru.mirea.lozhnichenkoas.looper;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

public class MyLooper extends Thread{
    private String TAG = "lesson 4 MyLooper";
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper (Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d(TAG, "run loop");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String dataAge = msg.getData().getString("AGE");
                String dataWork = msg.getData().getString("WORK");
                long dataTime = msg.getData().getLong("TIME");

                long count = System.currentTimeMillis() - dataTime;
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("RESULT", String.format(("Результат вычисления задержки %d"), count));
                bundle.putString("DATA", String.format(("Возраст: %s, Работа: %s"), dataAge, dataWork));
                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}
