package com.tuneme.bin;

import android.hardware.Sensor;

/**
 * Created by jianxhe on 10/16/2016.
 * This class is used for Gyroscope data
 */

public class SensorGry extends SensorData {
    public SensorGry(float[] axis, float timestamp) {
        super(axis, timestamp);
        type = Sensor.TYPE_GYROSCOPE;
    }
}
