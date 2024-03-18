package ru.mirea.lozhnichenkoas.mireaproject.ui.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentAudioBinding;

public class AudioFragment extends Fragment {

    private final String TAG = "Record";
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork;
    private boolean isStartPlaying = true;
    private boolean isStartRecording = true;
    private String recordFilePath;
    private Button recordButton;
    private Button playButton;
    private Button buttunSpeed1x;
    private Button buttunSpeed0x;
    private Button buttunSpeed2x;
    private MediaPlayer player;
    private MediaRecorder recorder;
    private FragmentAudioBinding fragmentAudioBinding;

    // включить 1 и 3 переключатель в настройках микрофона (3 точки над телефоном)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_audio, container, false);
        fragmentAudioBinding = FragmentAudioBinding.inflate(getLayoutInflater());
        View root = fragmentAudioBinding.getRoot();

        recordButton = fragmentAudioBinding.buttonRec;
        playButton = fragmentAudioBinding.buttonPlay;
        playButton.setEnabled(false);
        buttunSpeed1x = fragmentAudioBinding.buttonSpeed1x;
        buttunSpeed0x = fragmentAudioBinding.buttonSpeed0x;
        buttunSpeed2x = fragmentAudioBinding.buttonSpeed2x;
        recordFilePath = (new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();

        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        buttunSpeed1x.setOnClickListener(v -> setPlaybackSpeed(1.0f));
        buttunSpeed0x.setOnClickListener(v -> setPlaybackSpeed(0.5f));
        buttunSpeed2x.setOnClickListener(v -> setPlaybackSpeed(2.0f));
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

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!isWork) {
            requireActivity().finish();  //если разрешения не дали - закрываем активити
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

    private void setPlaybackSpeed(float speed) {
        if (player != null) {
            // устанавливаем новые параметры (получили их и изменили скорость)
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
        }
    }
}