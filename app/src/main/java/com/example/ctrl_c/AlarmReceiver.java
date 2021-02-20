package com.example.ctrl_c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "호출 성공", Toast.LENGTH_SHORT).show();
        String type = intent.getStringExtra("type");

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = 0;
        Log.e("day of week", calendar.get(Calendar.DAY_OF_WEEK) + "");
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1: //일요일
                dayOfWeek = 6;
                break;
            case 2: //월요일
                dayOfWeek = 0;
                break;
            case 3: //화요일
                dayOfWeek = 1;
                break;
            case 4: //수요일
                dayOfWeek = 2;
                break;
            case 5: //목요일
                dayOfWeek = 3;
                break;
            case 6: //금요일
                dayOfWeek = 4;
                break;
            case 7: //토요일
                dayOfWeek = 5;
        }

        if (type.equals("class")) { //수업 알람
            ArrayList<SubjectData> classes = new ArrayList<>();
            Bundle bundle = intent.getBundleExtra("bundle");
            classes = (ArrayList<SubjectData>) bundle.getSerializable("classes");
            if (!classes.get(dayOfWeek).getSubject().equals("none")) { //수업이 있으면
                Intent mIntent = new Intent(context, AlarmActivity.class);
                mIntent.putExtra("alarmType", "class");
                mIntent.putExtra("alarmName", classes.get(dayOfWeek).getSubject());
                mIntent.putExtra("alarmID", classes.get(dayOfWeek).getID());
                mIntent.putExtra("alarmPW", classes.get(dayOfWeek).getPW());
                mIntent.putExtra("setTime", intent.getLongExtra("time", 0));
                context.startActivity(mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } else if (type.equals("alarm")) {
            //일반 알람
        }
    }
}
