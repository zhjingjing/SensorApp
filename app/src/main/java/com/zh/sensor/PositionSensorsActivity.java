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

import com.zh.sensor.databinding.ActivityPositionSensorsBinding;

public class PositionSensorsActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityPositionSensorsBinding binding;
    private SensorManager sensorManager;
    public static void launch(Context context){
        context.startActivity(new Intent(context,PositionSensorsActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_position_sensors);
        binding.setPresenter(this);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager!=null){
            Sensor gameRotation=sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
            if (gameRotation!=null){
                sensorManager.registerListener(this,gameRotation,SensorManager.SENSOR_DELAY_NORMAL);
            }
            Sensor proximity=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            if (proximity!=null){
                sensorManager.registerListener(this,proximity,SensorManager.SENSOR_DELAY_NORMAL);
            }

            Sensor geomagnetic=sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
            if (geomagnetic!=null){
                sensorManager.registerListener(this,geomagnetic,SensorManager.SENSOR_DELAY_NORMAL);
            }
            Sensor magnetic_field=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (magnetic_field!=null){
                sensorManager.registerListener(this,magnetic_field,SensorManager.SENSOR_DELAY_NORMAL);
            }
            Sensor magnetic_field_uncalinrated=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
            if (magnetic_field_uncalinrated!=null){
                sensorManager.registerListener(this,magnetic_field_uncalinrated,SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type=event.sensor.getType();
        float[] values= event.values;
        if (type==Sensor.TYPE_GAME_ROTATION_VECTOR){
            StringBuilder sb=new StringBuilder();
            sb.append("游戏旋转矢量传感器：");
            sb.append("\nx轴："+values[0]);
            sb.append("\ny轴："+values[1]);
            sb.append("\nz轴："+values[2]);
            binding.tvGameRotationVector.setText(sb.toString());
        }else if (type==Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR){
            StringBuilder sb=new StringBuilder();
            sb.append("地磁旋转矢量传感器：");
            sb.append("\nx轴："+values[0]);
            sb.append("\ny轴："+values[1]);
            sb.append("\nz轴："+values[2]);
            binding.tvGeomagneticRotationVector.setText(sb.toString());

        }else if (type==Sensor.TYPE_MAGNETIC_FIELD){
            StringBuilder sb=new StringBuilder();
            sb.append("地磁场传感器：");
            sb.append("\nx轴："+values[0]);
            sb.append("\ny轴："+values[1]);
            sb.append("\nz轴："+values[2]);

            binding.tvMagneticField.setText(sb.toString());

        }else if (type==Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED){
            StringBuilder sb=new StringBuilder();
            sb.append("地磁场传感器：");
            sb.append("\n沿x轴的地磁场强度："+values[0]);
            sb.append("\n沿y轴的地磁场强度："+values[1]);
            sb.append("\n沿z轴的地磁场强度："+values[2]);
            sb.append("\n沿x轴的铁偏差估计："+values[3]);
            sb.append("\n沿y轴的铁偏差估计："+values[4]);
            sb.append("\n沿z轴的铁偏差估计："+values[5]);
            binding.tvMagneticFieldUncalibrated.setText(sb.toString());
        }else if (type==Sensor.TYPE_PROXIMITY){
            StringBuilder sb=new StringBuilder();
            sb.append("接近传感器：");
            sb.append("\n距离："+values[0]);
            sb.append("\n传感器的最大范围："+event.sensor.getMaximumRange());
            binding.tvProximity.setText(sb.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //精度发生变化
    }
}
