package ru.mirea.lozhnichenkoas.domain.repository;

import ru.mirea.lozhnichenkoas.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
