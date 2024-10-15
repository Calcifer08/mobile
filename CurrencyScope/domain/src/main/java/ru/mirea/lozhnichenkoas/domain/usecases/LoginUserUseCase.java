package ru.mirea.lozhnichenkoas.domain.usecases;

import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;

public class LoginUserUseCase {
    private UserRepository userRepository;

    public LoginUserUseCase(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void execute(String email, String password, AuthCallback callback){
        userRepository.signInUser(email, password, callback);
    }
}
