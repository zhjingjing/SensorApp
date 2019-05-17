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
                //以下是位置传感器


                //方向传感器
                if (sensorList.get(position).getType()==Sensor.TYPE_ORIENTATION){
                    OrientationSensorActivity.launch(MainActivity.this);
                }else if (sensorList.get(position).getType()==Sensor.TYPE_GAME_ROTATION_VECTOR){
                    //游戏旋转矢量传感器
                    PositionSensorsActivity.launch(MainActivity.this);
                }else if (sensorList.get(position).getType()==Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR){
                    //地磁场旋转矢量传感器
                    PositionSensorsActivity.launch(MainActivity.this);
                }else if (sensorList.get(position).getType()==Sensor.TYPE_MAGNETIC_FIELD){
                    //磁场
                    PositionSensorsActivity.launch(MainActivity.this);
                }else if (sensorList.get(position).getType()==Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED){
                    //未校准的磁场
                    PositionSensorsActivity.launch(MainActivity.this);
                }else if (sensorList.get(position).getType()==Sensor.TYPE_PROXIMITY){
                    //接近传感器返回距离
                    PositionSensorsActivity.launch(MainActivity.this);
                }

                //以下是motion 传感器
                //重力
                else if (sensorList.get(position).getType()==Sensor.TYPE_GRAVITY){
                    GravitySensorActivity.launch(MainActivity.this);
                }
                //加速度计
                else if (sensorList.get(position).getType()==Sensor.TYPE_ACCELEROMETER){
                    AccelerometerSensorActivity.launch(MainActivity.this);
                }
                //旋转矢量
                else if (sensorList.get(position).getType()==Sensor.TYPE_ROTATION_VECTOR){
                     RotationVectorDemo.launch(MainActivity.this);
                }
                //陀螺仪
                else if (sensorList.get(position).getType()==Sensor.TYPE_GYROSCOPE){
                    GyroscopeSensorActivity.launch(MainActivity.this);
                }


                //以下是环境传感器

                //光
                else if (sensorList.get(position).getType()==Sensor.TYPE_LIGHT){
                    EnvironmentActivity.launch(MainActivity.this);
                }
                //气压
                else if (sensorList.get(position).getType()==Sensor.TYPE_PRESSURE){
                    EnvironmentActivity.launch(MainActivity.this);
                }
                //湿度
                else if (sensorList.get(position).getType()==Sensor.TYPE_RELATIVE_HUMIDITY){
                    EnvironmentActivity.launch(MainActivity.this);
                }
                //设备温度 api 14弃用
                else if (sensorList.get(position).getType()==Sensor.TYPE_TEMPERATURE){
                    EnvironmentActivity.launch(MainActivity.this);
                }
                //环境温度
                else if (sensorList.get(position).getType()==Sensor.TYPE_AMBIENT_TEMPERATURE){
                    EnvironmentActivity.launch(MainActivity.this);
                }

                //其他传感器
                //心率传感器必须设置权限 body_sensors 否则sensorList不会返回。
                // 描述心率监测器的常数。
                //当精度为 SENSOR_STATUS_UNRELIABLE或时SENSOR_STATUS_NO_CONTACT，应丢弃心率值。
                // 报告的值是以每分钟节拍为单位的心率
                else if (sensorList.get(position).getType()==Sensor.TYPE_HEART_RATE){
                    HeartRateActivity.launch(MainActivity.this);
                }
            }
        });
    }

}
