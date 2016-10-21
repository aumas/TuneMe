package com.tuneme.bin;

import android.hardware.Sensor;

/**
 * Created by jianxhe on 10/16/2016.
 * This class is used for Accelerometer Data
 */

public class SensorAcc extends SensorData {
    public SensorAcc(float[] axis, float timestamp) {
        super(axis, timestamp);
        type = Sensor.TYPE_ACCELEROMETER;
    }
}
