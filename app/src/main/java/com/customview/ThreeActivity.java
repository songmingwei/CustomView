package com.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.customview.view.CustomProgressBar;

public class ThreeActivity extends AppCompatActivity {

    private CustomProgressBar customProgressBar;
    private int mProgress = 0;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        Intent intent = getIntent();
        if(intent==null)
            return;
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        customProgressBar = (CustomProgressBar) findViewById(R.id.cpb_id);
        progressBar = (ProgressBar) findViewById(R.id.pb_id);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgress<100){
                    mProgress++;
                    customProgressBar.setProgress(mProgress);
                    progressBar.setProgress(mProgress);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
