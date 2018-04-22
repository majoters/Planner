package devs.mulham.raee.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jameswich on 27/1/2561.
 */

public class ShareDatabase {

    public static final String COL_ID="id";
    public static final String COL_FOREIGNKEY="foreign_key";
    public static final String COL_REF="ref";
    public static final String COL_KEY="key";
    public static final String COL_STS="status";
    public static final String COL_OWN="owner";

    public static final int INDEX_ID=0;
    public static final int INDEX_FOREIGNKEY=INDEX_ID+1;
    public static final int INDEX_REF=INDEX_ID+2;
    public static final int INDEX_KEY=INDEX_ID+3;
    public static final int INDEX_STATUS=INDEX_ID+4;
    public static final int INDEX_OWNER=INDEX_ID+5;

    public static final String TAG="ShareDatabase";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME ="Share_Database";
    private static final String TABLE_NAME="Share";
    private static int DATABASE_VERSION=1;
    Context mCtx;

    private static final String CREATE_TABLE = "" +
            "CREATE TABLE if not exists "+TABLE_NAME+" ( "+
            COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
            COL_FOREIGNKEY+" INTEGER, "+
            COL_REF+" TEXT, "+
            COL_KEY+" TEXT, "+
            COL_STS+" INTEGER, "+
            COL_OWN+" INTEGER );";

    public ShareDatabase(Context context){
        this.mCtx=context;
    }

    public void Open() throws SQLiteException{
        mDbHelper=new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void Close() {
        if(mDbHelper!=null){
            mDbHelper.close();
        }
    }

    public void InsertData(ShareType shareType){

        if(!MainActivity4.IsFirebaseKeyInAllShare(shareType.getFirebaseKey())){
            ContentValues values = new ContentValues();
            values.put(COL_FOREIGNKEY,shareType.getForiegnKey());
            values.put(COL_REF,shareType.getRef());
            values.put(COL_KEY,shareType.getFirebaseKey());
            values.put(COL_STS,shareType.getStatus());
            values.put(COL_OWN,shareType.getOwner());

            mDb.insert(TABLE_NAME,null,values);
            MainActivity4.Refresh();
            MainActivity4.RefreshShareList();
        }
    }

    public ArrayList<ShareType> getAllData(){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
        COL_STS,COL_OWN},null,null,null,null,null);

        ArrayList<ShareType> shareTypes = new ArrayList<>();

        if(cursor!=null){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                ShareType shr = new ShareType(cursor.getInt(INDEX_FOREIGNKEY),cursor.getString(INDEX_REF),
                        cursor.getString(INDEX_KEY),cursor.getInt(INDEX_STATUS),cursor.getInt(INDEX_OWNER));
                shr.setId(cursor.getInt(INDEX_ID));
                shareTypes.add(shr);
                cursor.moveToNext();
            }
        }

        return shareTypes;
    }

    public int GetOwner(String key){
        Cursor cursor= mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
        COL_STS,COL_OWN},COL_KEY+"=?",new String[]{key},null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();

            return cursor.getInt(INDEX_OWNER);
        }

        return -1;
    }

    public int GetIDFromShare(ShareType shareType){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
                COL_STS,COL_OWN},null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(cursor.isAfterLast()){
                if(cursor.getInt(INDEX_FOREIGNKEY)==shareType.getForiegnKey()&&
                        cursor.getString(INDEX_REF).compareTo(shareType.getRef())==0&&
                        cursor.getString(INDEX_KEY).compareTo(shareType.getFirebaseKey())==0&&
                        cursor.getInt(INDEX_STATUS)==shareType.getStatus()&&
                        cursor.getInt(INDEX_OWNER)==shareType.getOwner()){
                    return cursor.getInt(INDEX_ID);
                }

            }
        }
        return -1;
    }

    public String GetKeyFromMainDbID(int id){
        /*Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
        COL_STS,COL_OWN},null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                if(cursor.getInt(INDEX_ID)==id){
                    return cursor.getString(INDEX_KEY);
                }
                cursor.moveToNext();
            }
        }*/
        ArrayList<ShareType> shareTypes = new ArrayList<>();
        shareTypes=getAllData();
        for(int i=0;i<shareTypes.size();i++){
            if(shareTypes.get(i).getForiegnKey()==id){
                return shareTypes.get(i).getFirebaseKey();
            }
        }

        return null;
    }

    public int GetMainDbIDFromFirebaseKey(String key){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
                COL_STS,COL_OWN},null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getString(INDEX_KEY).compareTo(key)==0){
                    return cursor.getInt(INDEX_FOREIGNKEY);
                }
                cursor.moveToNext();
            }
        }

        return 0;
    }

    public int GetStatusFromFirebaseKey(String key){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
                COL_STS,COL_OWN},null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getString(INDEX_KEY).compareTo(key)==0){
                    return cursor.getInt(INDEX_STATUS);
                }
                cursor.moveToNext();
            }
        }

        return 0;
    }

    public void Delete(int n){
        String sql = "DELETE FROM "+TABLE_NAME+" WHERE id = "+String.valueOf(n);
        mDb.execSQL(sql);
    }

    public int KeyToId(String key){
        Cursor cursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_FOREIGNKEY,COL_REF,COL_KEY,
        COL_STS,COL_OWN},null,null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){

                if(cursor.getString(INDEX_KEY).compareTo(key)==0){
                    Toast.makeText(MainActivity4.context,"Delete Complete",Toast.LENGTH_SHORT).show();
                    return cursor.getInt(INDEX_ID);
                }

                cursor.moveToNext();
            }
        }

        return -1;
    }

    public void UpdateShare(String key,ShareType shareType){
        int ID=0;

        ArrayList<ShareType> shareTypeArrayList = new ArrayList<>();
        shareTypeArrayList=MainActivity4.database_share;

        for(int i=0;i<shareTypeArrayList.size();i++){
            if(shareTypeArrayList.get(i).getFirebaseKey().compareTo(key)==0){
                ID=shareTypeArrayList.get(i).getId();
            }
        }

        ContentValues values = new ContentValues();
        values.put(COL_REF,shareType.getRef());
        values.put(COL_KEY,shareType.getFirebaseKey());
        values.put(COL_STS,shareType.getStatus());
        if(shareType.getOwner()>0){
            values.put(COL_OWN,shareType.getOwner());
        }
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?",
                new String[]{String.valueOf(ID)});


    }



    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG,CREATE_TABLE);
            db.execSQL(CREATE_TABLE);
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
