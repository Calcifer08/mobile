package ru.mirea.lozhnichenkoas.lesson5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.mirea.lozhnichenkoas.lesson5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ListView listSensor = binding.sensorList;

        // создаем список для отображения в ListView найденных датчиков
        ArrayList<HashMap<String, Object>> arrayList= new ArrayList<>();
        for (int i = 0; i < sensors.size(); i++) {
            HashMap<String, Object> sensorTypeList = new HashMap<>();
            sensorTypeList.put("NAME", sensors.get(i).getName());
            sensorTypeList.put("VALUE", sensors.get(i).getMaximumRange());
            arrayList.add(sensorTypeList);
        }
        // создаем адаптер и устанавливаем тип адаптера - отображение двух полей
        SimpleAdapter simpleAdapter =
                new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[] {"NAME", "VALUE"},
                        new int[] {android.R.id.text1, android.R.id.text2});
        listSensor.setAdapter(simpleAdapter);
    }
}