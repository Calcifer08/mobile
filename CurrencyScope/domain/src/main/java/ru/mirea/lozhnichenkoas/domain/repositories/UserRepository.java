package ru.mirea.lozhnichenkoas.domain.repositories;

import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;

public interface UserRepository {
    void registerUser(String email, String password, AuthCallback callback);
    void signInUser(String email, String password, AuthCallback callback);
    void signOutUser();
    void autoSignIn(AuthCallback callback);
}
