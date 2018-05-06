package com.project.kmitl57.beelife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

public class ImportantDataAnalysis {
    public static final String COL_ID="id";
    public static final String COL_TIMEACT="timeActivity";
    public static final String COL_DESCRIPTION="description";
    public static final String COL_LOCATION="location";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";
    public static final String COL_IMPORTANT="important";
    public static final String COL_ARRIVE="arrive";
    public static final String COL_FREQ="frequency";

    public static int INDEX_ID=0;
    public static int INDEX_TIMEACT=INDEX_ID+1;
    public static int INDEX_DESCRIPTION=INDEX_ID+2;
    public static int INDEX_LOCATION=INDEX_ID+3;
    public static int INDEX_LATITUDE=INDEX_ID+4;
    public static int INDEX_LONGITUDE=INDEX_ID+5;
    public static int INDEX_IMPORTANT=INDEX_ID+6;
    public static int INDEX_ARRIVE=INDEX_ID+7;
    public static int INDEX_FREQ=INDEX_ID+8;

    private static final String tag = "ImportantFrequency";

    public static int frequency;
    public static int id;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="ImportantFrequency";
    private static final String TABLE_NAME="Data";
    private static final int DATABASE_VERSION=2;

    private final Context mContext;

    private static final String DATABASE_CREATE="" +
            "CREATE TABLE if not exists "+TABLE_NAME+" ( "+
            COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
            COL_TIMEACT+" INTEGER, "+
            COL_DESCRIPTION+" TEXT, "+
            COL_LOCATION+" TEXT, "+
            COL_LATITUDE+" REAL, "+
            COL_LONGITUDE+" REAL, "+
            COL_IMPORTANT+" INTEGER, "+
            COL_ARRIVE+" INTEGER, "+
            COL_FREQ+" INTEGER"+" );";

    public ImportantDataAnalysis(Context mContext) {
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

    public void InsertData(List_Database list_database){
        ContentValues values = new ContentValues();
        values.put(COL_TIMEACT,list_database.getTime());
        values.put(COL_DESCRIPTION,list_database.getDescription());
        values.put(COL_LOCATION,list_database.getLocationName());
        values.put(COL_LATITUDE,list_database.getLatitude());
        values.put(COL_LONGITUDE,list_database.getLongitude());
        values.put(COL_IMPORTANT,list_database.getImportant());
        values.put(COL_ARRIVE,list_database.getArrive());
        frequency = checkiterate(list_database);
        int n = frequency+1;
        if(frequency==0){
            values.put(COL_FREQ,1);
            mDb.insert(TABLE_NAME,null,values);
        }
        else {
            int ID = id;
            values.put(COL_FREQ,n);
            mDb.update(TABLE_NAME, values,
                    COL_ID + "=?",
                    new String[]{String.valueOf(ID)});
        }

        //mDb.insert(TABLE_NAME,null,values);
    }

    private int checkiterate(List_Database list_database) {
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_TIMEACT,
                        COL_DESCRIPTION,
                        COL_LOCATION,
                        COL_LATITUDE,
                        COL_LONGITUDE,
                        COL_IMPORTANT,
                        COL_ARRIVE,
                        COL_FREQ},
                null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getInt(INDEX_TIMEACT)==list_database.getTime() &&
                        cursor.getString(INDEX_DESCRIPTION).equals(list_database.getDescription())&&
                        cursor.getString(INDEX_LOCATION).equals(list_database.getLocationName())&&
                        cursor.getDouble(INDEX_LATITUDE)==list_database.getLatitude()&&
                        cursor.getDouble(INDEX_LONGITUDE)==list_database.getLongitude()&&
                        cursor.getInt(INDEX_IMPORTANT)==list_database.getIntImportant()&&
                        cursor.getInt(INDEX_ARRIVE)==list_database.getIntArrive()){
                    int n=cursor.getInt(INDEX_FREQ);
                    id=cursor.getInt(INDEX_ID);
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
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_TIMEACT,COL_DESCRIPTION,
                COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_IMPORTANT,COL_ARRIVE,COL_FREQ},null,null,null,null,null);
        int n=cursor.getCount();
        cursor.close();
        return n;
    }

    public DataAnalysis GetByID(int id){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_TIMEACT,COL_DESCRIPTION,
                        COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_IMPORTANT,COL_ARRIVE,COL_FREQ},COL_ID+"=?",new String[]{String.valueOf(id)},
                null,null,null);

        DataAnalysis data=null;
        if(cursor!=null){
            cursor.moveToFirst();
            data = new DataAnalysis(cursor.getInt(INDEX_TIMEACT), cursor.getString(INDEX_DESCRIPTION),
                    cursor.getString(INDEX_LOCATION), cursor.getDouble(INDEX_LATITUDE),
                    cursor.getDouble(INDEX_LONGITUDE), cursor.getInt(INDEX_IMPORTANT),
                    cursor.getInt(INDEX_ARRIVE), cursor.getInt(INDEX_FREQ));

        }
        cursor.close();
        return data;
    }

    public ArrayList<DataAnalysis> GetImportant(){ //GetMaxFreqByDayOfWeek

        try{
            Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_TIMEACT,COL_DESCRIPTION,
                            COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_IMPORTANT,COL_ARRIVE,COL_FREQ},COL_IMPORTANT+"=? AND "+COL_ARRIVE+"=?",new String[]{"1","0"},
                    null,null,"frequency DESC limit 5");
            Cursor cursor2 = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_TIMEACT,COL_DESCRIPTION,
                            COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_IMPORTANT,COL_ARRIVE,COL_FREQ},COL_IMPORTANT+"=? AND "+COL_ARRIVE+"=?",new String[]{"1","1"},
                    null,null,"frequency DESC limit 5");

            ArrayList<DataAnalysis> dataAnalyses = new ArrayList<>();

            if(cursor!=null&&cursor2!=null){
                cursor.moveToFirst();
                cursor2.moveToFirst();
                while(!cursor.isAfterLast()&&!cursor2.isAfterLast()) {
                    DataAnalysis data = new DataAnalysis(cursor.getInt(INDEX_TIMEACT), cursor.getString(INDEX_DESCRIPTION),
                            cursor.getString(INDEX_LOCATION), cursor.getDouble(INDEX_LATITUDE), cursor.getDouble(INDEX_LONGITUDE),
                            cursor.getInt(INDEX_IMPORTANT), cursor.getInt(INDEX_ARRIVE), cursor.getInt(INDEX_FREQ));
                    DataAnalysis data2 = new DataAnalysis(cursor.getInt(INDEX_TIMEACT), cursor.getString(INDEX_DESCRIPTION),
                            cursor.getString(INDEX_LOCATION), cursor.getDouble(INDEX_LATITUDE), cursor.getDouble(INDEX_LONGITUDE),
                            cursor.getInt(INDEX_IMPORTANT), cursor.getInt(INDEX_ARRIVE), cursor.getInt(INDEX_FREQ));
                    dataAnalyses.add(data);
                    dataAnalyses.add(data2);
                    cursor.moveToNext();
                    cursor2.moveToNext();
                }
                cursor.close();
                cursor2.close();
                return dataAnalyses;
            }
            cursor.close();
            cursor2.close();
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
