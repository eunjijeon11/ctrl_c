package com.example.ctrl_c;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

    public void setClasses(ArrayList<SubjectData> classes){
        this.classes = classes;
    }

    public void setAlarm(int requestCode, int hour, int min, boolean useAlarm) {
        if (useAlarm) {
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
            Log.e("current time", currentTime + "");

            if (selectTime <= currentTime) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                selectTime = calendar.getTimeInMillis();
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("type", type);
            intent.putExtra("hour", hour);
            intent.putExtra("min", min);
            if (type.equals("class")) {
                intent.putExtra("classes", classes);
            }
            PendingIntent pIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.e("selected time", selectTime + "");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("alarm", requestCode + "set");
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectTime, pIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("alarm", requestCode + "set");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectTime, pIntent);
            } else {
                Log.e("alarm", requestCode + "set");
                alarmManager.set(AlarmManager.RTC_WAKEUP, selectTime, pIntent);
            }
        }
    }

    public void cancelAlarm(int requestCode) {
        Log.e("alarm", requestCode + "deleted");
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        alarmManager.cancel(pIntent);
    }
}
