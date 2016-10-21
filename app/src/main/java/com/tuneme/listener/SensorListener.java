package com.tuneme.listener;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.tuneme.bin.*;
import com.tuneme.bin.SensorData;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by jianxhe on 10/16/2016.
 */

public class SensorListener extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor step_detector;
    private BlockingQueue<SensorData> shared_queue = new ArrayBlockingQueue<SensorData>(10);

    //onStartCommand is called by a system every time a client explicitly starts the service by calling startService(Intent)
    @Override
    public int onStartCommand(Intent intent,int flag, int startId){
        Log.i("SensorListener","Register Listeners");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope     = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        step_detector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, step_detector,SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY; //used for services that are explicitly started and stopped as needed
    }

    @Override
    public IBinder onBind(Intent intent){
        // TODO: Return the communication channel to the service
        throw  new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onSensorChanged(SensorEvent event){
        Log.i("Test", "Sensor Event");
        //Producer producer;
        Sensor sensor = event.sensor;

        //TEST---
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            Log.i("step detector:",event.values.toString());
        }
        Producer producer= new Producer(shared_queue,sensor.getType(), event.values, event.timestamp);
        Consumer consumer = new Consumer(shared_queue, getApplicationContext());//getApplicationContext() returns the context of the single, global Application object of the current process.

        //creating Producer, Consumer Thread
        Thread prodThread = new Thread(producer);
        Thread consThread = new Thread(consumer);

        //starting Consumer Thread
        consThread.start();
        prodThread.start();
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, "Service done!", Toast.LENGTH_SHORT).show();
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, gyroscope);
        stopSelf();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
