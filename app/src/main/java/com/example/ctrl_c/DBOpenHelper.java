package com.example.ctrl_c;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class DBOpenHelper {

    private final Context mContext;
    private AlarmDBHelper alarmDBHelper;
    private SubjectDBHelper subjectDBHelper;
    private TimetableDBHelper timetableDBHelper;
    private static SQLiteDatabase aDB;
    private static SQLiteDatabase sDB;
    private static SQLiteDatabase tDB;

    private static class AlarmDBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "AlarmDataBase.db";
        private static final int DB_Version = 3;

        public AlarmDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateAlarmDB.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateAlarmDB.TABLE_NAME);
            onCreate(db);
        }
    }

    private static class SubjectDBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "SubjectDataBase.db";
        private static final int DB_Version = 5;

        public SubjectDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateSubjectDB.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateSubjectDB.TABLE_NAME);
            onCreate(db);
        }
    }

    private static class TimetableDBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "TimetableDataBase.db";
        private static final int DB_Version = 5;

        public TimetableDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateTimetableDB.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateTimetableDB.TABLE_NAME);
        }
    }

    public DBOpenHelper(Context context) {
        this.mContext = context;
    }

    public DBOpenHelper open(String DBName) throws SQLException {
        switch (DBName) {
            case "alarm":
                alarmDBHelper = new AlarmDBHelper(mContext, AlarmDBHelper.DB_NAME, null, AlarmDBHelper.DB_Version);
                aDB = alarmDBHelper.getWritableDatabase();
                break;
            case "subject":
                subjectDBHelper = new SubjectDBHelper(mContext, SubjectDBHelper.DB_NAME, null, SubjectDBHelper.DB_Version);
                sDB = subjectDBHelper.getWritableDatabase();
                break;
            case "timetable":
                timetableDBHelper = new TimetableDBHelper(mContext, TimetableDBHelper.DB_NAME, null, TimetableDBHelper.DB_Version);
                tDB = timetableDBHelper.getWritableDatabase();
                break;
        }
        return this;
    }

    public void create(String DBName) {
        switch (DBName) {
            case "alarm":
                alarmDBHelper.onCreate(aDB);
                break;
            case "subject":
                subjectDBHelper.onCreate(sDB);
                break;
            case "timetable":
                timetableDBHelper.onCreate(tDB);
                break;
        }
    }

    public void close(String DBName) {
        switch (DBName) {
            case "alarm":
                aDB.close();
                break;
            case "subject":
                sDB.close();
                break;
            case "timetable":
                tDB.close();
                break;
        }
    }

    //insert
    public boolean insertAlarm(AlarmData alarmData) {
        ContentValues values = new ContentValues();

        values.put(DataBases.CreateAlarmDB.ALARM_NAME, alarmData.getAlarmName());
        values.put(DataBases.CreateAlarmDB.HOUR, alarmData.getHour());
        values.put(DataBases.CreateAlarmDB.MIN, alarmData.getMin());
        boolean[] Day = alarmData.getDay();
        String useDay = "";
        for (boolean b : Day) {
            String  dayInt = b ? "1" : "0";
            useDay = useDay.concat(dayInt);
        }
        values.put(DataBases.CreateAlarmDB.ON_OFF, alarmData.getOnOff() ? 1 : 0);

        return aDB.insert(DataBases.CreateAlarmDB.TABLE_NAME, null, values) > 0;
    }

    public boolean insertSubject(SubjectData subjectData) {
        ContentValues values = new ContentValues();

        values.put(DataBases.CreateSubjectDB.SUBJECT_NAME, subjectData.getSubject());
        values.put(DataBases.CreateSubjectDB.ID, subjectData.getID());
        values.put(DataBases.CreateSubjectDB.PASSWORD, subjectData.getPW());
        values.put(DataBases.CreateSubjectDB.COLOR, subjectData.getColor());
        int useAlarm = subjectData.getUseAlarm() ? 1 : 0;
        values.put(DataBases.CreateSubjectDB.USE_ALARM, useAlarm);
        values.put(DataBases.CreateSubjectDB.ALARM_BEFORE, subjectData.getAlarmBefore());

        return sDB.insert(DataBases.CreateSubjectDB.TABLE_NAME, null, values) > 0;
    }

    public boolean insertClasses(String[] classes, rowData mData) {
        ContentValues values = new ContentValues();

        values.put(DataBases.CreateTimetableDB.MON, classes[0]);
        values.put(DataBases.CreateTimetableDB.TUE, classes[1]);
        values.put(DataBases.CreateTimetableDB.WED, classes[2]);
        values.put(DataBases.CreateTimetableDB.THU, classes[3]);
        values.put(DataBases.CreateTimetableDB.FRI, classes[4]);
        values.put(DataBases.CreateTimetableDB.SAT, classes[5]);
        values.put(DataBases.CreateTimetableDB.SUN, classes[6]);
        values.put(DataBases.CreateTimetableDB.START_HOUR, mData.getStartH());
        values.put(DataBases.CreateTimetableDB.START_MIN, mData.getStartM());
        values.put(DataBases.CreateTimetableDB.USE_ALARM, mData.getUseAlarm() ? 1 : 0);

        return tDB.insert(DataBases.CreateTimetableDB.TABLE_NAME, null, values) > 0;
    }

    //update
    public boolean updateAlarm(long id, AlarmData alarmData) {
        ContentValues values = new ContentValues();

        values.put(DataBases.CreateAlarmDB.ALARM_NAME, alarmData.getAlarmName());
        values.put(DataBases.CreateAlarmDB.HOUR, alarmData.getHour());
        values.put(DataBases.CreateAlarmDB.MIN, alarmData.getMin());
        boolean[] Day = alarmData.getDay();
        String useDay = "";
        for (boolean b : Day) {
            useDay = useDay.concat(String.valueOf(b));
        }
        values.put(DataBases.CreateAlarmDB.ON_OFF, alarmData.getOnOff() ? 1 : 0);

        return aDB.update(DataBases.CreateAlarmDB.TABLE_NAME, values, "_id=" + id, null) > 0;
    }

    public boolean updateSubject(long id, SubjectData subjectData) {
        ContentValues values = new ContentValues();

        values.put(DataBases.CreateSubjectDB.SUBJECT_NAME, subjectData.getSubject());
        values.put(DataBases.CreateSubjectDB.ID, subjectData.getID());
        values.put(DataBases.CreateSubjectDB.PASSWORD, subjectData.getPW());
        values.put(DataBases.CreateSubjectDB.COLOR, subjectData.getColor());
        values.put(DataBases.CreateSubjectDB.ALARM_BEFORE, subjectData.getAlarmBefore());

        return sDB.update(DataBases.CreateSubjectDB.TABLE_NAME, values, "_id=" + id, null) > 0;
    }

    public boolean updateClasses(long id, String[] classes, rowData mData) {
        ContentValues values = new ContentValues();

        values.put(DataBases.CreateTimetableDB.MON, classes[0]);
        values.put(DataBases.CreateTimetableDB.TUE, classes[1]);
        values.put(DataBases.CreateTimetableDB.WED, classes[2]);
        values.put(DataBases.CreateTimetableDB.THU, classes[3]);
        values.put(DataBases.CreateTimetableDB.FRI, classes[4]);
        values.put(DataBases.CreateTimetableDB.SAT, classes[5]);
        values.put(DataBases.CreateTimetableDB.SUN, classes[6]);
        values.put(DataBases.CreateTimetableDB.START_HOUR, mData.getStartH());
        values.put(DataBases.CreateTimetableDB.START_MIN, mData.getStartM());
        values.put(DataBases.CreateTimetableDB.USE_ALARM, mData.getUseAlarm() ? 1 : 0);

        return tDB.update(DataBases.CreateTimetableDB.TABLE_NAME, values, "_id=" + id, null) > 0;
    }

    public boolean deleteColumn(long id, String DBName) {
        boolean isDelete = false;
        switch (DBName) {
            case "alarm":
                isDelete = aDB.delete(DataBases.CreateAlarmDB.TABLE_NAME, "_id=" + id, null) > 0;
                break;
            case "subject":
                isDelete = sDB.delete(DataBases.CreateSubjectDB.TABLE_NAME, "_id=" + id, null) > 0;
                break;
            case "timetable":
                isDelete = tDB.delete(DataBases.CreateTimetableDB.TABLE_NAME, "_id=" + id, null) > 0;
                break;
        }
        return isDelete;
    }

    public Cursor selectColumns(String DBName) {
        Cursor cursor = null;
        switch (DBName) {
            case "alarm":
                cursor = aDB.query(DataBases.CreateAlarmDB.TABLE_NAME, null, null, null, null, null, null);
                break;
            case "subject":
                cursor = sDB.query(DataBases.CreateSubjectDB.TABLE_NAME, null, null, null, null, null, null);
                break;
            case "timetable":
                cursor = tDB.query(DataBases.CreateTimetableDB.TABLE_NAME, null, null, null, null, null, null);
                break;
        }
        return cursor;
    }

    public void deleteAllColumns(String DBName) {
        switch (DBName) {
            case "alarm":
                aDB.delete(DataBases.CreateAlarmDB.TABLE_NAME, null, null);
                break;
            case "subject":
                sDB.delete(DataBases.CreateSubjectDB.TABLE_NAME, null, null);
                break;
            case "timetable":
                tDB.delete(DataBases.CreateTimetableDB.TABLE_NAME, null, null);
                break;
        }
    }
}
