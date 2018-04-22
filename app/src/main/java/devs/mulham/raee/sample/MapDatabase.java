package devs.mulham.raee.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jameswich on 18/2/2561.
 */

public class MapDatabase {

    public static final String COL_ID="id";
    public static final String COL_LOCATION="location";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";

    public static int INDEX_ID=0;
    public static int INDEX_LOCATION=INDEX_ID+1;
    public static int INDEX_LATITUDE=INDEX_ID+2;
    public static int INDEX_LONGITUDE=INDEX_ID+3;

    private static final String TAG="MapDatabase";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="Map_Database";
    private static final String TABLE_NAME="Table_MapDatabase";
    private static final int DATABASE_VERSION=1;

    private final Context mCtx;

    private static final String DATABASE_CREATE=
            "CREATE TABLE if not exists "+ TABLE_NAME+" ( "+
                    COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
                    COL_LOCATION+" TEXT, "+
                    COL_LATITUDE+" REAL, "+
                    COL_LONGITUDE+" REAL );";

    public MapDatabase(Context ctx){
        this.mCtx=ctx;
    }

    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public void createMapDatabase(NearPlaceType nearPlaceType){
        ContentValues values = new ContentValues();
        values.put(COL_LOCATION,nearPlaceType.getName());
        values.put(COL_LATITUDE,nearPlaceType.getLatitude());
        values.put(COL_LONGITUDE,nearPlaceType.getLongitude());

        mDb.insert(TABLE_NAME,null,values);
    }

    public int NumberOfList(){
        Cursor cursor= mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_LOCATION,COL_LATITUDE,COL_LONGITUDE},
                null,null,null,null,null);

        return cursor.getCount();
    }

    public ArrayList<NearPlaceType> fecthAllList(){
        Cursor cursor= mDb.query(TABLE_NAME,new String[]{
                COL_ID,
                COL_LOCATION,COL_LATITUDE,COL_LONGITUDE},null,null,null,null,null);

        if(cursor!=null&&cursor.getCount()>0) {
            cursor.moveToFirst();


            ArrayList<NearPlaceType> List = new ArrayList<>();

            while (!cursor.isAfterLast()) {
                NearPlaceType nearPlaceType = new NearPlaceType(cursor.getString(INDEX_LOCATION),
                        cursor.getDouble(INDEX_LATITUDE),cursor.getDouble(INDEX_LONGITUDE));
                nearPlaceType.setId_data(cursor.getInt(INDEX_ID));
                List.add(nearPlaceType);
                cursor.moveToNext();
            }

            return List;
        }
        else{
            return null;
        }
    }

    public void UpdateList(NearPlaceType list_database){
        int ID = list_database.getId_data();
        ContentValues values = new ContentValues();
        values.put(COL_LOCATION,list_database.getName());
        values.put(COL_LATITUDE,list_database.getLatitude());
        values.put(COL_LONGITUDE,list_database.getLongitude());
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?",
                new String[]{String.valueOf(ID)});
    }

    public void DeleteList(int n){
        String sql = "DELETE FROM "+TABLE_NAME+" WHERE id = "+String.valueOf(n);
        mDb.execSQL(sql);
        //mDb.delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(n)});
    }

    public NearPlaceType fetchByID(int id){
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_LOCATION,COL_LATITUDE,COL_LONGITUDE},COL_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            NearPlaceType nearPlaceType = new NearPlaceType(cursor.getString(INDEX_LOCATION),cursor.getDouble(INDEX_LATITUDE),
                    cursor.getDouble(INDEX_LONGITUDE));

            return nearPlaceType;
        }
        return null;
    }

    public int ListToID(NearPlaceType nearPlaceType){
        Cursor cursor=mDb.query(TABLE_NAME, new String[]{COL_ID,COL_LOCATION,COL_LATITUDE,COL_LONGITUDE},null,
                null,null,null,null);

        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                if(cursor.getDouble(INDEX_LATITUDE)-0.0001<nearPlaceType.getLatitude()&&
                        cursor.getDouble(INDEX_LATITUDE)+0.0001>nearPlaceType.getLatitude()&&
                        cursor.getDouble(INDEX_LONGITUDE)-0.0005<nearPlaceType.getLongitude()&&
                        cursor.getDouble(INDEX_LONGITUDE)+0.0005>nearPlaceType.getLongitude()){
                    return cursor.getInt(INDEX_ID);
                }
                cursor.moveToNext();
            }
        }

        return -1;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

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
