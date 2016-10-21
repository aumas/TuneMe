package com.tuneme.bin;

/**
 * Created by jianxhe on 10/16/2016.
 * This is the parent of data from all different Sensors used in this APP
 */

public class SensorData {
    protected float x;
    protected float y;
    protected float z;
    protected float timestamp;
    protected int type;
    public SensorData(float axis[], float timestamp){
        this.x = axis[0];
        this.y = axis[1];
        this.z = axis[2];
        this.timestamp = timestamp;
    }
    public float getx(){
        return x;
    }
    public  float gety(){
        return y;
    }
    public  float getz(){
        return z;
    }
    public  int getType() {
        return type;
    }
    public void setx(float x){
        this.x = x;
    }
    public void sety(float y){
        this.y = y;
    }
    public void setz(float z){
        this.z = z;
    }
    public void setType(int type) {
        this.type = type;
    }

    public void setTimestamp(float timestamp){
        this.timestamp = timestamp;
    }
    public String toString(){
        return String.valueOf(this.type)+ ',' + String.valueOf(this.timestamp) + ',' + String.valueOf(this.x) + ',' + String.valueOf(this.y) + ','+ String.valueOf(this.z) + "\n";
    }
}
