package ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import ru.mirea.lozhnichenkoas.currencyscope.presentation.AuthActivity;
import ru.mirea.lozhnichenkoas.currencyscope.presentation.MainActivity;
import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.usecases.AddFavoriteCurrencyUseCase;
import ru.mirea.lozhnichenkoas.domain.usecases.RemoveFavoriteCurrencyUseCase;

public class CurrencyDetailsFragment extends Fragment {
    private static final String ARG_CURRENCY = "CURRENCY";
    private boolean isAuth = false;
    private CurrencyRepository currencyRepository;
    private Currency currency;
    private AddFavoriteCurrencyUseCase addFavoriteCurrencyUseCase;
    private RemoveFavoriteCurrencyUseCase removeFavoriteCurrencyUseCase;
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

    public static CurrencyDetailsFragment newInstance(
            Currency currency, CurrencyRepository currencyRepository, boolean isAuth) {
        CurrencyDetailsFragment fragment = new CurrencyDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENCY, currency);
        fragment.currencyRepository = currencyRepository;
        fragment.isAuth = isAuth;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currency = (Currency) getArguments().getSerializable(ARG_CURRENCY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_details, container, false);

        addFavoriteCurrencyUseCase = new AddFavoriteCurrencyUseCase(currencyRepository);
        removeFavoriteCurrencyUseCase = new RemoveFavoriteCurrencyUseCase(currencyRepository);

        textCurrencyName = view.findViewById(R.id.textCurrencyName);
        imageCurrencyIcon = view.findViewById(R.id.imageCurrencyIcon);
        textCurrencyCharCode = view.findViewById(R.id.textCurrencyCharCode);
        textNominalValue = view.findViewById(R.id.textNominalValue);
        textCurrencyRateValue = view.findViewById(R.id.textCurrencyRateValue);
        textCurrencyChange = view.findViewById(R.id.textCurrencyChange);
        buttonAddToFavorites = view.findViewById(R.id.buttonAddToFavorites);
        buttonDeleteFromFavorites = view.findViewById(R.id.buttonDeleteFromFavorites);
        editTextAmountLeft = view.findViewById(R.id.editTextAmountLeft);
        textCharCodeLeft = view.findViewById(R.id.textCharCodeLeft);
        imageLeft = view.findViewById(R.id.imageLeft);
        textAmountRight = view.findViewById(R.id.textAmountRight);
        textCharCodeRight = view.findViewById(R.id.textCharCodeRight);
        imageRight = view.findViewById(R.id.imageRight);
        buttonConvert = view.findViewById(R.id.buttonConvert);
        buttonSwapCurrencies = view.findViewById(R.id.buttonSwapCurrencies);
        buttonGoBack = view.findViewById(R.id.buttonGoBack);

        if (currency != null) {
            textCurrencyName.setText(currency.getName());
            imageCurrencyIcon.setImageResource(currency.getIconId());
            textCurrencyCharCode.setText(currency.getCharCode());
            textNominalValue.setText(String.valueOf(currency.getNominal()));
            textCurrencyRateValue.setText(String.valueOf(currency.getValue()));
            calculateCoefficient();
            textAmountRight.setText(String.valueOf(currency.getValue()));
            textCharCodeRight.setText(currency.getCharCode());
            imageRight.setImageResource(currency.getIconId());


            buttonAddToFavorites.setOnClickListener(v -> addToFavorites());

            buttonDeleteFromFavorites.setOnClickListener(v -> deleteFromFavorites());

            buttonConvert.setOnClickListener(v -> convertCurrency());

            buttonSwapCurrencies.setOnClickListener(v -> swapCurrencies());

            buttonGoBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

            if (isAuth) {
                checkIsFavorite(currency.getCharCode());
            }
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            String email = data.getStringExtra("EMAIL");
            isAuth = true;

            ((MainActivity) getActivity()).setAuthState(true, email);
        }
    }

    public void updateAuthState(boolean isAuth) {
        this.isAuth = isAuth;
        buttonAddToFavorites.setVisibility(View.VISIBLE);
        buttonDeleteFromFavorites.setVisibility(View.GONE);
    }

    private void checkIsFavorite(String charCode){
        List<String> favoriteCurrencies = currencyRepository.getFavoriteCurrencies();
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

    private void addToFavorites(){
        if (isAuth) {
            addFavoriteCurrencyUseCase.execute(currency.getCharCode());
            updateUIButtonFavorite(true);
            Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    private void deleteFromFavorites() {
        if (isAuth) {
            removeFavoriteCurrencyUseCase.execute(currency.getCharCode());
            updateUIButtonFavorite(false);
            Toast.makeText(requireContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            startActivityForResult(intent, 1);
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
            course = inputValue / currency.getValue();
        } else {
            course = inputValue * currency.getValue();
        }
        textAmountRight.setText(String.format("%.4f", course));
    }
}