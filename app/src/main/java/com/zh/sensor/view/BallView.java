package com.zh.sensor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.zh.sensor.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * create by zj on 2019/5/7
 * 重力感应球
 */
public class BallView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener,Runnable {


    /** 游戏画笔 **/
    Paint mPaint = null;

    /**重力感应X轴 Y轴 Z轴的重力值**/
    private float mGX = 0;
    private float mGY = 0;
    private float mGZ = 0;

    /**手机屏幕宽高**/
    int mScreenWidth = 0;
    int mScreenHeight = 0;
    /**小球资源文件越界区域**/
    private int mScreenBallWidth = 0;
    private int mScreenBallHeight = 0;

    /**游戏背景文件**/
    private Bitmap mbitmapBg;

    /**小球资源文件**/
    private Bitmap mbitmapBall;

    /**小球的坐标位置**/
    private float mPosX = 50;
    private float mPosY = 0;

    SurfaceHolder mSurfaceHolder = null;
    /**每50帧刷新一次屏幕**/
    public static final int TIME_IN_FRAME = 50;

    /** 游戏画布 **/
    Canvas mCanvas = null;

    /**控制游戏循环**/
    boolean mIsRunning = false;

    /**SensorManager管理器**/
    private SensorManager mSensorMgr = null;
    Sensor mSensor = null;

    public BallView(Context context) {
        super(context);
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /** 设置当前View拥有控制焦点 **/
        this.setFocusable(true);
        /** 设置当前View拥有触摸事件 **/
        this.setFocusableInTouchMode(true);
        /** 拿到SurfaceHolder对象 **/
        mSurfaceHolder = this.getHolder();
        /** 将mSurfaceHolder添加到Callback回调函数中 **/
        mSurfaceHolder.addCallback(this);
        /** 创建画布 **/
        mCanvas = new Canvas();
        /** 创建曲线画笔 **/
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(24);


        /**加载小球资源**/
        mbitmapBall = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_ball);
        /**加载游戏背景**/
        mbitmapBg = BitmapFactory.decodeResource(this.getResources(), R.mipmap.bg_ball);

        /**得到SensorManager对象**/
        mSensorMgr = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        // 注册listener，第三个参数是检测的精确度
        //SENSOR_DELAY_FASTEST 最灵敏 因为太快了没必要使用
        //SENSOR_DELAY_GAME    游戏开发中使用
        //SENSOR_DELAY_NORMAL  正常速度
        //SENSOR_DELAY_UI    最慢的速度
        mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        mGX = event.values[SensorManager.DATA_X];
        mGY= event.values[SensorManager.DATA_Y];
        mGZ = event.values[SensorManager.DATA_Z];

        //这里乘以2是为了让小球移动的更快
        mPosX -= mGX * 2;
        mPosY += mGY * 2;

        //检测小球是否超出边界
        if (mPosX < 0) {
            mPosX = 0;
        } else if (mPosX > mScreenBallWidth) {
            mPosX = mScreenBallWidth;
        }
        if (mPosY < 0) {
            mPosY = 0;
        } else if (mPosY > mScreenBallHeight) {
            mPosY = mScreenBallHeight;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        /**开始游戏主循环线程**/
        mIsRunning = true;
        new Thread(this).start();
        /**得到当前屏幕宽高**/
        mScreenWidth = this.getWidth();
        mScreenHeight = this.getHeight();

        /**得到小球越界区域**/
        mScreenBallWidth = mScreenWidth - mbitmapBall.getWidth();
        mScreenBallHeight = mScreenHeight - mbitmapBall.getHeight();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning=false;
    }

    @Override
    public void run() {
        while (mIsRunning) {

            /** 取得更新游戏之前的时间 **/
            long startTime = System.currentTimeMillis();

            /** 在这里加上线程安全锁 **/
            synchronized (mSurfaceHolder) {
                /** 拿到当前画布 然后锁定 **/
                mCanvas = mSurfaceHolder.lockCanvas();
                draw();
                /** 绘制结束后解锁显示在屏幕上 **/
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }

            /** 取得更新游戏结束的时间 **/
            long endTime = System.currentTimeMillis();

            /** 计算出游戏一次更新的毫秒数 **/
            int diffTime = (int) (endTime - startTime);

            /** 确保每次更新时间为50帧 **/
            while (diffTime <= TIME_IN_FRAME) {
                diffTime = (int) (System.currentTimeMillis() - startTime);
                /** 线程等待 **/
                Thread.yield();
            }

        }
    }

    public void draw() {
        /**绘制游戏背景**/
        RectF rectF = new RectF(0, 0, mScreenWidth, mScreenHeight);
        mCanvas.drawBitmap(mbitmapBg, null, rectF, null);
        /**绘制小球**/
        mCanvas.drawBitmap(mbitmapBall, mPosX, mPosY, mPaint);
        /**X轴 Y轴 Z轴的重力值**/
        mCanvas.drawText("X轴重力值 ：" + mGX, 0, 20, mPaint);
        mCanvas.drawText("Y轴重力值 ：" + mGY, 0, 40, mPaint);
        mCanvas.drawText("Z轴重力值 ：" + mGZ, 0, 60, mPaint);
    }
}
