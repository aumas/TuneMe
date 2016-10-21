package com.tuneme.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.util.Log;
import android.view.View;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.tuneme.R;
import com.tuneme.service.StepDetectorService;

/**
 * Created by jianxhe on 10/16/2016.
 * This class is used for all Click Listener
 */

public class ClickListener extends Activity implements View.OnClickListener {
    private Context context;

    public ClickListener(Context context){
        this.context = context;
    }

    /**
     * react Buttion Click
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                actionStart();
                break;
            case R.id.button_stop:
                actionStop();
                break;
            default:
                break;
        }
    }

    /**
     * action for buttion Start
     */
    private void actionStart(){
        //start SensorListener
        Log.i("ClickListener","Start");
        Intent intentStart = new Intent(context, SensorListener.class);
        context.startService(intentStart);
    }

    /**
     * action for button Stop
     */
    private void actionStop(){
        read();
        Toast.makeText(context, "Stop", Toast.LENGTH_SHORT).show();

        StepDetectorService stepDetectorService = new StepDetectorService(context);
        Intent intentDetector = new Intent(this,stepDetectorService.getClass());
        context.startService(intentDetector);

        Intent intentStop = new Intent(context,SensorListener.class);
        context.stopService(intentStop);

    }

    private void read() {
    }
}
