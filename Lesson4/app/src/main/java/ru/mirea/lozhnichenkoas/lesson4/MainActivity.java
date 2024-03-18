package ru.mirea.lozhnichenkoas.lesson4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import ru.mirea.lozhnichenkoas.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isPlay = true;
    String[] Music = {"Песня 1", "Песня 2", "Песня 3"};
    Integer i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView.setText(Music[0]);
    }

    public void onPlay(View view) {
        if (isPlay) {
            binding.play.setImageResource(android.R.drawable.ic_media_pause);
            isPlay = false;
        } else {
            binding.play.setImageResource(android.R.drawable.ic_media_play);
            isPlay = true;
        }
    }

    public void onNext (View view) {
        i++;
        if (i >= Music.length) {
            i = 0;
        }
        binding.textView.setText(Music[i]);
    }

    public void onBack (View view) {
        i--;
        if (i < 0) {
            i = Music.length - 1;
        }
        binding.textView.setText(Music[i]);
    }
}