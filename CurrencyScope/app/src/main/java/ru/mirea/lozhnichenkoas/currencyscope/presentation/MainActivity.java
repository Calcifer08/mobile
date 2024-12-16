package ru.mirea.lozhnichenkoas.currencyscope.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.ActivityMainBinding;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter.CurrencyAdapter;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter.CurrencyIconHelper;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments.CurrencyDetailsFragment;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments.CurrencyListFragment;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments.NavigationFragment;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.MainViewModel;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory.MainViewModelFactory;

import ru.mirea.lozhnichenkoas.domain.models.Currency;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new CurrencyListFragment())
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.navigationFragmentContainerView, new NavigationFragment())
                    .commit();
        }

        mainViewModel = new ViewModelProvider(this,
                new MainViewModelFactory(this)).get(MainViewModel.class);

        mainViewModel.autoSign();
    }
}