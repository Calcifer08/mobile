package ru.mirea.lozhnichenkoas.mireaproject.ui.сurrency;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mirea.lozhnichenkoas.mireaproject.R;
import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentCurrencyBinding;
import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentProfileBinding;

public class CurrencyFragment extends Fragment {

private FragmentCurrencyBinding fragmentCurrencyBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_currency, container, false);

        fragmentCurrencyBinding = FragmentCurrencyBinding.inflate(inflater, container, false);
        View root = fragmentCurrencyBinding.getRoot();

        return  root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadPageCurrency(fragmentCurrencyBinding).execute("https://www.cbr-xml-daily.ru/daily_json.js");
        } else {
            Toast.makeText(requireContext(), "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }
}