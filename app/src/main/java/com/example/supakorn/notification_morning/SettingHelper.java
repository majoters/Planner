package com.example.supakorn.notification_morning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by supakorn on 9/1/2561.
 */

public class SettingHelper {

    public SettingHelper(Context Ctx) {
        this.mCtx = Ctx;
    }

    public static final String COL_ID = "id";
    public static final String COL_HOUR = "hour";
    public static final String COL_MINUTE = "minute";

    public static final int INDEX_ID = 0;
    public static final int INDEX_HOUR = INDEX_ID+1;
    public static final int INDEX_MINUTE = INDEX_ID+2;

    private static final String TAG = "SettingHelper";

    private SettingDatabase mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "dba_setting";
    private static final String TABLE_NAME = "tbl_setting";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_HOUR + " INTEGER, " +
                    COL_MINUTE + " INTEGER );";

    public void open() throws SQLException{
        mDbHelper = new SettingDatabase(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public void createSetting(int hour,int minute){
        ContentValues values = new ContentValues();
        values.put(COL_HOUR,hour);
        values.put(COL_MINUTE,minute);
        mDb.insert(TABLE_NAME,null,values);
    }

    public void updateSetting(int hour,int minute){
        ContentValues values = new ContentValues();
        values.put(COL_HOUR,hour);
        values.put(COL_MINUTE,minute);
        mDb.update(TABLE_NAME,values,
                COL_ID+ "=?",new String[]{String.valueOf(1)});
    }

    public int getTimeSetting(){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_HOUR,COL_MINUTE},null,
                null,null,null,null);
        cursor.moveToFirst();
        int Hour=cursor.getInt(INDEX_HOUR);
        int Minute=cursor.getInt(INDEX_MINUTE);

        return Hour*100+Minute;
    }

    public boolean isCreate(){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_HOUR,COL_MINUTE},null,
                null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }

    public static class SettingDatabase extends SQLiteOpenHelper{

        public SettingDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG,"Upgrading database from version " + oldVersion+" to "
            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
