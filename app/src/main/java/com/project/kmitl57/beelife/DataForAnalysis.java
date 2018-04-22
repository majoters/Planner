package com.project.kmitl57.beelife;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.supakorn.checking.GetUpdateCurrent;

import java.util.ArrayList;
import java.util.Date;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

/**
 * Created by supakorn on 21/1/2561.
 */

public class DataForAnalysis {

    public static final String COL_ID="id";
    public static final String COL_TIME="time";
    public static final String COL_TIMEACT="timeActivity";
    public static final String COL_DESCRIPTION="description";
    public static final String COL_LOCATION="location";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";
    public static final String COL_FREQ="frequency";
    public static final String COL_GROUP="group_";

    public static int INDEX_ID=0;
    public static int INDEX_TIME=INDEX_ID+1;
    public static int INDEX_TIMEACT=INDEX_ID+2;
    public static int INDEX_DESCRIPTION=INDEX_ID+3;
    public static int INDEX_LOCATION=INDEX_ID+4;
    public static int INDEX_LATITUDE=INDEX_ID+5;
    public static int INDEX_LONGITUDE=INDEX_ID+6;
    public static int INDEX_FREQ=INDEX_ID+7;
    public static int INDEX_GROUP=INDEX_ID+8;
    public double CurrentLat;
    public double CurrentLon;

    private static final String tag = "DataForAnalysis";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="DataForAnalysis";
    private static final String TABLE_NAME="Data";
    private static final int DATABASE_VERSION=2;

    private final Context mContext;

    private static final String DATABASE_CREATE="" +
            "CREATE TABLE if not exists "+TABLE_NAME+" ( "+
            COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
            COL_TIME+" INTEGER, "+
            COL_TIMEACT+" INTEGER, "+
            COL_DESCRIPTION+" TEXT, "+
            COL_LOCATION+" TEXT, "+
            COL_LATITUDE+" REAL, "+
            COL_LONGITUDE+" REAL, "+
            COL_FREQ+" INTEGER, "+
            COL_GROUP+" INTEGER"+" );";

    public DataForAnalysis(Context mContext) {
        this.mContext = mContext;
    }

