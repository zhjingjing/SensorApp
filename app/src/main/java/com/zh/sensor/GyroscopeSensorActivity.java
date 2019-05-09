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
import android.widget.TextView;

import com.zh.sensor.databinding.ActivityGyroscopeSensorBinding;

public class GyroscopeSensorActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityGyroscopeSensorBinding binding;
    private SensorManager manager;
    private Sensor gyroscope;
    public static void launch(Context context){
        context.startActivity(new Intent(context,GyroscopeSensorActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_gyroscope_sensor);
        binding.setPresenter(this);

        manager= (SensorManager) getSystemService(SENSOR_SERVICE);
        if (manager!=null){
          gyroscope= manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (manager!=null&&gyroscope!=null){
            manager.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            float[] values = event.values;
            StringBuilder sb=new StringBuilder();
            sb.append("x轴:"+values[0]+"\n");
            sb.append("y轴:"+values[1]+"\n");
            sb.append("z轴:"+values[2]+"\n");
            binding.tvValue.setText(sb.toString());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
