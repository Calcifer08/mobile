package ru.mirea.lozhnichenkoas.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .build();
        WorkManager
                .getInstance(this)
                .enqueue(workRequest);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build();

        WorkRequest workRequest1 = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager
                .getInstance(this)
                .enqueue(workRequest1);
    }
}