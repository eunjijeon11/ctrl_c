package com.example.ctrl_c;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBOpenHelper {

    private Context mContext;
    private AlarmDBHelper alarmDBHelper;
    private SubjectDBHelper subjectDBHelper;
    private static SQLiteDatabase aDB;
    private static SQLiteDatabase sDB;

    private static class AlarmDBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "AlarmDataBase.db";
        private static final int DB_Version = 1;

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
        private static final int DB_Version = 4;

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

    public DBOpenHelper(Context context) {
        this.mContext = context;
    }

    public DBOpenHelper open(String DBName) throws SQLException {
        if (DBName.equals("alarm")) {
            alarmDBHelper = new AlarmDBHelper(mContext, AlarmDBHelper.DB_NAME, null, AlarmDBHelper.DB_Version);
            aDB = alarmDBHelper.getWritableDatabase();
        } else if (DBName.equals("subject")) {
            subjectDBHelper = new SubjectDBHelper(mContext, SubjectDBHelper.DB_NAME, null, SubjectDBHelper.DB_Version);
            sDB = subjectDBHelper.getWritableDatabase();
        }
        return this;
    }

    public void create(String DBName) {
        if (DBName.equals("alarm")) {
            alarmDBHelper.onCreate(aDB);
        } else if (DBName.equals("subject")) {
            subjectDBHelper.onCreate(sDB);
        }
    }

    public void close(String DBName) {
        if (DBName.equals("alarm")) {
            aDB.close();
        } else if (DBName.equals("subject")) {
            sDB.close();
        }
    }

    public boolean insertColumn(String DBName, Data data) {
        ContentValues values = new ContentValues();
        boolean isInsert = false;
        if (DBName.equals("alarm")) {
            //
            isInsert = aDB.insert(DataBases.CreateAlarmDB.TABLE_NAME, null, values) > 0;
        } else if (DBName.equals("subject")) {
            values.put(DataBases.CreateSubjectDB.SUBJECT_NAME, data.getSubject());
            values.put(DataBases.CreateSubjectDB.ID, data.getID());
            values.put(DataBases.CreateSubjectDB.PASSWORD, data.getPW());
            values.put(DataBases.CreateSubjectDB.COLOR, data.getColor());
            int useAlarm = data.getUseAlarm() ? 1 : 0;
            values.put(DataBases.CreateSubjectDB.USE_ALARM, useAlarm);
            values.put(DataBases.CreateSubjectDB.ALARM_BEFORE, data.getAlarmBefore());
            isInsert = sDB.insert(DataBases.CreateSubjectDB.TABLE_NAME, null, values) > 0;
        }
        return isInsert;
    }

    public boolean updateColumn(long id, String DBName, Data data) {
        ContentValues values = new ContentValues();
        boolean isUpdate = false;
        if (DBName.equals("alarm")) {
            //
            isUpdate = aDB.update(DataBases.CreateAlarmDB.TABLE_NAME, values, "_id=" + id, null) > 0;
        } else if (DBName.equals("subject")) {
            values.put(DataBases.CreateSubjectDB.SUBJECT_NAME, data.getSubject());
            values.put(DataBases.CreateSubjectDB.ID, data.getID());
            values.put(DataBases.CreateSubjectDB.PASSWORD, data.getPW());
            values.put(DataBases.CreateSubjectDB.COLOR, data.getColor());
            values.put(DataBases.CreateSubjectDB.ALARM_BEFORE, data.getAlarmBefore());
            isUpdate = sDB.update(DataBases.CreateSubjectDB.TABLE_NAME, values, "_id=" + id, null) > 0;
        }
        return isUpdate;
    }

    public boolean deleteColumn(long id, String DBName) {
        boolean isDelete = false;
        if (DBName.equals("alarm")) {
            isDelete = aDB.delete(DataBases.CreateAlarmDB.TABLE_NAME, "_id=" + id, null) > 0;
        } else if (DBName.equals("subject")) {
            isDelete = sDB.delete(DataBases.CreateSubjectDB.TABLE_NAME, "_id=" + id, null) > 0;
            Log.e("deleteColumn", "triggered");
            Log.e("id", Long.toString(id));
        }
        return isDelete;
    }

    public Cursor selectColumns(String DBName) {
        Cursor cursor = null;
        if (DBName.equals("alarm")) {
            cursor = aDB.query(DataBases.CreateAlarmDB.TABLE_NAME, null,null,null,null, null, null);
        } else if (DBName.equals("subject")) {
            cursor = sDB.query(DataBases.CreateSubjectDB.TABLE_NAME, null,null,null,null, null, null);
        }
        return cursor;
    }

    public void deleteAllColumns(String DBName) {
        if (DBName.equals("alarm")) {
            aDB.delete(DataBases.CreateAlarmDB.TABLE_NAME, null, null);
        } else if (DBName.equals("subject")){
            sDB.delete(DataBases.CreateSubjectDB.TABLE_NAME, null, null);
        }

    }
}
