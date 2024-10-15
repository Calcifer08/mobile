package ru.mirea.lozhnichenkoas.domain.usecases;

import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;

public class RegisterUserUseCase {
    private UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void execute(String email, String password, AuthCallback callback){
        userRepository.registerUser(email, password, callback);
    }
}
