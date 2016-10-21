package com.tuneme.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.tuneme.bin.StepDetector;

public class StepDetectorService extends Service {
    Context context;
    public StepDetectorService() {
    }

    public StepDetectorService(Context context) {
        StepDetector stepDetector = new StepDetector(context);
        new Thread(stepDetector).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
