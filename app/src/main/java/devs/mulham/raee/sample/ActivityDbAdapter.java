package devs.mulham.raee.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by supakorn on 25/12/2560.
 */

public class ActivityDbAdapter {

    public static final String COL_ID="id";
    public static final String COL_DATE="date";
    public static final String COL_TIME="time";
    public static final String COL_DESCRIPTION="description";
    public static final String COL_LOCATION="location";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";
    public static final String COL_SHARE="share";
    public static final String COL_IMPORTANT="important";
    public static final String COL_ARRIVE="arrive";


    public static int INDEX_ID=0;
    public static int INDEX_DATE=INDEX_ID+1;
    public static int INDEX_TIME=INDEX_ID+2;
    public static int INDEX_DESCRIPTION=INDEX_ID+3;
    public static int INDEX_LOCATION=INDEX_ID+4;
    public static int INDEX_LATITUDE=INDEX_ID+5;
    public static int INDEX_LONGITUDE=INDEX_ID+6;
    public static int INDEX_SHARE=INDEX_ID+7;
    public static int INDEX_IMPORTANT=INDEX_ID+8;
    public static int INDEX_ARRIVE=INDEX_ID+9;

    private static final String TAG="ActivityListDatabase";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="AcitivityList_Database";
    private static final String TABLE_NAME="Table_ActivityList";
    private static final int DATABASE_VERSION=2;

    private final Context mCtx;

    private static final String DATABASE_CREATE=
            "CREATE TABLE if not exists "+ TABLE_NAME+" ( "+
                    COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
                    COL_DATE+" INTEGER, "+
                    COL_TIME+" INTEGER, "+
                    COL_DESCRIPTION+" TEXT, "+
                    COL_LOCATION+" TEXT, "+
                    COL_LATITUDE+" REAL, "+
                    COL_LONGITUDE+" REAL, "+
                    COL_SHARE+" INTEGER, "+
                    COL_IMPORTANT+" INTEGER, "+
                    COL_ARRIVE+" INTEGER "+ ");";

    public ActivityDbAdapter(Context ctx){
        this.mCtx=ctx;
    }

