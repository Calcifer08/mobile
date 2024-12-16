package ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.FragmentCurrencyListBinding;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.AuthActivity;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.MainActivity;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter.CurrencyAdapter;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter.CurrencyIconHelper;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.MainViewModel;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory.MainViewModelFactory;
import ru.mirea.lozhnichenkoas.domain.models.Currency;


public class CurrencyListFragment extends Fragment {
    private FragmentCurrencyListBinding binding;
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private CurrencyAdapter currencyAdapter;
    private boolean isAuth = false;


    public CurrencyListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireActivity())).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrencyListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
        setupObservers();

        recyclerView = binding.recyclerCurrencies;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mainViewModel.getAllCurrencies();
    }

    private void setupListeners() {
        binding.buttonUpdateList.setOnClickListener(v -> {
            mainViewModel.getAllCurrencies();
            binding.textTitleList.setText("Валюты");
        });
        binding.buttonFavoriteCurrencies.setOnClickListener(v -> {
            if (isAuth) {
                mainViewModel.showFavoriteCurrencies();
                binding.textTitleList.setText("Избранные валюты");
            } else {
                login();
            }
        });
        binding.buttonSearchByCode.setOnClickListener(v -> {
            String charCode = binding.editSearchByCode.getText().toString().trim().toLowerCase();

            if (charCode.isEmpty()) {
                Toast.makeText(requireActivity(), "Введите код валюты", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.textTitleList.setText("Валюты: \"" + charCode + "\"");
            mainViewModel.showCurrencyByCode(charCode);
        });
        binding.buttonSearchByVoice.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "Заглушка", Toast.LENGTH_SHORT).show();
            //mainViewModel.showCurrencyByVoice();////////////////////////////// дописать
        });
    }

    private void setupObservers() {
        mainViewModel.getCurrenciesMediatorLiveData().observe(getViewLifecycleOwner(), currencyList -> {
            CurrencyIconHelper.setIconsCurrencies(currencyList);
            setAdapterData(currencyList);
        });

        mainViewModel.getUserNameLiveData().observe(getViewLifecycleOwner(), userName -> {
            mainViewModel.getAllCurrencies();
            binding.textTitleList.setText("Валюты");
            isAuth = userName != null;
        });

        mainViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorText -> {
            Toast.makeText(requireActivity(), errorText, Toast.LENGTH_SHORT).show();
        });
    }

    private void login() {
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String email = data.getStringExtra("EMAIL");
            mainViewModel.resultLogin(email);
        }
    }

    private void setAdapterData(List<Currency> currencyList) {
        if (!currencyList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            currencyAdapter = new CurrencyAdapter(currencyList, currency -> openCurrencyDetailsFragment(currency));
            recyclerView.setAdapter(currencyAdapter);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);

        }
    }

    private void openCurrencyDetailsFragment(Currency currency) {
        CurrencyDetailsFragment fragment = CurrencyDetailsFragment.newInstance(currency);
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }
}