package ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;
import ru.mirea.lozhnichenkoas.domain.usecases.LoginUserUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.RegisterUserUseCase;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<String> authMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMutableLiveData = new MutableLiveData<>();
    private RegisterUserUseCase registerUserUseCase;
    private LoginUserUseCase loginUserUseCase;

    public AuthViewModel(UserRepository userRepository) {
        this.registerUserUseCase = new RegisterUserUseCase(userRepository);
        this.loginUserUseCase = new LoginUserUseCase(userRepository);
    }

    public MutableLiveData<String> getAuthMutableLiveData() {
        return authMutableLiveData;
    }

    public MutableLiveData<String> getErrorMutableLiveData() {
        return errorMutableLiveData;
    }

    public void register(String email, String password) {
        registerUserUseCase.execute(email, password, new AuthCallback() {
            @Override
            public void onSuccess(String email) {
                authMutableLiveData.setValue(email);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorMutableLiveData.setValue(errorMessage);
            }
        });
    }

    public void login(String email, String password) {
        loginUserUseCase.execute(email, password, new AuthCallback() {
            @Override
            public void onSuccess(String email) {
                authMutableLiveData.setValue(email);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorMutableLiveData.setValue(errorMessage);
            }
        });
    }
}
