package com.example.ctrl_c;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;




public class alarm_setting extends AppCompatActivity {

    TimePicker timePicker;

    Button btn_save;

    EditText editText;

    Intent intent;

    DBOpenHelper dbOpenHelper;

    long id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        timePicker = findViewById(R.id.timepicker);
        btn_save = findViewById(R.id.btn_save);
        editText = findViewById(R.id.ctrlc_alarm);
        dbOpenHelper = new DBOpenHelper(this);

        intent = getIntent();

        final Intent intent = getIntent();

        final String type = intent.getStringExtra("Change type");

        if (type.equals("change")) {
            id = intent.getLongExtra("alarm id", 0);
            String alarmName = intent.getStringExtra("alarm name");
            int alarmHour = intent.getIntExtra("alarm hour", 0);
            int alarmMin = intent.getIntExtra("alarm min", 0);

            editText.setText(alarmName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(alarmHour);
                timePicker.setMinute(alarmMin);
            } else {
                timePicker.setCurrentHour(alarmHour);
                timePicker.setCurrentMinute(alarmMin);
            }
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0;
                int minute = 0;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                String alarmname = editText.getText().toString();
                dbOpenHelper.open("alarm");
                if (type.equals("add")) {
                    AlarmData alarmData = new AlarmData();
                    alarmData.setAlarmName(alarmname);
                    alarmData.setHour(hour);
                    alarmData.setMin(minute);
                    alarmData.setOnOff(true);

                    //추가
                    dbOpenHelper.insertAlarm(alarmData);
                }
                else {
                    //수정
                    AlarmData alarmData = new AlarmData();
                    alarmData.setAlarmName(alarmname);
                    alarmData.setHour(hour);
                    alarmData.setMin(minute);
                    alarmData.setOnOff(true);

                    dbOpenHelper.updateAlarm(id, alarmData);
                }

                intent.putExtra("alarmChangeType", type);
                intent.putExtra("alarm name", alarmname);
                intent.putExtra("alarm hour", hour);
                intent.putExtra("alarm min", minute);
                setResult(2000, intent);
                finish();

            }
        });
    }

    @Override
    protected void onPause() {
        dbOpenHelper.close("alarm");
        super.onPause();
    }
    @Override
    protected void onResume() {
        dbOpenHelper.open("alarm");
        super.onResume();
    }
}
