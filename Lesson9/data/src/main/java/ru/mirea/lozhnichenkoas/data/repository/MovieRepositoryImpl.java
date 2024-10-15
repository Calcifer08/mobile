package ru.mirea.lozhnichenkoas.data.repository;

import java.time.LocalDate;

import ru.mirea.lozhnichenkoas.data.storage.MovieStorage;
import ru.mirea.lozhnichenkoas.data.storage.models.Movie;
import ru.mirea.lozhnichenkoas.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private MovieStorage movieStorage;
    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(ru.mirea.lozhnichenkoas.domain.models.Movie movie) {
        movieStorage.save(mapToStorage(movie));
        return true;
    }

    @Override
    public ru.mirea.lozhnichenkoas.domain.models.Movie getMovie() {
        Movie movie = movieStorage.get();
        return mapToDomain(movie);
    }

    private Movie mapToStorage(ru.mirea.lozhnichenkoas.domain.models.Movie movie){
        String name = movie.GetName();
        return new Movie(2, name, LocalDate.now().toString());
    }

    private ru.mirea.lozhnichenkoas.domain.models.Movie mapToDomain(Movie movie){
        String name = movie.getName();
        return new ru.mirea.lozhnichenkoas.domain.models.Movie(movie.getId(), name);
    }
}
