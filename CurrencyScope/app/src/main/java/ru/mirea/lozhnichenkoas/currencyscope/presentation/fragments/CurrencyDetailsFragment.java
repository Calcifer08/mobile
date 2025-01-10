package ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.currencyscope.databinding.FragmentCurrencyDetailsBinding;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.AuthActivity;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.MainViewModel;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.viewmodel.factory.MainViewModelFactory;
import ru.mirea.lozhnichenkoas.domain.models.Currency;

public class CurrencyDetailsFragment extends Fragment {
    private FragmentCurrencyDetailsBinding binding;
    private MainViewModel mainViewModel;
    private static final String ARG_CURRENCY = "CURRENCY";
    private boolean isAuth = false;
    private ActivityResultLauncher<Intent> loginActivityLauncher;

    private Currency currency;
    private TextView textCurrencyName;
    private ImageView imageCurrencyIcon;
    private TextView textCurrencyCharCode;
    private TextView textNominalValue;
    private TextView textCurrencyRateValue;
    private TextView textCurrencyChange;
    private Button buttonAddToFavorites;
    private Button buttonDeleteFromFavorites;
    private EditText editTextAmountLeft;
    private TextView textCharCodeLeft;
    private ImageView imageLeft;
    private TextView textAmountRight;
    private TextView textCharCodeRight;
    private ImageView imageRight;
    private boolean isLeftRUB = true;
    private Button buttonSwapCurrencies;
    private Button buttonConvert;
    private Button buttonGoBack;

    public static CurrencyDetailsFragment newInstance(Currency currency) {
        CurrencyDetailsFragment fragment = new CurrencyDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENCY, currency);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currency = (Currency) getArguments().getSerializable(ARG_CURRENCY);
        }

        mainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

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
        binding = FragmentCurrencyDetailsBinding.inflate(inflater, container, false);

        textCurrencyName = binding.textCurrencyName;
        imageCurrencyIcon = binding.imageCurrencyIcon;
        textCurrencyCharCode = binding.textCurrencyCharCode;
        textNominalValue = binding.textNominalValue;
        textCurrencyRateValue = binding.textCurrencyRateValue;
        textCurrencyChange = binding.textCurrencyChange;
        buttonAddToFavorites = binding.buttonAddToFavorites;
        buttonDeleteFromFavorites = binding.buttonDeleteFromFavorites;
        editTextAmountLeft = binding.editTextAmountLeft;
        textCharCodeLeft = binding.textCharCodeLeft;
        imageLeft = binding.imageLeft;
        textAmountRight = binding.textAmountRight;
        textCharCodeRight = binding.textCharCodeRight;
        imageRight = binding.imageRight;
        buttonConvert = binding.buttonConvert;
        buttonSwapCurrencies = binding.buttonSwapCurrencies;
        buttonGoBack = binding.buttonGoBack;

        if (currency != null) {
            textCurrencyName.setText(currency.getName());
            imageCurrencyIcon.setImageResource(currency.getIconId());
            textCurrencyCharCode.setText(currency.getCharCode());
            textNominalValue.setText(String.valueOf(currency.getNominal()));
            textCurrencyRateValue.setText(String.valueOf(currency.getValue()));
            calculateCoefficient();
            textAmountRight.setText(String.valueOf(currency.getNominal()));
            textCharCodeRight.setText(currency.getCharCode());
            imageRight.setImageResource(currency.getIconId());
            editTextAmountLeft.setText(String.valueOf(currency.getValue()));

            mainViewModel.getUserNameLiveData().observe(getViewLifecycleOwner(), nameUser -> {
                isAuth = nameUser != null;
                if (isAuth) {
                    checkIsFavorite(currency.getCharCode());
                } else {
                    buttonAddToFavorites.setVisibility(View.VISIBLE);
                    buttonDeleteFromFavorites.setVisibility(View.GONE);
                }
            });

            buttonAddToFavorites.setOnClickListener(v -> addToFavorites());

            buttonDeleteFromFavorites.setOnClickListener(v -> deleteFromFavorites());

            buttonConvert.setOnClickListener(v -> convertCurrency());

            buttonSwapCurrencies.setOnClickListener(v -> swapCurrencies());

//            buttonGoBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
            buttonGoBack.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.popBackStack();
            });
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkIsFavorite(String charCode){
        List<String> favoriteCurrencies = mainViewModel.getFavoriteCurrenciesName();
        updateUIButtonFavorite(favoriteCurrencies.contains(charCode));
    }

    private void updateUIButtonFavorite(boolean isFavorite){
        if (isFavorite) {
            buttonAddToFavorites.setVisibility(View.INVISIBLE);
            buttonDeleteFromFavorites.setVisibility(View.VISIBLE);
        } else {
            buttonAddToFavorites.setVisibility(View.VISIBLE);
            buttonDeleteFromFavorites.setVisibility(View.GONE);
        }
    }

    private void addToFavorites(){
        if (isAuth) {
            mainViewModel.addToFavorites(currency.getCharCode());
            updateUIButtonFavorite(true);
            Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            loginActivityLauncher.launch(intent);
        }
    }

    private void deleteFromFavorites() {
        if (isAuth) {
            mainViewModel.deleteFromFavorites(currency.getCharCode());
            updateUIButtonFavorite(false);
            Toast.makeText(requireContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            loginActivityLauncher.launch(intent);
        }
    }

    private void calculateCoefficient(){
        double value = currency.getValue();
        double previous = currency.getPrevious();
        double difference = value - previous;
        double percent = (value / (previous / 100)) - 100;

        if (difference > 0) {
            textCurrencyChange.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            textCurrencyChange.setText("+" + String.format("%.4f", difference) +
                    " " + "+" + String.format("%.4f", percent) + "%");
        } else {
            textCurrencyChange.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            textCurrencyChange.setText("-" + String.format("%.4f", difference) +
                    " " + "-" + String.format("%.4f", percent) + "%");
        }
    }

    private void swapCurrencies(){
        isLeftRUB = !isLeftRUB;
        String leftText = editTextAmountLeft.getText().toString();
        String rightText = textAmountRight.getText().toString();
        editTextAmountLeft.setText(rightText);
        textAmountRight.setText(leftText);

        if (isLeftRUB) {
            imageLeft.setImageResource(R.drawable.rus);
            textCharCodeLeft.setText("RUB");
            imageRight.setImageResource(currency.getIconId());
            textCharCodeRight.setText(currency.getCharCode());
        } else {
            imageLeft.setImageResource(currency.getIconId());
            textCharCodeLeft.setText(currency.getCharCode());
            imageRight.setImageResource(R.drawable.rus);
            textCharCodeRight.setText("RUB");
        }
    }

    private void convertCurrency(){
        String text = editTextAmountLeft.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните поле", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue = Double.parseDouble(text);
        double course;
        if (isLeftRUB) {
            course = (inputValue * currency.getNominal()) / currency.getValue();
        } else {
            course = (inputValue * currency.getValue()) / currency.getNominal();
        }
        textAmountRight.setText(String.format("%.4f", course));
    }
}