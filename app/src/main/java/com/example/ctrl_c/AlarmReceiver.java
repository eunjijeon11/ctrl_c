package com.example.ctrl_c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        int requestCode = intent.getIntExtra("requestCode", 0);
        int startH = intent.getIntExtra("hour", 0);
        int startM = intent.getIntExtra("min", 0);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1: //일요일
                dayOfWeek = 6;
            case 2: //월요일
                dayOfWeek = 0;
            case 3: //화요일
                dayOfWeek = 1;
            case 4: //수요일
                dayOfWeek = 2;
            case 5: //목요일
                dayOfWeek = 3;
            case 6: //금요일
                dayOfWeek = 4;
            case 7: //토요일
                dayOfWeek = 5;
            default:
                dayOfWeek = 0;
        }

        if (type.equals("class")) { //수업 알람
            ArrayList<SubjectData> classes = (ArrayList<SubjectData>)intent.getSerializableExtra("classes");
            assert classes != null;
            if (!classes.get(dayOfWeek).getSubject().equals("none")) { //수업이 있으면
                Intent mIntent = new Intent(context, AlarmActivity.class);
                mIntent.putExtra("alarmType", "class");
                mIntent.putExtra("alarmName", classes.get(dayOfWeek).getSubject());
                mIntent.putExtra("alarmID", classes.get(dayOfWeek).getID());
                mIntent.putExtra("alarmPW", classes.get(dayOfWeek).getPW());
                mIntent.putExtra("setTime", intent.getLongExtra("time", 0));
            }
        } else if (type.equals("alarm")) {
            //일반 알람
        }

        //내일로 다시설정
        alarmUtil myAlarmUtil = new alarmUtil(context, type);
        myAlarmUtil.setAlarm(requestCode, startH, startM, true);
    }
}
