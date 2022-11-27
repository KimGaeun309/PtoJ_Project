package com.example.ptoj_project;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Handler;
import android.os.Message;

import android.content.Intent;

import java.util.TimeZone;
import java.util.Date;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView dateView, timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show date and time
        dateView = (TextView)findViewById(R.id.view_date);
        timeView = (TextView)findViewById(R.id.view_time);
        setDateTime();

        // move to menu page (임시로 버튼을 통해 페이지 이동하도록 구현)
        Button button = (Button) findViewById(R.id.btn_menu);
        button.setOnClickListener(this);
    }

    public void setDateTime() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm");
                dateFormat.setTimeZone(tz);
                timeFormat.setTimeZone(tz);
                Date date = new Date();

                dateView.setText(dateFormat.format(date));
                timeView.setText(timeFormat.format(date));
            }
        };
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    handler.sendEmptyMessage(1);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}