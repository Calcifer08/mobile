package ru.mirea.lozhnichenkoas.currencyscope.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.ActivityAuthBinding;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.AuthViewModel;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory.AuthViewModelFactory;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private AuthViewModel authViewModel;
    private boolean isRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this,
                new AuthViewModelFactory(this)).get(AuthViewModel.class);

        loadPicasso();

        setupListeners();
        setupObservers();
    }

    private void loadPicasso(){
        Picasso.get()
                .load("https://icon-icons.com/downloadimage.php?id=81499&root=1153/PNG/512/&file=1486564179-finance-saving_81499.png")
                .error(R.drawable.default_currency)
                .placeholder(R.drawable.default_currency)
                .into(binding.imageView);

    }

    private void setupListeners(){
        binding.buttonRegis.setOnClickListener(v -> registration());
        binding.buttonAuth.setOnClickListener(v -> login());
        binding.buttonBack.setOnClickListener(v -> finish());
    }

    private void setupObservers(){
        authViewModel.getAuthMutableLiveData().observe(this, email -> {
            if (email != null) {
                textToastSuccess(email);
                Intent intent = new Intent();
                intent.putExtra("EMAIL", email);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        authViewModel.getErrorMutableLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                textToastFailure(errorMessage);
            }
        });
    }

    private void registration(){
        String email = binding.editTextEmail.getText().toString().toLowerCase().trim();
        String password = binding.editTextPassword.getText().toString().toLowerCase().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        isRegis = true;
        authViewModel.register(email, password);
    }

    private void login(){
        String email = binding.editTextEmail.getText().toString().toLowerCase().trim();
        String password = binding.editTextPassword.getText().toString().toLowerCase().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        isRegis = false;
        authViewModel.login(email, password);
    }

    private boolean validateInputs(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void textToastSuccess(String email){
        if (isRegis) {
            Toast.makeText(AuthActivity.this, "Регистрация успешна: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AuthActivity.this, "Вход успешен: " + email, Toast.LENGTH_SHORT).show();
        }
    }

    private void textToastFailure(String errorMessage){
        if (isRegis) {
            Toast.makeText(AuthActivity.this, "Ошибка регистрации: " + errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AuthActivity.this, "Ошибка входа: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}