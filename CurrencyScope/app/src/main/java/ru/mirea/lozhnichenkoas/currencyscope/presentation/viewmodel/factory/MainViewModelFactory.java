package ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.lozhnichenkoas.currencyscope.App;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.MainViewModel;
import ru.mirea.lozhnichenkoas.data.repositories.UserRepositoryImpl;
import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public MainViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        UserRepository userRepository = new UserRepositoryImpl(context);
        FavoriteCurrencyDao favoriteCurrencyDao = App.getInstance().getDatabaseProvider().getFavoriteCurrencyDao();
        return (T) new MainViewModel(userRepository, favoriteCurrencyDao);
    }
}