    public void open() throws SQLException{
        mDbHelper=new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close(){
        if(mDbHelper!=null){
            mDb.close();
        }
    }

    public void InsertData(int Time,List_Database list_database){
        ContentValues values = new ContentValues();
        Date date = new Date();
        values.put(COL_TIME,date.getHours()*100+date.getMinutes());
        values.put(COL_TIMEACT,list_database.getTime());
        values.put(COL_DESCRIPTION,list_database.getDescription());
        values.put(COL_LOCATION,list_database.getLocationName());
        values.put(COL_LATITUDE,list_database.getLatitude());
        values.put(COL_LONGITUDE,list_database.getLongitude());
        if(checkiterate(list_database,Time)==0){
            values.put(COL_FREQ,1);
        }
        else {
            values.put(COL_FREQ,checkiterate(list_database,Time)+1);
        }
        values.put(COL_GROUP,CalculateKmean(list_database));
        //values.put(COL_GROUP,CalculateKnear(list_database));

        mDb.insert(TABLE_NAME,null,values);
    }

    public int CalculateKmean(List_Database list_database){

        int n= MainActivity4.mDbKmean_Model.GetNumberOfAllAgent();
        int id=0 ;
        double min=10000;
        double cal;
        //double sum2 = 0;
        Date date = new Date();
        Intent update = new Intent(mContext,GetUpdateCurrent.class);
        mContext.startService(update);
        //double CurrentLat;
        //double CurrentLon;
        try {
            CurrentLat = GetUpdateCurrent.location.getLatitude();
            CurrentLon = GetUpdateCurrent.location.getLongitude();
        }catch (NullPointerException e){
            CurrentLat = 0;
            CurrentLon = 0;
        }
        for(int i=1;i<=n;i++){
            cal = Math.sqrt(Math.pow(list_database.getTime()-MainActivity4.mDbKmean_Model.GetIDAgent(i).getTimeAgent(), 2)+
                    Math.pow(list_database.getLatitude()-MainActivity4.mDbKmean_Model.GetIDAgent(i).getLatitude(),2)+
                    Math.pow(list_database.getLongitude()-MainActivity4.mDbKmean_Model.GetIDAgent(i).getLongitude(),2));
            if(cal<min){
            //if(Math.abs(list_database.getTime()-MainActivity4.mDbKmean_Model.GetIDAgent(i).getTimeAgent())<min){
                //min=Math.abs(list_database.getTime()-MainActivity4.mDbKmean_Model.GetIDAgent(i).getTimeAgent());
                min = cal;
                id=i;
            }
        }

        if(MainActivity4.mDbKmean_Model.GetNumberOfAllAgent()<1){
            //MainActivity4.mDbKmean_Model.InsertData(date.getHours()*100+date.getMinutes(),
            //        0);
            MainActivity4.mDbKmean_Model.InsertData(date.getHours()*100+date.getMinutes(),CurrentLat,CurrentLon,
                    0);
            return 1;
        }else {
            if (min>300){
                //MainActivity4.mDbKmean_Model.InsertData(date.getHours()*100+date.getMinutes(),
                //        MainActivity4.mDbKmean_Model.GetNumberOfAgent(id));
                MainActivity4.mDbKmean_Model.InsertData(date.getHours()*100+date.getMinutes(),CurrentLat,CurrentLon,
                        MainActivity4.mDbKmean_Model.GetNumberOfAgent(id));
                return MainActivity4.mDbKmean_Model.GetNumberOfAllAgent()+1;
            }else {
                MainActivity4.mDbKmean_Model.Updatedata(date.getHours()*100+date.getMinutes(),CurrentLat,CurrentLon,id);
            }
        }

        return id;

    }

    private int checkiterate(List_Database list_database,int time) {
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_TIME,
                        COL_TIMEACT,
                        COL_DESCRIPTION,
                        COL_LOCATION,
                        COL_LATITUDE,
                        COL_LONGITUDE,
                        COL_FREQ,
                        COL_GROUP},
                null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getInt(INDEX_TIME)==time &&
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

    public ArrayList<DataAnalysis> GetGroup(int group){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{
                COL_ID,COL_TIME,COL_TIMEACT,COL_DESCRIPTION,COL_LOCATION,
                COL_LATITUDE,COL_LONGITUDE,COL_FREQ,COL_GROUP
        },COL_GROUP+"=?",new String[]{String.valueOf(group)},null,null,null);

        ArrayList<DataAnalysis> dataAnalyses = new ArrayList<>();

        if(cursor!=null){
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                DataAnalysis data =new DataAnalysis(cursor.getInt(INDEX_TIME),cursor.getInt(INDEX_TIMEACT),
                        cursor.getString(INDEX_DESCRIPTION),cursor.getString(INDEX_LATITUDE),
                        cursor.getDouble(INDEX_LATITUDE),cursor.getDouble(INDEX_LONGITUDE),
                        cursor.getInt(INDEX_FREQ),cursor.getInt(INDEX_GROUP));
                dataAnalyses.add(data);
                cursor.moveToNext();
            }
            cursor.close();
            return dataAnalyses;
        }
        cursor.close();
        return null;
    }

    public ArrayList<DataAnalysis> GetAll() {
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,
                COL_TIME,
                COL_TIMEACT,
                COL_DESCRIPTION,
                COL_LOCATION,
                COL_LATITUDE,
                COL_LONGITUDE,
                COL_FREQ,
                COL_GROUP},null,null,null,null,null);

        ArrayList<DataAnalysis> dataAnalyses = new ArrayList<>();
        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                DataAnalysis data =new DataAnalysis(cursor.getInt(INDEX_TIME),cursor.getInt(INDEX_TIMEACT),
                        cursor.getString(INDEX_DESCRIPTION),cursor.getString(INDEX_LATITUDE),
                        cursor.getDouble(INDEX_LATITUDE),cursor.getDouble(INDEX_LONGITUDE),
                        cursor.getInt(INDEX_FREQ),cursor.getInt(INDEX_GROUP));
                dataAnalyses.add(data);
                cursor.moveToNext();
            }
            cursor.close();
            return dataAnalyses;
        }
        cursor.close();
        return null;
    }

    public int GetNumberDatabase(){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_TIME,COL_TIMEACT,COL_DESCRIPTION,
        COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_FREQ,COL_GROUP},null,null,null,null,null);
        int n=cursor.getCount();
        cursor.close();
        return n;
    }

    public DataAnalysis GetByID(int id){
        Cursor cursur = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_TIME,COL_TIMEACT,COL_DESCRIPTION,
        COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_FREQ,COL_GROUP},COL_ID+"=?",new String[]{String.valueOf(id)},
                null,null,null);

        DataAnalysis data=null;
        if(cursur!=null){
            cursur.moveToFirst();
            data = new DataAnalysis(cursur.getInt(INDEX_TIME), cursur.getInt(INDEX_TIMEACT),
                    cursur.getString(INDEX_DESCRIPTION), cursur.getString(INDEX_LOCATION), cursur.getDouble(
                    INDEX_LATITUDE), cursur.getDouble(INDEX_LONGITUDE), cursur.getInt(INDEX_FREQ), cursur.getInt(
                    INDEX_GROUP));

        }
        cursur.close();
        return data;

    }

    private class DatabaseHelper extends SQLiteOpenHelper{

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
