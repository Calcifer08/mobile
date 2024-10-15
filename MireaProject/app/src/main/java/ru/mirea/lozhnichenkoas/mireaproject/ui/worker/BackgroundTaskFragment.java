package ru.mirea.lozhnichenkoas.mireaproject.ui.worker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentBackgroundTaskBinding;


public class BackgroundTaskFragment extends Fragment {

    private FragmentBackgroundTaskBinding backgroundTaskBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BackgroundTackViewModel backgroundTackViewModel =
                new ViewModelProvider(this).get(BackgroundTackViewModel.class);

        backgroundTaskBinding = FragmentBackgroundTaskBinding.inflate(inflater, container, false);
        View root = backgroundTaskBinding.getRoot();

        final TextView textViewResult = backgroundTaskBinding.textViewDegreeResult;
        final EditText editTextNumber = backgroundTaskBinding.editTextNumber;
        final EditText editTextDegree = backgroundTaskBinding.editTextDegree;
        final Button buttonDegree = backgroundTaskBinding.buttonDegree;

        buttonDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data inputData = new Data.Builder()
                        .putString("NUMBER", editTextNumber.getText().toString())
                        .putString("DEGREE", editTextDegree.getText().toString())
                        .build();

                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                        .setInputData(inputData)
                        .build();
                WorkManager.getInstance(requireContext())
                        .enqueue(oneTimeWorkRequest);

                WorkManager.getInstance(requireContext())
                        .getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                        .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                if (workInfo != null && workInfo.getState().isFinished()) {
                                    String res = workInfo.getOutputData().getString("RESULT");
                                    textViewResult.setText(res);
                                }
                            }
                        });
            }
        });
        return root;
    }
}