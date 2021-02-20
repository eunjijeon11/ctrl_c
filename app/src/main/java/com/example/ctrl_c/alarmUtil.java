package com.example.ctrl_c;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class alarmUtil {

    Context context;
    AlarmManager alarmManager;

    String type;

    ArrayList<SubjectData> classes;

    public alarmUtil(Context context, String type) {
        this.type = type;
        this.context = context;

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(int requestCode, int hour, int min, boolean useAlarm, ArrayList<SubjectData> classes) {
        if (min < 0) {
            hour -= 1;
            min += 60;
            if (hour < 0) {
                hour += 24;
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        if (selectTime <= currentTime) {
            calendar.add(Calendar.DATE, 1);
            selectTime = calendar.getTimeInMillis();
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("type", type);
        if (type.equals("class")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("classes", classes);
            intent.putExtra("bundle", bundle);
        }
        PendingIntent pIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (useAlarm) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, selectTime, AlarmManager.INTERVAL_DAY, pIntent);
            Log.e("alarm", "set");
        } else {
            alarmManager.cancel(pIntent);
        }
    }

    public void cancelAlarm(int requestCode) {
        Log.e("alarm", requestCode + "deleted");
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        alarmManager.cancel(pIntent);
    }
}
