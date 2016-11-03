package com.taozi.news.modules.map.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Tao Yimin on 2016/10/19.
 */
public class MyOrientationListener implements SensorEventListener
{

    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;

    private float lastX ;

    private OnOrientationListener onOrientationListener ;

    public MyOrientationListener(Context context)
    {
        this.context = context;
    }

    // 开始
    public void start()
    {
        // 获得传感器管理器
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null)
        {
            // 获得方向传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        // 注册
        if (sensor != null)
        {//SensorManager.SENSOR_DELAY_UI
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_UI);
        }

    }

    // 停止检测
    public void stop()
    {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            float x = event.values[SensorManager.DATA_X];

            if( Math.abs(x- lastX) > 1.0 )
            {
                onOrientationListener.onOrientationChanged(x);
            }
            lastX = x ;

        }
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener)
    {
        this.onOrientationListener = onOrientationListener ;
    }


    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }
}

