package ru.mirea.lozhnichenkoas.lesson9.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.lozhnichenkoas.domain.models.Movie;
import ru.mirea.lozhnichenkoas.domain.repository.MovieRepository;
import ru.mirea.lozhnichenkoas.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.SaveFilmToFavoriteUseCase;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    private MovieRepository movieRepository;
    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MutableLiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    };

    public void SaveMovie(Movie movie) {
        Boolean result = new SaveFilmToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(result.toString());
    }

    public void getMovie() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("Save result %s", movie.GetName()));
    }
}
