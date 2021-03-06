package com.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.customview.activity.FourActivity;
import com.customview.activity.OneActivity;
import com.customview.activity.ThreeActivity;
import com.customview.activity.TwoActivity;

public class MainActivity extends AppCompatActivity {

    String[] arrays = {"可点击的文本，类似于验证码",
            "图片加文字，可点击",
            "圆形进度条",
            "音量控制"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.lv_id);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrays));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this,OneActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this,TwoActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this,ThreeActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this,FourActivity.class);
                        break;
                }
                intent.putExtra("title",arrays[position]);
                startActivity(intent);
            }
        });
    }
}