    public void open() throws SQLException{
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public void createActivityList(List_Database list_database){
        if(MainActivity4.SearchIndex(list_database)==-1){
            ContentValues values = new ContentValues();
            values.put(COL_DATE,list_database.getDate());
            values.put(COL_TIME,list_database.getTime());
            values.put(COL_DESCRIPTION,list_database.getDescription());
            values.put(COL_LOCATION,list_database.getLocationName());
            values.put(COL_LATITUDE,list_database.getLatitude());
            values.put(COL_LONGITUDE,list_database.getLongitude());
            values.put(COL_SHARE,list_database.getStatus());
            values.put(COL_IMPORTANT,list_database.getImportant());
            values.put(COL_ARRIVE,list_database.getArrive());

            mDb.insert(TABLE_NAME,null,values);
            MainActivity4.Refresh();
            MainActivity4.RefreshShareList();
            MainActivity4.NumberOfActivities(list_database.getDate(),list_database.getTime());
        }
    }

    public int NumberOfList(){
        Cursor cursor= mDb.query(TABLE_NAME, new String[]{COL_ID,
        COL_DATE,COL_TIME,COL_DESCRIPTION,COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_SHARE,COL_IMPORTANT,COL_ARRIVE},
                null,null,null,null,null);
        int n=cursor.getCount();
        cursor.close();
        return n;
    }

    public ArrayList<List_Database> fecthAllList(){
        Cursor cursor= mDb.query(TABLE_NAME,new String[]{
                COL_ID,COL_DATE,COL_TIME,COL_DESCRIPTION,
                COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_SHARE,COL_IMPORTANT,COL_ARRIVE},null,null,null,null,null);

        if(cursor!=null&&cursor.getCount()>0) {
            cursor.moveToFirst();


            ArrayList<List_Database> List = new ArrayList<>();

            while (!cursor.isAfterLast()) {
                List_Database list = new List_Database(cursor.getInt(INDEX_DATE), cursor.getInt(INDEX_TIME),
                        cursor.getString(INDEX_DESCRIPTION), cursor.getString(INDEX_LOCATION),
                        cursor.getFloat(INDEX_LATITUDE),cursor.getFloat(INDEX_LONGITUDE),cursor.getInt(INDEX_SHARE),cursor.getInt(INDEX_IMPORTANT),cursor.getInt(INDEX_ARRIVE));
                list.setID(cursor.getInt(INDEX_ID));
                List.add(list);
                cursor.moveToNext();
            }

            cursor.close();
            return List;
        }
        else{
            cursor.close();
            return null;
        }

    }

    public void UpdateList(List_Database listDatabase,List_Database list_database){
        int ID = MainActivity4.SearchIndex(listDatabase);
        ContentValues values = new ContentValues();
        values.put(COL_DATE,list_database.getDate());
        values.put(COL_TIME,list_database.getTime());
        values.put(COL_DESCRIPTION,list_database.getDescription());
        values.put(COL_LOCATION,list_database.getLocationName());
        values.put(COL_LATITUDE,list_database.getLatitude());
        values.put(COL_LONGITUDE,list_database.getLongitude());
        values.put(COL_SHARE,list_database.getStatus());
        values.put(COL_IMPORTANT,list_database.getImportant());
        values.put(COL_ARRIVE,list_database.getStatus());

        mDb.update(TABLE_NAME, values,
                COL_ID + "=?",
                new String[]{String.valueOf(ID)});
        MainActivity4.Refresh();
        MainActivity4.RefreshShareList();
        MainActivity4.NumberOfActivities(list_database.getDate(),list_database.getTime());
    }
    public void UpdateArrive(int id,boolean WorkStatus){
        int ID = id;
        boolean WorkDone = WorkStatus;
        ContentValues values = new ContentValues();
        values.put(COL_ARRIVE,WorkDone);

        mDb.update(TABLE_NAME, values,
                COL_ID + "=?",
                new String[]{String.valueOf(ID)});
        MainActivity4.mDbImportantAnalysis_Model.InsertData(fetchByID(ID));
        MainActivity4.Refresh();
        MainActivity4.RefreshShareList();
    }

    public void DeleteList(int n){
        String sql = "DELETE FROM "+TABLE_NAME+" WHERE id = "+String.valueOf(n);
        mDb.execSQL(sql);
        //mDb.delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(n)});
    }

    public List_Database fetchByID(int id){
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,
        COL_DATE,COL_TIME,COL_DESCRIPTION,COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,
                COL_SHARE,COL_IMPORTANT,COL_ARRIVE},COL_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            List_Database listDatabase = new List_Database(cursor.getInt(INDEX_DATE),
                    cursor.getInt(INDEX_TIME),cursor.getString(INDEX_DESCRIPTION),
                    cursor.getString(INDEX_LOCATION),cursor.getFloat(INDEX_LATITUDE),
                    cursor.getFloat(INDEX_LONGITUDE),cursor.getInt(INDEX_SHARE),
                    cursor.getInt(INDEX_IMPORTANT), cursor.getInt(INDEX_ARRIVE));

            cursor.close();
            return listDatabase;
        }

        cursor.close();
        return null;


    }

    public int ListToID(List_Database list_database){
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_DATE,COL_TIME,COL_DESCRIPTION,COL_LOCATION,COL_LATITUDE,COL_LONGITUDE,COL_SHARE,COL_IMPORTANT,COL_ARRIVE},null,
                null,null,null,null);

        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                if(cursor.getInt(INDEX_DATE)==list_database.getDate()){
                    if(cursor.getInt(INDEX_TIME)==list_database.getTime()){
                        if(cursor.getString(INDEX_DESCRIPTION).compareTo(list_database.getDescription())==0){ //default==0
                            int i=cursor.getInt(INDEX_ID);
                            cursor.close();
                            return i;
                        }
                    }
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
        return -1;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG,DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG,"Upgrading database from version "+oldVersion+" to "
            +newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }


}
