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

import com.zh.sensor.databinding.ActivityHeartRateBinding;

public class HeartRateActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityHeartRateBinding binding;
    private SensorManager manager;
    public static void launch(Context context){
        context.startActivity(new Intent(context,HeartRateActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_heart_rate);
        binding.setPresenter(this);
        manager= (SensorManager) getSystemService(SENSOR_SERVICE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager!=null){
            Sensor heartRate=manager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            if (heartRate!=null){
                manager.registerListener(this,heartRate,SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_HEART_RATE){
            float[] values=event.values;
            StringBuilder sb=new StringBuilder();
            sb.append("心率：");
            for (int i=0;i<values.length;i++){
                sb.append("\nvalues["+i+"]="+values[i]);
            }
            binding.tvHeartRate.setText(sb.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
