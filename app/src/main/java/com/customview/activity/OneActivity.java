package com.customview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.customview.R;

/**
 *
 */
public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        Intent intent = getIntent();
        if(intent==null)
            return;
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

    }
}
