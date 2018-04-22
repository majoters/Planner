package com.project.kmitl57.beelife;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.raee.sample.List_Database;

public class DayOfWeekDataAnalysis {

    public static final String COL_ID="id";
    public static final String COL_DAYOFWEEK="day_of_week";
    public static final String COL_TIMEACT="timeActivity";
    public static final String COL_DESCRIPTION="description";
    public static final String COL_LOCATION="location";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";
    public static final String COL_FREQ="frequency";

    public static int INDEX_ID=0;
    public static int INDEX_DAYOFWEEK=INDEX_ID+1;
    public static int INDEX_TIMEACT=INDEX_ID+2;
    public static int INDEX_DESCRIPTION=INDEX_ID+3;
    public static int INDEX_LOCATION=INDEX_ID+4;
    public static int INDEX_LATITUDE=INDEX_ID+5;
    public static int INDEX_LONGITUDE=INDEX_ID+6;
    public static int INDEX_FREQ=INDEX_ID+7;

    private static final String tag = "DayOfWeekFrequency";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="DayOfWeekFrequency";
    private static final String TABLE_NAME="Data";
    private static final int DATABASE_VERSION=1;

    private final Context mContext;

    private static final String DATABASE_CREATE="" +
            "CREATE TABLE if not exists "+TABLE_NAME+" ( "+
            COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
            COL_DAYOFWEEK+" TEXT, "+
            COL_TIMEACT+" INTEGER, "+
            COL_DESCRIPTION+" TEXT, "+
            COL_LOCATION+" TEXT, "+
            COL_LATITUDE+" REAL, "+
            COL_LONGITUDE+" REAL, "+
            COL_FREQ+" INTEGER"+" );";

    public DayOfWeekDataAnalysis(Context mContext) {
        this.mContext = mContext;
    }

    public void open() throws SQLException {
        mDbHelper=new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close(){
        if(mDbHelper!=null){
            mDb.close();
        }
    }

    public void InsertData(int date,List_Database list_database){
        String DayOfWeek = getDayOfWeek(date);
        ContentValues values = new ContentValues();
        values.put(COL_DAYOFWEEK,DayOfWeek);
        values.put(COL_TIMEACT,list_database.getTime());
        values.put(COL_DESCRIPTION,list_database.getDescription());
        values.put(COL_LOCATION,list_database.getLocationName());
        values.put(COL_LATITUDE,list_database.getLatitude());
        values.put(COL_LONGITUDE,list_database.getLongitude());
        if(checkiterate(DayOfWeek,list_database)==0){
            values.put(COL_FREQ,1);
        }
        else {
            values.put(COL_FREQ,checkiterate(DayOfWeek,list_database)+1);
        }

        mDb.insert(TABLE_NAME,null,values);
    }


    public static String getDayOfWeek(int date) { //calculate dayofweek from date
        String[] dates = new String[] { "SUNDAY", "MONDAY", "TUESDAY", //
                "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
        int day = date/10000;
        int month = date/100;
        int year = date-(month*100);
        Calendar cal = Calendar.getInstance();
        cal.set(year, //
                month - 1, // <-- add -1
                day);
        int date_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return dates[date_of_week - 1];
    }

    public static String getIntDayOfWeek(int dayofweek) {
        String[] dates = new String[] { "SUNDAY", "MONDAY", "TUESDAY", //
                "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
        return dates[dayofweek - 1];
    }

    private int checkiterate(String DayOfWeek,List_Database list_database) {
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_DAYOFWEEK,
                        COL_TIMEACT,
                        COL_DESCRIPTION,
                        COL_LOCATION,
                        COL_LATITUDE,
                        COL_LONGITUDE,
                        COL_FREQ},
                null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getString(INDEX_DAYOFWEEK)==DayOfWeek &&
                        cursor.getInt(INDEX_TIMEACT)==list_database.getTime() &&
                        cursor.getString(INDEX_DESCRIPTION).compareTo(list_database.getDescription())==0&&
                        cursor.getString(INDEX_LOCATION).compareTo(list_database.getLocationName())==0&&
                        cursor.getDouble(INDEX_LATITUDE)==list_database.getLatitude()&&
                        cursor.getDouble(INDEX_LONGITUDE)==list_database.getLongitude()){
                    int n=cursor.getInt(INDEX_FREQ);
                    cursor.close();
                    return n;
                }
                cursor.moveToNext();
            }
            cursor.close();
            return 0;
        }
        cursor.close();
        return 0;
    }

    public int GetNumberDatabase(){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_DAYOFWEEK,COL_TIMEACT,COL_DESCRIPTION,
                COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},null,null,null,null,null);
        int n=cursor.getCount();
        cursor.close();
        return n;
    }

    public DataAnalysis GetByID(int id){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_DAYOFWEEK,COL_TIMEACT,COL_DESCRIPTION,
                        COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},COL_ID+"=?",new String[]{String.valueOf(id)},
                null,null,null);

        DataAnalysis data=null;
        if(cursor!=null){
            cursor.moveToFirst();
            data = new DataAnalysis(cursor.getString(INDEX_DAYOFWEEK), cursor.getInt(INDEX_TIMEACT),
                    cursor.getString(INDEX_DESCRIPTION), cursor.getString(INDEX_LOCATION), cursor.getDouble(
                    INDEX_LATITUDE), cursor.getDouble(INDEX_LONGITUDE), cursor.getInt(INDEX_FREQ));

        }
        cursor.close();
        return data;

    }
    public ArrayList<DataAnalysis> GetMaxDayOfWeek(int dayofweek){ //GetMaxFreqByDayOfWeek

        try{
            String day = getIntDayOfWeek(dayofweek);
            Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_DAYOFWEEK,COL_TIMEACT,COL_DESCRIPTION,
                            COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},COL_FREQ+"=?",new String[]{"TOP 10 MAX(frequency)"},  //new String[]{String.valueOf(dayofweek)}
                    day,null,"frequency DESC");

            ArrayList<DataAnalysis> dataAnalyses = new ArrayList<>();

            if(cursor!=null){
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    DataAnalysis data = new DataAnalysis(cursor.getString(INDEX_DAYOFWEEK), cursor.getInt(INDEX_TIMEACT),
                            cursor.getString(INDEX_DESCRIPTION), cursor.getString(INDEX_LOCATION), cursor.getDouble(
                            INDEX_LATITUDE), cursor.getDouble(INDEX_LONGITUDE), cursor.getInt(INDEX_FREQ));
                    dataAnalyses.add(data);
                    cursor.moveToNext();

                }
                cursor.close();
                return dataAnalyses;
            }
            cursor.close();
        }catch (SQLException e){
            return null;
        }

        return null;

    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(tag,DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(tag,"Upgrading database from version "+oldVersion+" to "
                    +newVersion+", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }
}
