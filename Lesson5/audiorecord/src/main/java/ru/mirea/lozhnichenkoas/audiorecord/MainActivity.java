package ru.mirea.lozhnichenkoas.audiorecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import ru.mirea.lozhnichenkoas.audiorecord.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Record";
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork;
    private boolean isStartPlaying = true;
    private boolean isStartRecording = true;
    private String recordFilePath;
    private Button recordButton;
    private Button playButton;
    private MediaPlayer player;
    private MediaRecorder recorder;
    private ActivityMainBinding binding;
    // включить 1 и 3 переключатель в настройках микрофона (3 точки над телефоном)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recordButton = binding.buttonRec;
        playButton = binding.buttonPlay;
        playButton.setEnabled(false);
        recordFilePath = (new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();

        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRecording) {
                    recordButton.setText("Запись остановлена");
                    playButton.setEnabled(false);
                    startRecording();
                } else {
                    recordButton.setText("Начать запись");
                    playButton.setEnabled(true);
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartPlaying) {
                    playButton.setText("Остановить воспроизведение");
                    recordButton.setEnabled(false);
                    startPlaying();
                } else {
                    playButton.setText("Начать воспроизведение");
                    recordButton.setEnabled(true);
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if	(requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!isWork) {
            finish(); //если разрешения не дали - закрываем активити
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d(TAG, "startRecording prepare() FAIL");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.d(TAG, "startPlaying prepare() FAIL");
        }
    }

    private void stopPlaying() {
        player.stop();
        player.release();
        player = null;
    }
}