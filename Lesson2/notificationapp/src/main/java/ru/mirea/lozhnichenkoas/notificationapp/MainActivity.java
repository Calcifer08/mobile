package ru.mirea.lozhnichenkoas.notificationapp;

//import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "com.mirea.asd.notification.ANDROID";
    private int PermissionCode = 200;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED) {
            Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения получены");
        } else {
            Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения нет");
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, PermissionCode);
        }
    }

    public void onClickNewMessageNotification(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Успех!")
                .setSmallIcon(androidx.constraintlayout.widget.R.drawable.notification_bg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Гораздо более длинный текст, который не помещается в одну строку"))
                .setContentTitle("MIREA");
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "уведомления о ФИО учащегося", importance);
        channel.setDescription("MIREA channel");

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.createNotificationChannel(channel);
        notificationManagerCompat.notify(1, builder.build());
    }
}