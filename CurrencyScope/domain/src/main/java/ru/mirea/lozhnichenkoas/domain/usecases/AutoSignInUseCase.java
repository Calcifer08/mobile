package ru.mirea.lozhnichenkoas.domain.usecases;


import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;

public class AutoSignInUseCase {
    private UserRepository userRepository;

    public AutoSignInUseCase(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void execute(AuthCallback callback){
        userRepository.autoSignIn(callback);
    }
}
