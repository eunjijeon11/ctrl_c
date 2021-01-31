package com.example.ctrl_c;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;


public class alarm_setting extends AppCompatActivity {

    TimePicker timePicker;

    Button btn_save;

    EditText editText;

    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        timePicker=findViewById(R.id.timepicker);
        btn_save=findViewById(R.id.btn_save);
        editText=findViewById(R.id.ctrlc_alarm);

        intent=getIntent();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int hour=timePicker.getHour();
                    int minute=timePicker.getMinute();
                }
                else {
                    int hour=timePicker.getCurrentHour();
                    int minute=timePicker.getCurrentMinute();
                }

                String alarmname=editText.getText().toString();
                if(intent.getStringExtra("alarmsetType").equals("add alarm")){
                    //추가

                }
                else{
                    //수정
                }
            }
        });
    }
}
