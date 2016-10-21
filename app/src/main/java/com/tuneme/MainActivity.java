package com.tuneme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.tuneme.listener.*;

public class MainActivity extends AppCompatActivity {

    private ClickListener clickListener;
    Button button_start;
    Button button_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickListener = new ClickListener(this);

        //get views of activity here
        button_start = (Button) findViewById(R.id.button_start);
        button_stop = (Button) findViewById(R.id.button_stop);

        //register Listeners
        button_start.setOnClickListener(clickListener);
        button_stop.setOnClickListener(clickListener);

    }
}
