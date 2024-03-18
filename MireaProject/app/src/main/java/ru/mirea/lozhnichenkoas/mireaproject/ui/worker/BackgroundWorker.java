package ru.mirea.lozhnichenkoas.mireaproject.ui.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundWorker extends Worker {
    public BackgroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters parameters) {
        super(context, parameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        double number = Double.parseDouble(getInputData().getString("NUMBER"));
        double degree = Double.parseDouble(getInputData().getString("DEGREE"));

        String result = String.valueOf(Math.round(Math.pow(number, degree)));

        Data outData = new Data.Builder()
                .putString("RESULT", result)
                .build();
        return Result.success(outData);
    }
}
