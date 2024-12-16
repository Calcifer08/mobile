package ru.mirea.lozhnichenkoas.resultapifragmentapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DataFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.button);
        EditText editText = view.findViewById(R.id.editTextText);

        button.setOnClickListener(v -> {
            String text = editText.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("KEY", text);
            getChildFragmentManager().setFragmentResult("REQUESTKEY", bundle);

            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getChildFragmentManager(), "ModalBottomSheet");
        });
    }
}