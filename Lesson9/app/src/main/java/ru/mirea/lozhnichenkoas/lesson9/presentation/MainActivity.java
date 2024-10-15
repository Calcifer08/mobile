package ru.mirea.lozhnichenkoas.lesson9.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import ru.mirea.lozhnichenkoas.domain.models.Movie;
import ru.mirea.lozhnichenkoas.lesson9.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        EditText edittext = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        viewModel.getFavoriteMovie().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

//        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(this);
//        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(v -> {
            viewModel.SaveMovie(new Movie(2, edittext.getText().toString()));
//            textView.setText(String.format("Save result %s", result));
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(v -> {
            viewModel.getMovie();
//            textView.setText(movieName);
        });
    }
}