package devs.mulham.raee.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import static com.facebook.FacebookSdk.getApplicationContext;
import static devs.mulham.raee.sample.MainActivity4.ActivityLocation;
import static devs.mulham.raee.sample.MainActivity4.ActivityName;
import static devs.mulham.raee.sample.MainActivity4.mDbAdabter_Model;
import static devs.mulham.raee.sample.MainActivity4.values_filter;

/**
 * Created by supakorn on 21/1/2561.
 */

public class ShowDelete extends Dialog implements View.OnClickListener{

    public Context context;
    public List_Database listDatabase;
    public TextView Description;
    public Button Yes,No;
    public int position;

    public ShowDelete(@NonNull Context context,List_Database list_database,int position) {
        super(context);
        this.context=context;
        this.listDatabase=list_database;
        this.position=position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);
        Description=(TextView)findViewById(R.id.edit_activity);
        TextView Time=(TextView)findViewById(R.id.edit_time);
        Yes=(Button)findViewById(R.id.yes);
        No=(Button)findViewById(R.id.no);
        Description.setText(listDatabase.getDescription());
        Time.setText(MainActivity.ConvertTimeToString(listDatabase.getTime()));

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //Get Share Database and Check Status
                        try{
                            if(listDatabase.getStatus()!=0){
                                String key = MainActivity4.SHR_Model.GetKeyFromMainDbID(mDbAdabter_Model.ListToID(listDatabase));
                                AutoUpdate.deleteOnlineAct(key);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("Users").child(String.valueOf(MainActivity4.User_ID))
                                        .child(MainActivity4.User).child("PublicActivity").child(key).setValue(null);
                                MainActivity4.SHR_Model.Delete(MainActivity4.SHR_Model.KeyToId(key));
                                MainActivity4.RefreshShareList();
                            }
                        }catch (NullPointerException e){

                        }

                /*if(listDatabase.getStatus()==2){
                    String key = MainActivity4.SHR_Model.GetKeyFromMainDbID(mDbAdabter_Model.ListToID(listDatabase));
                    MainActivity4.SHR_Model.Delete(MainActivity4.SHR_Model.KeyToId(key));

                }*/
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"
                        +MainActivity4.User_ID+"/"+MainActivity4.User+"/");
                        databaseReference.child("NumberOfActivities").child(
                                MainActivity.ConvertDateToString(values_filter.get(position).getDate())
                        ).child(MainActivity.ConvertTimeToString(values_filter.get(position).getTime()))
                                .setValue(null);
                        MainActivity4.mDbAdabter_Model.DeleteList(MainActivity4.mDbAdabter_Model.ListToID(
                                MainActivity4.values_filter.get(position)));
                        //mDbAdabter_Model.DeleteList();
                        /*txt.setText(String.valueOf(MainActivity4.mDbAdabter_Model.NumberOfList()));*/
                        //Toast.makeText(getApplicationContext(),String.valueOf(values_filter.get(position)
                        //        .getDescription()),Toast.LENGTH_LONG).show();
                        MainActivity4.databases.remove(MainActivity4.getArrayIndex(MainActivity4.values_filter.get(position)));
                        MainActivity4.values_filter.remove(position);
                        MainActivity4.ActivityTime.remove(position);
                        MainActivity4.ActivityName.remove(position); //
                        MainActivity4.ActivityLocation.remove(position); //
                        MainActivity4.ActivityOld.remove(position);
                        MainActivity4.ActivityArrive.remove(position);
                        MainActivity4.ActivityImportant.remove(position);

                        cancel();
                        final CustomListView adapter = new CustomListView(MainActivity4.context,
                                MainActivity4.ActivityTime,ActivityName,ActivityLocation,MainActivity4.ActivityOld,MainActivity4.ActivityArrive,MainActivity4.ActivityImportant);
                        MainActivity4.listView.setAdapter(adapter);
                        MainActivity4.listView.invalidate();

                    }
                };
                handler.postDelayed(runnable,300);
                }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                };
                handler.postDelayed(runnable,300);
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}
