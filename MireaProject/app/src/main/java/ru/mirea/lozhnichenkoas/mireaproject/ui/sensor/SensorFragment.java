package ru.mirea.lozhnichenkoas.mireaproject.ui.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentSensorBinding;


public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private TextView textViewHeigh;
    private TextView textViewBar;
    private float seaLevelPressure = 1013.25f; // Стандартное атмосферное давление на уровне моря в гПа
    private FragmentSensorBinding fragmentSensorBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_sensor, container, false);

        fragmentSensorBinding = FragmentSensorBinding.inflate(inflater, container, false);
        View root = fragmentSensorBinding.getRoot();

        textViewHeigh = fragmentSensorBinding.textViewHeigh;
        textViewBar = fragmentSensorBinding.textViewBar;

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
// https://herramientasingenieria.com/onlinecalc/altitude/altitude.html проверить высоту
    @Override
    public void onSensorChanged(SensorEvent event) {
        float pressureValue = event.values[0];
        float altitude = SensorManager.getAltitude(seaLevelPressure, pressureValue);
        String formattedAltitude = String.format("%.0f", altitude);
        textViewHeigh.setText("Высота: " + formattedAltitude + " м");
        textViewBar.setText("Давление: " + pressureValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}