package com.tuneme.bin;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StepDetector implements Runnable {
    float[] oriValues = new float[3];
    final int valueNum = 4; //用于存放计算阈值的波峰波谷差值
    float[] tempValue = new float[valueNum];
    int tempCount = 0; //是否上升的标志位
    boolean isDirectionUp = false; //持续上升次数
    int continueUpCount = 0; //上一点的持续上升的次数，为了记录波峰的上升次数
    int continueUpFormerCount = 0; //上一点的状态，上升还是下降
    boolean lastStatus = false; //波峰值
    float peakOfWave = 0; //波谷值
    float valleyOfWave = 0; //此次波峰的时间
    float timeOfThisPeak = 0; //上次波峰的时间
    float timeOfLastPeak = 0; //当前的时间
    float timeOfNow = 0; //当前传感器的值
    float gravityNew = 0; //上次传感器的值
    float gravityOld = 0; //动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    final float initialValue = (float) 1.3; //初始阈值
    float ThreadValue = (float) 2.0;
    //private StepListener mStepListeners;
    private Context context;
    public static int CURRENT_STEP = 0;
    private float timestamp;

    public StepDetector(){}
    public StepDetector(Context context){this.context = context;}

    public void detect(String input) {
        String values[] = input.split(",");
        int type;
        type = Integer.parseInt(values[1]);
        if (type != Sensor.TYPE_ACCELEROMETER){return;}
        timestamp = Float.parseFloat(values[0]);
        oriValues[0] = Float.parseFloat(values[2]);
        oriValues[1] = Float.parseFloat(values[3]);
        oriValues[2] = Float.parseFloat(values[4]);

        gravityNew = (float) Math.sqrt(oriValues[0] * oriValues[0]
                + oriValues[1] * oriValues[1] + oriValues[2] * oriValues[2]);
        DetectorNewStep(gravityNew);
    }
    /*
    * 检测步子，并开始计步
    * 1.传入sersor中的数据
    * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
    * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
    * */
    public void DetectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (DetectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = timestamp;
                if (timeOfNow - timeOfLastPeak >= 500
                        && (peakOfWave - valleyOfWave >= ThreadValue)) {
                    timeOfThisPeak = timeOfNow;
/*
* 更新界面的处理，不涉及到算法
* 一般在通知更新界面之前，增加下面处理，为了处理无效运动：
* 1.连续记录10才开始计步
* 2.例如记录的9步用户停住超过3秒，则前面的记录失效，下次从头开始
* 3.连续记录了9步用户还在运动，之前的数据才有效
* */
                    //mStepListeners.onStep();
                    CURRENT_STEP += 1;
                    Log.i("CURRENT_STEP",String.valueOf(CURRENT_STEP));
                }
                if (timeOfNow - timeOfLastPeak >= 500
                        && (peakOfWave - valleyOfWave >= initialValue)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = Peak_Valley_Thread(peakOfWave - valleyOfWave);
                }
            }
        }
        gravityOld = values;
    }
    /*
    * 检测波峰
    * 以下四个条件判断为波峰：
    * 1.目前点为下降的趋势：isDirectionUp为false
    * 2.之前的点为上升的趋势：lastStatus为true
    * 3.到波峰为止，持续上升大于等于2次
    * 4.波峰值大于20
    * 记录波谷值 * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
    * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
    * */
    public boolean DetectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }

        if (!isDirectionUp && lastStatus && (continueUpFormerCount >= 2 || oldValue >= 20)) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }
    /*
    * 阈值的计算
    * 1.通过波峰波谷的差值计算阈值
    * 2.记录4个值，存入tempValue[]数组中
    * 3.在将数组传入函数averageValue中计算阈值
    * */
    public float Peak_Valley_Thread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < valueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, valueNum);
            for (int i = 1; i < valueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[valueNum - 1] = value;
        }
        return tempThread;
    }
    /*
    * 梯度化阈值
    * 1.计算数组的均值
    * 2.通过均值将阈值梯度化在一个范围里
    * */
    public float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }
        return ave;
    }

    @Override
    public void run() {
        String message;
        try {
            FileInputStream fileInputStream = context.openFileInput("acc3.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message=bufferedReader.readLine())!=null){
                detect(message);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

