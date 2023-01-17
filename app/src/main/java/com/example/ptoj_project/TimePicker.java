package com.example.ptoj_project;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.AccessController;

public class TimePicker extends AppCompatActivity {

    private ArcArrSingleton myArcAttr;
    android.widget.TimePicker time_picker;
    TextView tv_time;
    TextView tv_howlong;
    Spinner spinner_hour;
    Spinner spinner_min;
    Button btn_apply;
    int idx;
    int startMin;
    int sweepMin;

    Integer[] howlong_hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    Integer[] howlong_mins = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        myArcAttr = ArcArrSingleton.getInstance();

        Intent myintent = getIntent();
        idx = myintent.getIntExtra("idx", -1);



        time_picker = (android.widget.TimePicker) findViewById(R.id.time_picker);
        time_picker.setIs24HourView(true);
        tv_time = findViewById(R.id.tv_time);
        tv_howlong = findViewById(R.id.tv_howlong);
        spinner_hour = findViewById(R.id.spinner_hour);
        spinner_min = findViewById(R.id.spinner_min);

        time_picker.setOnTimeChangedListener(new android.widget.TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(android.widget.TimePicker timePicker, int hour, int minute) {
                // 오전 / 오후 를 확인하기 위한 if 문
                if (hour > 12) {
                    tv_time.setText("오후 " + (hour - 12) + "시 " + minute + "분 선택");
                } else {
                    tv_time.setText("오전 " + hour + "시 " + minute + "분 선택");
                }

                startMin = 60 * hour + minute;
            }
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, howlong_hours);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, howlong_mins);
        adapter2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_min.setAdapter(adapter2);

        spinner_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                sweepMin = 60 * howlong_hours[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sweepMin = 0;
            }
        });

        spinner_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sweepMin += howlong_mins[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void btnClick(View view) {
    if (view.getId() == R.id.btn_apply) {
        int ret;
        ret = myArcAttr.isMinAvailable(startMin, sweepMin);
        if (ret != 0) {
            Toast.makeText(getApplicationContext(), "이미 해당 시간에 루틴이 존재합니다 " + ret , Toast.LENGTH_SHORT).show();
            return;
        }

        myArcAttr.addArc(startMin, sweepMin, idx);
        Intent intent = new Intent(TimePicker.this, MenuActivity.class);
        startActivity(intent);
    }
    else if (view.getId() == R.id.btn_exit) {
        Intent intent = new Intent(TimePicker.this, MenuActivity.class);
        startActivity(intent);
    }

    }
}