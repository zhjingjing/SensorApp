package com.zh.sensor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zh.sensor.databinding.ActivityEnvironmentBinding;

/**
 * 环境传感器
 * 包括温度，气压，光，湿度
 */
public class EnvironmentActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityEnvironmentBinding binding;
    private SensorManager sensorManager;
    private Sensor sensor_light;//光
    private Sensor sensor_temperature;//设备温度
    private Sensor sensor_temperature_ambient;//环境温度
    private Sensor sensor_humidity;//湿度
    private Sensor sensor_pressure;

    public static void launch(Context context){
        context.startActivity(new Intent(context,EnvironmentActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_environment);
        binding.setPresenter(this);

        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager==null)
            return;
        sensor_light=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_temperature=sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        sensor_temperature_ambient=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensor_humidity=sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensor_pressure=sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager==null)
            return;
        if (sensor_light!=null){
            sensorManager.registerListener(this,sensor_light,SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (sensor_humidity!=null){
            sensorManager.registerListener(this,sensor_humidity,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensor_pressure!=null){
            sensorManager.registerListener(this,sensor_pressure,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensor_temperature!=null){
            sensorManager.registerListener(this,sensor_temperature,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensor_temperature_ambient!=null){
            sensorManager.registerListener(this,sensor_temperature_ambient,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        if (sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type=event.sensor.getType();

        if (type==Sensor.TYPE_LIGHT){
            binding.tvLight.setText("光"+event.values[0]+"LX");
        }else  if (type==Sensor.TYPE_TEMPERATURE){
            binding.tvLight.setText("设备温度"+event.values[0]+"℃");
        }else  if (type==Sensor.TYPE_AMBIENT_TEMPERATURE){
            binding.tvLight.setText("环境温度"+event.values[0]+"℃");
        }else  if (type==Sensor.TYPE_RELATIVE_HUMIDITY){
            binding.tvLight.setText("湿度"+event.values[0]+"％");
        }else  if (type==Sensor.TYPE_PRESSURE){
            binding.tvLight.setText("气压"+event.values[0]+"");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
