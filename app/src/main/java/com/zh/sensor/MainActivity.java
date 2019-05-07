package com.zh.sensor;

import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.zh.sensor.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private ActivityMainBinding binding;

    private List<Sensor> sensorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_main);
        binding.setPresenter(this);

        mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        // 得到设置支持的所有传感器的List
        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        final List<String> sensorNameList = new ArrayList<String>();
        for (Sensor sensor : sensorList) {
            sensorNameList.add(sensor.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sensorNameList);
        binding.listSensor.setAdapter(adapter);

        binding.listSensor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //方向传感器
                if (sensorList.get(position).getType()==3){
                    OrientationSensorActivity.launch(MainActivity.this);
                }
            }
        });



    }

}
