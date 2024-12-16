package ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.AuthViewModel;
import ru.mirea.lozhnichenkoas.data.repositories.UserRepositoryImpl;
import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public AuthViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        UserRepository userRepository = new UserRepositoryImpl(context);
        return (T) new AuthViewModel(userRepository);
    }
}
