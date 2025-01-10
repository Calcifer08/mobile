package ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.FragmentCurrencyListBinding;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.FragmentProfileBinding;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.AuthActivity;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.MainViewModel;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory.MainViewModelFactory;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private MainViewModel mainViewModel;
    private ActivityResultLauncher<Intent> loginActivityLauncher;


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireActivity())).get(MainViewModel.class);

        loginActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String email = result.getData().getStringExtra("EMAIL");
                        mainViewModel.resultLogin(email);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
        setupObservers();
    }

    private void setupListeners() {
        binding.buttonLogout.setOnClickListener(v -> logout());
        binding.buttonLogin.setOnClickListener(v -> login());
    }

    private void setupObservers() {
        mainViewModel.getUserNameLiveData().observe(getViewLifecycleOwner(), userName -> {
            updateUIAfterLogin(userName);
        });

        mainViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorText -> {
            Toast.makeText(requireActivity(), errorText, Toast.LENGTH_SHORT).show();
        });
    }

    private void login() {
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        loginActivityLauncher.launch(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String email = data.getStringExtra("EMAIL");
            mainViewModel.resultLogin(email);
        }
    }

    private void logout() {
        mainViewModel.logout();
        Toast.makeText(requireActivity(), "Выход выполнен", Toast.LENGTH_SHORT).show();
    }

    private void updateUIAfterLogin(String userName) {
        if (userName != null) {
            binding.textViewUserName.setText(userName);
            binding.buttonLogin.setVisibility(View.GONE);
            binding.buttonLogout.setVisibility(View.VISIBLE);
        } else {
            binding.textViewUserName.setText("Гость");
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.buttonLogout.setVisibility(View.GONE);
        }
    }
}