package com.customview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.customview.R;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        Intent intent = getIntent();
        if(intent==null)
            return;
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        findViewById(R.id.civ_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TwoActivity.this, "被点击了！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
