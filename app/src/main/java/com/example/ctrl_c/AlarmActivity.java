package com.example.ctrl_c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {

    Button btn_alarmOff, btn_startClass;
    TextView tv_alarmName, tv_currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btn_alarmOff = findViewById(R.id.btn_alarmOff);
        btn_startClass = findViewById(R.id.btn_startClass);
        tv_alarmName = findViewById(R.id.tv_alarmInfo);
        tv_currentTime = findViewById(R.id.tv_currentTime);

        Intent intent = getIntent();
        String currentTime = "현재시각 " + intent.getStringExtra("setTime");
        tv_currentTime.setText(currentTime); //시간 설정
        if (intent.getStringExtra("alarmType").equals("alarm")) {
            //순수 알람
            tv_alarmName.setText(intent.getStringExtra("alarmName"));
            btn_startClass.setVisibility(View.GONE);
        } else if (intent.getStringExtra("alarmType").equals("class")) {
            //수업듣기
            String currentClass = "지금은 " + intent.getStringExtra("alarmName") + "시간입니다";
            tv_alarmName.setText(currentClass);
            btn_startClass.setVisibility(View.VISIBLE);
        }

        btn_startClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //줌 이동
            }
        });

        btn_alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //알람 종료
            }
        });
    }
}