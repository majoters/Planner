package com.project.kmitl57.beelife;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.supakorn.checking.GetUpdateCurrent;

import java.util.ArrayList;
import java.util.Date;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

/**
 * Created by supakorn on 21/1/2561.
 */

public class KMean {

    public static final String COL_ID="id";
    public static final String COL_AGENTTIME="agent_time";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";
    public static final String COL_FREQ="frequency";

    public static int INDEX_ID=0;
    public static int INDEX_AGENTTIME=INDEX_ID+1;
    public static int INDEX_LATITUDE=INDEX_ID+2;
    public static int INDEX_LONGITUDE=INDEX_ID+3;
    public static int INDEX_FREQ=INDEX_ID+4;
    public double CurrentLat;
    public double CurrentLon;

    private static final String tag = "Kmean";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="Kmean";
    private static final String TABLE_NAME="Data";
    private static final int DATABASE_VERSION=1;

    private final Context mContext;

    private static final String DATABASE_CREATE="" +
            "CREATE TABLE if not exists "+TABLE_NAME+" ( "+
            COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
            COL_AGENTTIME+" REAL, "+
            COL_LATITUDE+" REAL, "+
            COL_LONGITUDE+" REAL, "+
            COL_FREQ+" INTEGER"+" );";

    public KMean(Context mContext) {
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

    //public void InsertData(int TimeAgent,int n){
    public void InsertData(int TimeAgent,double latitude,double longitude,int n){
        ContentValues values = new ContentValues();
        values.put(COL_AGENTTIME,TimeAgent);
        //
        values.put(COL_LATITUDE,latitude);
        //
        values.put(COL_LONGITUDE,longitude);
        values.put(COL_FREQ,n+1);

        mDb.insert(TABLE_NAME,null,values);
    }

    //public void Updatedata(int TimeAgent,int id){
    public void Updatedata(int TimeAgent,double Latitude,double Longitude,int id){
        double mean=0;
        double mean2=0;
        double mean3=0;
        KmeanType kmeanType = GetIDAgent(id);
        mean=(TimeAgent+(kmeanType.getTimeAgent()*kmeanType.getFrequency()))/(kmeanType.getFrequency()+1);
        mean2=(Latitude+(kmeanType.getLatitude()*kmeanType.getFrequency()))/(kmeanType.getFrequency()+1);
        mean3=(Longitude+(kmeanType.getLongitude()*kmeanType.getFrequency()))/(kmeanType.getFrequency()+1);
        ContentValues values = new ContentValues();
        values.put(COL_FREQ,kmeanType.getFrequency()+1);
        values.put(COL_AGENTTIME,mean);
        values.put(COL_LATITUDE,mean2);
        values.put(COL_LONGITUDE,mean3);

        mDb.update(TABLE_NAME,values,COL_ID+"=?",new String[]{String.valueOf(id)});
    }

    public KmeanType GetIDAgent(int ID){

        Cursor cursor=mDb.query(TABLE_NAME,new String[]{COL_ID,COL_AGENTTIME,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},
                COL_ID+"=?",new String[]{String.valueOf(ID)},null,
                null,null);

        if(cursor!=null) {
            cursor.moveToFirst();
            KmeanType kmeanType = new KmeanType(cursor.getDouble(INDEX_AGENTTIME),cursor.getDouble(INDEX_LATITUDE),cursor.getDouble(INDEX_LONGITUDE), cursor.getInt(INDEX_FREQ));
            return kmeanType;
        }
        else
            return null;
    }

    public ArrayList<DataAnalysis> GetShow(){
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
        int id=0;
        double min=10000;
        double cal;
        boolean found=false;
        ArrayList<KmeanType> kmeanTypeArrayList = new ArrayList<>();
        kmeanTypeArrayList=GetAll();
        for(int i=1;i<=kmeanTypeArrayList.size();i++){
            //if(Math.abs(kmeanTypeArrayList.get(id).getTimeAgent()-(date.getHours()*100+date.getMinutes()))<min){
            //    min=Math.abs(kmeanTypeArrayList.get(id).getTimeAgent()-(date.getHours()*100+date.getMinutes()));
            cal = Math.sqrt(Math.pow(kmeanTypeArrayList.get(id).getTimeAgent()-(date.getHours()*100+date.getMinutes()), 2)+
                    Math.pow(kmeanTypeArrayList.get(id).getLatitude()-CurrentLat,2)+
                    Math.pow(kmeanTypeArrayList.get(id).getLongitude()-CurrentLon,2));
            if(cal<min){
                id=i;
            }
        }
        ArrayList<DataAnalysis> dataAnalyses = new ArrayList<>();
        dataAnalyses=MainActivity4.mDbDataForAnalysis_Model.GetGroup(id);
        return dataAnalyses;
    }

    public ArrayList<KmeanType> GetAll(){
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,COL_AGENTTIME,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},
                null,null,null,null,null);

        ArrayList<KmeanType> kmeanTypeArrayList = new ArrayList<>();

        if(cursor!=null){
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                KmeanType kmeanType = new KmeanType(cursor.getDouble(INDEX_AGENTTIME),cursor.getDouble(INDEX_LATITUDE),cursor.getDouble(INDEX_LONGITUDE), cursor.getInt(INDEX_FREQ));
                kmeanTypeArrayList.add(kmeanType);
                cursor.moveToNext();
            }

            return kmeanTypeArrayList;
        }

        return null;
    }

    public int GetNumberOfAllAgent(){
        Cursor cursor=mDb.query(TABLE_NAME,new String[]{COL_ID,COL_AGENTTIME,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},
                null,null,null,null,null);

        return cursor.getCount();
    }

    public int GetNumberOfAgent(int id){
        Cursor cursor=mDb.query(TABLE_NAME,new String[]{COL_ID,COL_AGENTTIME,COL_LATITUDE,COL_LONGITUDE,COL_FREQ},
                COL_ID+"=?",new String[]{String.valueOf(id)},null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            return cursor.getInt(INDEX_FREQ);
        }

        return 0;
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
