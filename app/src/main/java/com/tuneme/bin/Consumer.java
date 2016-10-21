package com.tuneme.bin;

/**
 * Created by jianxhe on 10/16/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class Consumer extends Activity implements Runnable{
    private BlockingQueue<SensorData> shared_queue;
    private Context context;
    private static final String CONS = "CONSUMER";
    private static final String CONS_DATA = "CONSUMER_DATA";
    private static final String PATH_NAME = "PATH_NAME";
    private File file;

    public Consumer(BlockingQueue<SensorData> shared_queue, Context context){
        this.shared_queue = shared_queue;
        this.context = context;
    }

    @Override
    public void run(){
        SensorData sensor_data = null;
        try {
            sensor_data = shared_queue.take();//retrieves and removes the head of this shared_queue
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(sensor_data == null){

        }else{
            Log.i(CONS, "Consuming");
            Log.i(CONS_DATA, sensor_data.toString());
            String saveText = String.valueOf(sensor_data.toString());
            save(saveText);
        }
    }
    public void save(String saveText){
        try{
            FileOutputStream outStream = context.openFileOutput("acc1.txt", Context.MODE_APPEND);
            //Log.i("path_test0", String.valueOf(context.getFileStreamPath("acc1.txt")));
            outStream.write(saveText.getBytes());
            //flush the content to the underlying stream
            //outStream.flush();
            outStream.close();

        }catch(FileNotFoundException e){


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}