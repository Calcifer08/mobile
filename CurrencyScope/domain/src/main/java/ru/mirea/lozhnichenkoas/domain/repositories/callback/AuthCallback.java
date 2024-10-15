package ru.mirea.lozhnichenkoas.domain.repositories.callback;

public interface AuthCallback {
    void onSuccess(String email);
    void onFailure(String errorMessage);
}
