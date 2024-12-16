package ru.mirea.lozhnichenkoas.fragmentmanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_list, new ListFragment(),"list")
                    .add(R.id.fragment_container_detail, new DetailsFragment(),"details")
                    .commit();
        }
    }
}