package ru.mirea.lozhnichenkoas.currencyscope.presentation;

import androidx.appcompat.app.AppCompatActivity;
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

import ru.mirea.lozhnichenkoas.currencyscope.App;
import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.ActivityMainBinding;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter.CurrencyAdapter;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter.CurrencyIconHelper;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments.CurrencyDetailsFragment;
import ru.mirea.lozhnichenkoas.data.repositories.CurrencyRepositoryImpl;
import ru.mirea.lozhnichenkoas.data.repositories.UserRepositoryImpl;
import ru.mirea.lozhnichenkoas.data.sources.NetApi.NetworkApi;
import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.UserRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;
import ru.mirea.lozhnichenkoas.domain.usecases.AutoSignInUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.GetAllCurrenciesUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.GetCurrencyDetailsUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.GetFavoriteCurrenciesUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.LogoutUserUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.SearchCurrencyByCodeUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.SearchCurrencyByVoiceUseCase;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    CurrencyRepository currencyRepository;
    private boolean isAuth = false;
    private AutoSignInUseCase autoSignInUseCase;
    private LogoutUserUseCase logoutUserUseCase;
    private GetAllCurrenciesUseCase getAllCurrenciesUseCase;
    private GetCurrencyDetailsUseCase getCurrencyDetailsUseCase;
    private GetFavoriteCurrenciesUseCase getFavoriteCurrenciesUseCase;
    private SearchCurrencyByCodeUseCase searchCurrencyByCodeUseCase;
    private SearchCurrencyByVoiceUseCase searchCurrencyByVoiceUseCase;
    private RecyclerView recyclerView;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageLoading.setVisibility(View.VISIBLE);

        initializeUseCases();

        autoSign();

        setupListeners();

        recyclerView = binding.recyclerCurrencies;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllCurrencies();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN) {
            hideKeyboard();
        }
        return super.dispatchTouchEvent(ev);
    }
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void initializeUseCases() {
        UserRepository userRepository = new UserRepositoryImpl(this);
        autoSignInUseCase = new AutoSignInUseCase(userRepository);
        logoutUserUseCase = new LogoutUserUseCase(userRepository);

        FavoriteCurrencyDao favoriteCurrencyDao = App.getInstance().getDatabaseProvider().getFavoriteCurrencyDao();
        currencyRepository = new CurrencyRepositoryImpl(new NetworkApi(), favoriteCurrencyDao);
        getAllCurrenciesUseCase = new GetAllCurrenciesUseCase(currencyRepository);
        getCurrencyDetailsUseCase = new GetCurrencyDetailsUseCase(currencyRepository);
        getFavoriteCurrenciesUseCase = new GetFavoriteCurrenciesUseCase(currencyRepository);
        searchCurrencyByCodeUseCase = new SearchCurrencyByCodeUseCase(currencyRepository);
        searchCurrencyByVoiceUseCase = new SearchCurrencyByVoiceUseCase(currencyRepository);
    }

    private void setupListeners(){
        binding.buttonLogout.setOnClickListener(v -> logout());
        binding.buttonLogin.setOnClickListener(v -> login());
        binding.buttonUpdateList.setOnClickListener(v -> getAllCurrencies());
        binding.buttonFavoriteCurrencies.setOnClickListener(v -> showFavoriteCurrencies());
        binding.buttonSearchByCode.setOnClickListener(v -> showCurrencyByCode());
        binding.buttonSearchByVoice.setOnClickListener(v -> showCurrencyByVoice());
    }

    private void autoSign() {
        autoSignInUseCase.execute(new AuthCallback() {
            @Override
            public void onSuccess(String email) {
                isAuth = true;
                updateUIAfterLogin(email);
            }

            @Override
            public void onFailure(String errorMessage) {
                isAuth = false;
                updateUIAfterLogin(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String email = data.getStringExtra("EMAIL");
            isAuth = true;
            updateUIAfterLogin(email);
        }
    }

    public void setAuthState(boolean isAuth, String email) {
        this.isAuth = isAuth;
        updateUIAfterLogin(email);
    }

    private void login(){
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivityForResult(intent, 1);
    }

    private void logout(){
        logoutUserUseCase.execute();
        isAuth = false;
        Toast.makeText(MainActivity.this, "Выход выполнен", Toast.LENGTH_SHORT).show();
        updateUIAfterLogin(null);
        getAllCurrencies();

        CurrencyDetailsFragment fragment = (CurrencyDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frameLayout);

        if (fragment != null) {
            fragment.updateAuthState(false);
        }
    }

    private void updateUIAfterLogin(String userName){
        binding.imageLoading.setVisibility(View.GONE);

        if (userName != null) {
            binding.textNameUser.setText(userName);
            binding.buttonLogin.setVisibility(View.GONE);
            binding.buttonLogout.setVisibility(View.VISIBLE);
        } else {
            binding.textNameUser.setText("Гость");
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.buttonLogout.setVisibility(View.GONE);
        }
    }

    private void setAdapterData(List<Currency> currencyList) {
        if (!currencyList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            currencyAdapter = new CurrencyAdapter(currencyList, currency -> openCurrencyDetailsFragment(currency));
            recyclerView.setAdapter(currencyAdapter);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);/////////////////////////////////////////////////////////////////////

        }
    }

    private void openCurrencyDetailsFragment(Currency currency){
        CurrencyDetailsFragment fragment = CurrencyDetailsFragment.newInstance(currency, currencyRepository, isAuth);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
        binding.frameLayout.setVisibility(View.VISIBLE);
    }

    private void getAllCurrencies() {
        List<Currency> currencyList = getAllCurrenciesUseCase.execute();
        CurrencyIconHelper.setIconsCurrencies(currencyList);

        binding.textTitleList.setText("Валюты");
        setAdapterData(currencyList);
    }

    private void showFavoriteCurrencies(){
        if (isAuth) {
            List<Currency> favoriteCurrencies = getFavoriteCurrenciesUseCase.execute();

            binding.textTitleList.setText("Избранные валюты");
            setAdapterData(favoriteCurrencies);
        } else {
            login();
        }
    }

    private void showCurrencyByCode(){
        String charCode = binding.editSearchByCode.getText().toString().trim().toLowerCase();

        if (charCode.isEmpty()) {
            Toast.makeText(this, "Введите код валюты", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.textTitleList.setText("Валюты: \"" + charCode + "\"");
        List<Currency> currencyList = searchCurrencyByCodeUseCase.execute(charCode);

        setAdapterData(currencyList);
    }

    private void showCurrencyByVoice(){
        Toast.makeText(this, "Заглушка", Toast.LENGTH_SHORT).show();
    }
}