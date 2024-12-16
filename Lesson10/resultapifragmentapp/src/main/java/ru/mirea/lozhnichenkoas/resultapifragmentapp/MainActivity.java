package ru.mirea.lozhnichenkoas.resultapifragmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, new DataFragment())
                    .commit();
        }

        getSupportFragmentManager().setFragmentResultListener("REQUESTKEY", this, (requestKey, result) -> {
            String text = result.getString("KEY");
            Log.d(BottomSheetFragment.class.getSimpleName(), "Main Activity: " + text);
        });
    }
}