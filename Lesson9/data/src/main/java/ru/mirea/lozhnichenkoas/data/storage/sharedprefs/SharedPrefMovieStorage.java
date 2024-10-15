package ru.mirea.lozhnichenkoas.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

import ru.mirea.lozhnichenkoas.data.storage.MovieStorage;
import ru.mirea.lozhnichenkoas.data.storage.models.Movie;

public class SharedPrefMovieStorage implements MovieStorage {
    private SharedPreferences preferences;
    private String KeyMovieName = "MOVIE";
    private String KeyMovieID = "IDMovie";
    private String KeyMovieDate = "DateMovie";

    public SharedPrefMovieStorage(Context context) {
        preferences = context.getSharedPreferences("Lesson9", Context.MODE_PRIVATE);
    }

    @Override
    public boolean save(Movie movie) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KeyMovieName, movie.getName());
        editor.putString(KeyMovieDate, LocalDate.now().toString());
        editor.putInt(KeyMovieID, movie.getId());
        editor.apply();
        return true;
    }

    @Override
    public Movie get() {
        String nameMovie = preferences.getString(KeyMovieName, null);
        String dateMovie = preferences.getString(KeyMovieDate, LocalDate.now().toString());
        int idMovie = preferences.getInt(KeyMovieID, 0);
        return new Movie(idMovie, nameMovie, dateMovie);
    }
}
