package com.zh.sensor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.zh.sensor.databinding.ActivityAccelerometerSensorBinding;

import java.lang.ref.WeakReference;

/**
 * 加速度传感器
 * 模拟微信摇一摇
 */
public class AccelerometerSensorActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityAccelerometerSensorBinding binding;

    private SensorManager sensorManager;
    private Sensor mSensor;

    //开始摇一摇
    private static final int START_SHAKE = 0x1;
    //结束
    private static final int END_SHAKE = 0x2;
    private MyHandler mHandler;

    public static void launch(Context context){
        context.startActivity(new Intent(context,AccelerometerSensorActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding= DataBindingUtil. setContentView(this,R.layout.activity_accelerometer_sensor);
         binding.setPresenter(this);
        mHandler = new MyHandler(this);
         sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
         mSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager!=null){
            sensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }


    private boolean isShake;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];

            if ((Math.abs(x)>17||Math.abs(y)>17||Math.abs(z)>17)&&!isShake){
                isShake=true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            //展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            //上下展开停留时间。
                            Thread.sleep(1500);
                            //结束摇一摇，合并上下两部分，隐藏line
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private static class MyHandler extends Handler {
        private WeakReference<AccelerometerSensorActivity> mReference;
        private AccelerometerSensorActivity mActivity;
        public MyHandler(AccelerometerSensorActivity activity) {
            mReference = new WeakReference<AccelerometerSensorActivity>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    mActivity.startAnimation(false);//两张图片分散开的动画
                    break;
                case END_SHAKE:
                    //整体效果结束,
                    mActivity.isShake = false;
                    // 展示上下两种图片合并的效果
                    mActivity.startAnimation(true);
                    break;
            }
        }
    }

    /**
     * 开启 摇一摇动画
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        binding.tvLineTop.setVisibility(View.VISIBLE);
        binding.tvLineBottom.setVisibility(View.VISIBLE);
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;

        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);

        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);


        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
                    binding.tvLineTop.setVisibility(View.GONE);
                    binding.tvLineBottom.setVisibility(View.GONE);
                }
            });
        }
        //设置动画
        binding.llTop.startAnimation(topAnim);
        binding.llBottom.startAnimation(bottomAnim);

    }
}
