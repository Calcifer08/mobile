package ru.mirea.lozhnichenkoas.serviceapp;

import static android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.lozhnichenkoas.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = "MUSIC";
    private int PermissionCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Разрешения получены");
        } else {
            Log.d(TAG, "Нет разрешений");
            ActivityCompat.requestPermissions(this, new String[]
                    {POST_NOTIFICATIONS, FOREGROUND_SERVICE_MEDIA_PLAYBACK}, PermissionCode);
        }
    }

    public void onClickPlay(View view) {
        Intent serviceIntent = new Intent(MainActivity.this, PlayService.class);
        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
    }

    public void onClickPause(View view) {
        stopService(new Intent(MainActivity.this, PlayService.class));
    }
}