package com.example.ctrl_c;

import android.provider.BaseColumns;

public class DataBases {

    public static final class CreateAlarmDB implements BaseColumns {
        public static final String TABLE_NAME = "alarmDB";
        public static final String ALARM_NAME = "alarmName";
        public static final String HOUR = "hour";
        public static final String MIN = "min";
        public static final String CREATE = "create table if not exists " + TABLE_NAME +
                "(" + _ID + " integer primary key autoincrement, "
                + ALARM_NAME + " text not null , "
                + HOUR + " integer not null , "
                + MIN + " integer not null );";
    }

    public static final class CreateSubjectDB implements BaseColumns {
        public static final String TABLE_NAME = "subjectDB";
        public static final String SUBJECT_NAME = "subjectName";
        public static final String ID = "id";
        public static final String PASSWORD = "password";
        public static final String COLOR = "color";
        public static final String ALARM_BEFORE = "alarm_before";
        public static final String USE_ALARM = "useAlarm";
        public static final String CREATE = "create table if not exists " + TABLE_NAME +
                "(" + _ID + " integer primary key autoincrement, "
                + SUBJECT_NAME + " text not null , "
                + ID + " text not null , "
                + PASSWORD + " text not null , "
                + COLOR + " text not null , "
                + USE_ALARM + " integer not null , "
                + ALARM_BEFORE + " integer not null );";
    }
}
