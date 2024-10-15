package ru.mirea.lozhnichenkoas.data.storage;

import ru.mirea.lozhnichenkoas.data.storage.models.Movie;

public interface MovieStorage {
    public Movie get();
    public boolean save(Movie movie);
}
