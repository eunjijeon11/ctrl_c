package com.example.ctrl_c;

import android.provider.BaseColumns;

public class DataBases {

    public static final class CreateAlarmDB implements BaseColumns {
        public static final String TABLE_NAME = "alarmDB";
        public static final String ALARM_NAME = "alarmName";
        public static final String HOUR = "hour";
        public static final String MIN = "min";
        public static final String  ON_OFF = "onOff";
        public static final String CREATE = "create table if not exists " + TABLE_NAME +
                "(" + _ID + " integer primary key autoincrement, "
                + ALARM_NAME + " text not null , "
                + HOUR + " integer not null , "
                + MIN + " integer not null , "
                + ON_OFF + " integer not null );";
    }

    public static final class CreateSubjectDB implements BaseColumns {
        public static final String TABLE_NAME = "subjectDB";
        public static final String SUBJECT_NAME = "subjectName";
        public static final String ID = "id";
        public static final String PASSWORD = "password";
        public static final String COLOR = "color";
        public static final String CREATE = "create table if not exists " + TABLE_NAME +
                "(" + _ID + " integer primary key autoincrement, "
                + SUBJECT_NAME + " text not null , "
                + ID + " text not null , "
                + PASSWORD + " text not null , "
                + COLOR + " integer not null );";
    }

    public static final class CreateTimetableDB implements BaseColumns {
        public static final String TABLE_NAME = "timetableDB";
        public static final String MON = "mon";
        public static final String TUE = "tue";
        public static final String WED = "wed";
        public static final String THU = "thu";
        public static final String FRI = "fri";
        public static final String SAT = "sat";
        public static final String SUN = "sun";
        public static final String START_HOUR = "hour";
        public static final String START_MIN = "min";
        public static final String USE_ALARM = "useAlarm";
        public static final String ALARM_BEFORE = "alarmBefore";
        public static final String CREATE = "create table if not exists " + TABLE_NAME +
                "(" + _ID + " integer primary key autoincrement, "
                + MON + " text not null , "
                + TUE + " text not null , "
                + WED + " text not null , "
                + THU + " text not null , "
                + FRI + " text not null , "
                + SAT + " text not null , "
                + SUN + " text not null , "
                + START_HOUR + " integer not null , "
                + START_MIN + " integer not null , "
                + USE_ALARM + " integer not null , "
                + ALARM_BEFORE + " integer not null );";
    }
}
