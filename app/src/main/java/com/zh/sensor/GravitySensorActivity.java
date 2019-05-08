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

import com.zh.sensor.databinding.ActivityGravitySensorBinding;

public class GravitySensorActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityGravitySensorBinding binding;

    private SensorManager manager;
    private Sensor mSensor;

    public static void launch(Context context){
        context.startActivity(new Intent(context,GravitySensorActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_gravity_sensor);
        binding.setPresenter(this);
        manager= (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensor=manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        manager.registerListener(this,mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (manager!=null){
            manager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values=event.values;
        StringBuilder sb=new StringBuilder();
        sb.append("\nx轴"+values[0]);
        sb.append("\ny轴"+values[1]);
        sb.append("\nz轴"+values[2]);
        binding.tvValue.setText(sb.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
