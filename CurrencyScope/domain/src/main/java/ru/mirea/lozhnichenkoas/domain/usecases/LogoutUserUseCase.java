package ru.mirea.lozhnichenkoas.domain.usecases;

import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;

public class LogoutUserUseCase {
    private UserRepository userRepository;

    public LogoutUserUseCase(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void execute(){
        userRepository.signOutUser();
    }
}
