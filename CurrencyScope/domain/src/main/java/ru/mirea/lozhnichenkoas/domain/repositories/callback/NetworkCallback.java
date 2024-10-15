package ru.mirea.lozhnichenkoas.domain.repositories.callback;

public interface NetworkCallback<T> {
    void onResponse(T result);
    void onFailure(Throwable throwable);
}
