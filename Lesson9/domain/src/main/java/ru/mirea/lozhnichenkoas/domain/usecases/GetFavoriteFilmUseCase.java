package ru.mirea.lozhnichenkoas.domain.usecases;

import ru.mirea.lozhnichenkoas.domain.models.Movie;
import ru.mirea.lozhnichenkoas.domain.repository.MovieRepository;

public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie execute() {
        return movieRepository.getMovie();
    }
}
