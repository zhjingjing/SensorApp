package com.zh.sensor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zh.sensor.databinding.ActivityOrientationSensorBinding;

public class OrientationSensorActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityOrientationSensorBinding binding;
    private SensorManager manager;
    private Sensor orientationSensor;

    //加速度传感器数据
    private final float[] accelerometerReading = new float[3];
    //地磁传感器数据
    private final float[] magnetometerReading = new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    private final float[] rotationMatrix = new float[9];
    //方向数据
    private final float[] orientationAngles = new float[3];
    public static void launch(Context context){
        context.startActivity(new Intent(context,OrientationSensorActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_orientation_sensor);
        binding.setPresenter(this);

        manager= (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationSensor=manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();

        if (orientationSensor!=null){
            manager.registerListener(this,orientationSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }


        Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            manager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
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
            float[] values = event.values;
            StringBuilder sb=new StringBuilder();
            sb.append("z轴:"+values[0]+"\n");
            sb.append("x轴:"+values[1]+"\n");
            sb.append("y轴:"+values[2]+"\n");
            binding.tvValue.setText("旧API：\n"+sb.toString());

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }
        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateOrientationAngles() {
        // 更新旋转矩阵.
        // 参数1：
        // 参数2 ：将磁场数据转换进实际的重力坐标中,一般默认情况下可以设置为null
        //  参数3：加速度
        // 参数4：地磁
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        //根据旋转矩阵计算设备的方向
        //参数1：旋转数组
        //参数2：模拟方向传感器的数据
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        StringBuilder sb=new StringBuilder();
        if (orientationAngles.length>=3){
            sb.append("z轴:"+orientationAngles[0]+"\n");
            sb.append("x轴:"+orientationAngles[1]+"\n");
            sb.append("y轴:"+orientationAngles[2]+"\n");
            binding.tvValueNew.setText("\n新API:\n"+sb.toString());
        }
    }
}
