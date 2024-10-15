package ru.mirea.lozhnichenkoas.currencyscope.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.ActivityAuthBinding;
import ru.mirea.lozhnichenkoas.data.repositories.UserRepositoryImpl;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.AuthCallback;
import ru.mirea.lozhnichenkoas.domain.usecases.LoginUserUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.RegisterUserUseCase;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private RegisterUserUseCase registerUserUseCase;
    private LoginUserUseCase loginUserUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserRepositoryImpl userRepository = new UserRepositoryImpl(this);
        registerUserUseCase = new RegisterUserUseCase(userRepository);
        loginUserUseCase = new LoginUserUseCase(userRepository);

        setupListeners();
    }

    private void setupListeners(){
        binding.buttonRegis.setOnClickListener(v -> registration());
        binding.buttonAuth.setOnClickListener(v -> login());
        binding.buttonBack.setOnClickListener(v -> finish());
    }

    private void registration(){
        String email = binding.editTextEmail.getText().toString().toLowerCase().trim();
        String password = binding.editTextPassword.getText().toString().toLowerCase().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        registerUserUseCase.execute(email, password, new AuthCallback() {
            @Override
            public void onSuccess(String email) {
                Toast.makeText(AuthActivity.this, "Регистрация успешна: " + email, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("EMAIL", email);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AuthActivity.this, "Ошибка регистрации: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(){
        String email = binding.editTextEmail.getText().toString().toLowerCase().trim();
        String password = binding.editTextPassword.getText().toString().toLowerCase().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        loginUserUseCase.execute(email, password, new AuthCallback() {
            @Override
            public void onSuccess(String email) {
                Toast.makeText(AuthActivity.this, "Вход успешен: " + email, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("EMAIL", email);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AuthActivity.this, "Ошибка входа: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}