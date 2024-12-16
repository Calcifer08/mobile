package ru.mirea.lozhnichenkoas.currencyscope.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.mirea.lozhnichenkoas.currencyscope.R;

public class NavigationFragment extends Fragment {

    public NavigationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonOpenFragmentCurrencyList = view.findViewById(R.id.buttonOpenFragmentCurrencyList);
        Button buttonOpenFragmentProfile = view.findViewById(R.id.buttonOpenFragmentProfile);

        buttonOpenFragmentCurrencyList.setOnClickListener(v -> {
            Fragment existingFragment = getParentFragmentManager().findFragmentByTag(CurrencyListFragment.class.getName());

            if (existingFragment == null) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, new CurrencyListFragment(), CurrencyListFragment.class.getName())
                        .addToBackStack(CurrencyListFragment.class.getName())
                        .commit();
            } else {
                getParentFragmentManager().popBackStack(CurrencyListFragment.class.getName(), 0);
            }
        });

        buttonOpenFragmentProfile.setOnClickListener(v -> {
            Fragment existingFragment = getParentFragmentManager().findFragmentByTag(ProfileFragment.class.getName());

            if (existingFragment == null) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, new ProfileFragment(), ProfileFragment.class.getName())
                        .addToBackStack(ProfileFragment.class.getName())
                        .commit();
            } else {
                getParentFragmentManager().popBackStack(ProfileFragment.class.getName(), 0);
            }
        });
    }
}