package com.tuneme.bin;
import android.hardware.Sensor;
import android.util.Log;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{
    private float axis[];
    private float timestamp;
    private int type;
    private BlockingQueue<SensorData> shared_queue;

    public Producer(BlockingQueue<SensorData> shared_queue,int type,float axis[], float timestamp){
        this.shared_queue = shared_queue;
        this.type = type;
        this.axis = axis;
        this.timestamp = timestamp;
    }
    @Override
    public void run(){
        try{
            shared_queue.put(produce(type, axis, timestamp));
        }catch (InterruptedException ex){
            //handle the exception
        }
    }

    public static SensorData produce(int type, float axis[], float timestamp){
        SensorData sensor_data;
        if(type == Sensor.TYPE_ACCELEROMETER){
            sensor_data = new SensorAcc(axis, timestamp);
        }else if(type == Sensor.TYPE_GYROSCOPE) {
            sensor_data = new SensorGry(axis, timestamp);
        }else if(type == Sensor.TYPE_STEP_DETECTOR) {
            Log.i("test",axis.toString());
            sensor_data = null;
        }else{
            sensor_data = null;
        }

        return sensor_data;
    }

}