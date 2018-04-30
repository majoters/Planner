package devs.mulham.raee.sample;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;
import com.example.hotmildc.shareact.PositionTarget;
import com.example.hotmildc.shareact.Post;
import com.example.supakorn.notification_morning.NotificationMain;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Date;

import static devs.mulham.raee.sample.MainActivity4.ActivityLocation;
import static devs.mulham.raee.sample.MainActivity4.ActivityName;
import static devs.mulham.raee.sample.MainActivity4.ActivityTime;
import static devs.mulham.raee.sample.MainActivity4.ActivityArrive;
import static devs.mulham.raee.sample.MainActivity4.GetShareFromForeignkey;
import static devs.mulham.raee.sample.MainActivity4.SHR_Model;
import static devs.mulham.raee.sample.MainActivity4.User;
import static devs.mulham.raee.sample.MainActivity4.context;
import static devs.mulham.raee.sample.MainActivity4.database_share;
import static devs.mulham.raee.sample.MainActivity4.listView;
import static devs.mulham.raee.sample.MainActivity4.listdate;
import static devs.mulham.raee.sample.MainActivity4.values_filter;

/**
 * Created by supakorn on 27/11/2560.
 */

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    List_Database list_database;
    int index;
    public Button yes, no;
    public int i=0;
    private GoogleMap map;
    public static EditText locate;
    int PLACE_PICKER_REQUEST = 1;
    public boolean CancelActivity=false;
    public Date date = new Date();
    public boolean suggest = false;
    public FirebaseDatabase firebaseDatabase;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    public static ArrayList<FriendListType> mDataSet;
    SwitchCompat switchCompat;
    public static int statusActivity;
    Button plus;
    static TextView typeshare;
    public static FriendListShare friendListShare;
    public static boolean important;
    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
        list_database=null;
        statusActivity=0;
    }

    public CustomDialogClass(Activity a,List_Database list_database,int i) {
        super(a);
        this.c = a;
        this.list_database=list_database;
        this.index=i;
        statusActivity=list_database.getStatus();
    }

    public CustomDialogClass(Activity a,List_Database list_database) {
        super(a);
        this.c = a;
        this.list_database=list_database;
        statusActivity=0;
        if(list_database!=null)
            suggest=true;
    }

    public void show(List_Database database,int Time){
        EditText description = (EditText)findViewById(R.id.Description);
        //EditText Hours = (EditText)findViewById(R.id.Hour);
        //EditText Minutes = (EditText)findViewById(R.id.Minute);
        final TimePicker simpleTimePicker = (TimePicker) findViewById(R.id.timepicker1);
        simpleTimePicker.setIs24HourView(true);
        locate = (EditText)findViewById(R.id.Location);
        description.setText(database.getDescription());

        //Hours.setText(String.valueOf(Time/100));
        //Minutes.setText(String.valueOf(Time%100));
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        final EditText description = (EditText)findViewById(R.id.Description);
        final CheckBox box = (CheckBox)findViewById(R.id.checkBox2);
        typeshare=(TextView)findViewById(R.id.typeshare);
        switchCompat = (SwitchCompat)findViewById(R.id.switchCompat);
        plus=(Button)findViewById(R.id.add_to_share);
        mDataSet = new ArrayList<>();
        mDataSet.clear();
        if (box.isChecked()) {
            important = true;
        } else {
            important = false;
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ShareTypeDialog shareTypeDialog = new ShareTypeDialog(context);
                    shareTypeDialog.show();
                    /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    statusActivity=1;
                                    typeshare.setText("PUBSHARE");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    statusActivity=3;
                                    typeshare.setText("REQSHARE");
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    ab.setMessage("What type you want to share?").setPositiveButton("Public Share", dialogClickListener)
                            .setNegativeButton("Request Share", dialogClickListener).show();
               */ }else{
                    statusActivity=0;
                    typeshare.setText("SHARE");
                }
            }
        });

        if(statusActivity==1){
            typeshare.setText("PubShare");
        }else if(statusActivity==2){
            typeshare.setText("SocialPlan");
        }else {
            typeshare.setText("SHARE");
        }

        //final EditText Hours = (EditText)findViewById(R.id.Hour);
        //final EditText Minutes = (EditText)findViewById(R.id.Minute);
        final TimePicker simpleTimePicker = (TimePicker)findViewById(R.id.timepicker1);
        simpleTimePicker.setIs24HourView(true);
        locate = (EditText)findViewById(R.id.Location);
        Button OK = (Button)findViewById(R.id.OK);
        Button Cancel = (Button)findViewById(R.id.Cancel);
        //Button search = (Button)findViewById(R.id.Search);
        firebaseDatabase=FirebaseDatabase.getInstance();

        //locate.setEnabled(false);
        if(list_database!=null||suggest){
            description.setText(list_database.getDescription());
            simpleTimePicker.setCurrentHour(list_database.getTime()/100);
            simpleTimePicker.setCurrentMinute(list_database.getTime()%100);
            locate.setText(list_database.getLocationName());

            if(!suggest){
                int ID=list_database.getID();
                Log.w("List Database",String.valueOf(list_database.getID()));
                try{
                    String firebasekey=GetShareFromForeignkey(ID).getFirebaseKey();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                            MainActivity4.User_ID+"/"+MainActivity4.User+"/PublicActivity/"+firebasekey+"/"+
                            "CreatorOrJoiner/MemberList/");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                if(ds.getKey().toString().compareTo("IDMember")!=0){
                                    int ID=Integer.parseInt(ds.getKey().toString());
                                    String username=SocialPlan.memberName.get(SocialPlan.getindexArrayByUserID(ID));
                                    FriendListType friendListType = new FriendListType(username,true);
                                    mDataSet.add(friendListType);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (NullPointerException e){

                }

            }

        }

        locate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    MapsActivity.Search = locate.getText().toString();
                    Intent map = new Intent(c,MapsActivity.class);
                    c.startActivities(new Intent[]{map});
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                }


                /**/
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                };
                handler.postDelayed(runnable,300);

            }
        });


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //try{
                    //Set Variable For set a list
                    if (description.length() == 0 || locate.length() == 0) {
                        //cancel();
                        if(description.length() == 0 || locate.length() == 0){
                            Toast.makeText(context, "Please Enter All Empty Space", Toast.LENGTH_SHORT).show();
                        }else
                        if(description.length()==0)
                            Toast.makeText(context, "Please Enter Description", Toast.LENGTH_SHORT).show();
                        else
                        if(locate.length()==0){
                            Toast.makeText(context, "Please Choose Location", Toast.LENGTH_SHORT).show();
                        }
                        CancelActivity=true;
                    }else{
                        String Hours = String.valueOf(simpleTimePicker.getCurrentHour());
                        String Minutes = String.valueOf(simpleTimePicker.getCurrentMinute());
                        int hour = Integer.parseInt(Hours);
                        int minute = Integer.parseInt(Minutes);
                        long now=date.getYear()%100*100000000+(date.getMonth()+1)*1000000+date.getDate()%100*10000+date.getHours()*100+date.getMinutes();
                        long factor = listdate%100*100000000+listdate%10000/100*1000000+listdate/10000*10000+hour*100+minute;
                        //if(now>=factor){
                        //    CancelActivity=true;
                        //    Toast.makeText(context, "You can't back to your time", Toast.LENGTH_SHORT).show();
                        //}
                        //else{
                        //    CancelActivity=false;
                        //}
                    }
                    if(!CancelActivity){
                        String Hours = String.valueOf(simpleTimePicker.getCurrentHour());
                        String Minutes = String.valueOf(simpleTimePicker.getCurrentMinute());
                        int key_date = MainActivity4.listdate;
                        int hour = Integer.parseInt(Hours);
                        int minute = Integer.parseInt(Minutes);
                        int key_time = hour * 100 + minute;
                        String hr, min = "";
                        //String locationName = locate.getText().toString();

                        //Perform Activity list
                        if (Integer.parseInt(Hours) < 10) {
                            hr = "0" + String.valueOf(hour);
                        } else {
                            hr = String.valueOf(hour);
                        }
                        if (Integer.parseInt(Minutes) < 10) {
                            min = "0" + String.valueOf(minute);
                        } else {
                            min = String.valueOf(minute);
                        }

                        //Add if not have yet and Update if it is a exist data
                        if (list_database == null || suggest) {
                            ActivityName.add(description.getText().toString());
                            ActivityTime.add(hr + ":" + min);
                            ActivityLocation.add(locate.getText().toString());
                            ActivityArrive.add(false);
                            List_Database list = new List_Database(key_date, key_time, description.getText().toString(), String.valueOf(MainActivity4.location.getName()),
                                    MainActivity4.location.getLatLng().latitude, MainActivity4.location.getLatLng().longitude, statusActivity,false,important);
                            MainActivity4.databases.add(0, list);
                            MainActivity4.mDbAdabter_Model.createActivityList(new List_Database(key_date, key_time, description.getText().toString(), String.valueOf(MainActivity4.location.getName()),
                                    MainActivity4.location.getLatLng().latitude, MainActivity4.location.getLatLng().longitude, statusActivity,false,important));
                            MainActivity4.mDbDataForAnalysis_Model.InsertData(date.getHours() * 100 + date.getMinutes(),
                                    list);
                            MainActivity4.mDbDayOfWeekAnalysis_Model.InsertData(key_date,list);
                            //MainActivity4.mDbImportantAnalysis_Model.InsertData(list);


                            if(statusActivity>0){
                                if(statusActivity==1){
                                    Post post = new Post();
                                    post=ListToPost(list);
                                    int index=MainActivity4.SearchIndex(list);
                                    ShareType shareType = new ShareType(index,null,null,
                                            1,MainActivity4.User_ID);
                                    AutoUpdate.pushToFire(post,shareType);
                                }else if(statusActivity==3){
                                    Post post = new Post();
                                    post=ListToPost(list);
                                    int index=MainActivity4.SearchIndex(list);
                                    ShareType shareType = new ShareType(index,null,null,
                                            3,MainActivity4.User_ID);
                                    AutoUpdate.pushToFire(post,shareType);
                                }
                            }

                            if(mDataSet.size()>0){
                                for(int i=0;i<mDataSet.size();i++){
                                    int id=SocialPlan.getIDbyUsername(mDataSet.get(i).getUsername());
                                    AutoUpdate.sendActivity(list,SocialPlan.memberList.get(id));
                                }
                            }

                            NotificationMain notificationMain = new NotificationMain(getContext());
                            notificationMain.setAlarm(list);

                            suggest = false;
                        } else {
                            NotificationMain.CancelEmergency=MainActivity4.mDbAdabter_Model.ListToID(list_database)+1000;
                            ActivityName.set(index, description.getText().toString());
                            ActivityTime.set(index, hr + ":" + min);
                            ActivityLocation.set(index, locate.getText().toString());
                            List_Database list = new List_Database(key_date, key_time, description.getText().toString(), locate.getText().toString()
                                    , list_database.getLatitude(), list_database.getLongitude(), list_database.getStatus(),false);
                            try {
                                list = new List_Database(key_date, key_time, description.getText().toString(), locate.getText().toString(),
                                        MainActivity4.location.getLatLng().latitude, MainActivity4.location.getLatLng().longitude, statusActivity,false);
                            } catch (NullPointerException e) {

                            }
                            MainActivity4.mDbAdabter_Model.UpdateList(list_database, list);
                                    /*MainActivity4.databases.set(SearchIndex(list_database, MainActivity4.databases), list);
                                    MainActivity4.values_filter.set(SearchIndex(list_database, values_filter), list);
                                    */
                            MainActivity4.Refresh();
                            if(list_database.getStatus()!=list.getStatus()){
                                int id=MainActivity4.SearchIndex(list);
                                ShareType shareType=MainActivity4.GetShareFromForeignkey(id);
                                shareType.setStatus(statusActivity);
                                SHR_Model.UpdateShare(shareType.getFirebaseKey(),shareType);
                            }

                            if(list.getStatus()==1||list.getStatus()==3){
                                AutoUpdate.PushUpdate(list);
                            }

                            NotificationMain notificationMain = new NotificationMain(getContext());

                            boolean alarmUp = (PendingIntent.getBroadcast(c, MainActivity4.mDbAdabter_Model.ListToID(
                                    list), new Intent("com.example.supakorn.notification_morning"),
                                    PendingIntent.FLAG_NO_CREATE) == null);

                            if (alarmUp) {
                                notificationMain.CancleAlarm();
                                notificationMain.CancleAlarmChecking();
                            }
                            NotificationMain.CancelEmergency=MainActivity4.mDbAdabter_Model.ListToID(list_database)+1000;
                            NotificationMain.CancelEmergency=-1;
                            notificationMain.setAlarm(list);

                        }

                        //Add Data to Database
                        cancel();

                        //Set Update to list
                        final CustomListView adapter = new CustomListView(getContext(), MainActivity4.ActivityTime, MainActivity4.ActivityName, MainActivity4.ActivityLocation,MainActivity4.ActivityOld,MainActivity4.ActivityArrive);
                        MainActivity4.listView.setAdapter(adapter);
                        listView.invalidate();

                    }

                /*}catch (NullPointerException e){
                    cancel();
                    Toast.makeText(context, "Create Activity Failed!", Toast.LENGTH_SHORT).show();
                }

                /*final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
                handler.postDelayed(runnable,300);*/
            }
        });

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_friend);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new MainAdapter(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(layoutManager);
                if(isShowing())
                    handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for(int i = 1;i<=15;i++)
                    mDataSet.add(String.valueOf(i));
                mDataSet.add("Orn BNK48");
                mDataSet.add("Pun BMK48");
                mDataSet.add("A");
                mDataSet.add("AB");
                mDataSet.add("AC");*/
                if(list_database==null||suggest){
                    friendListShare = new FriendListShare(context);
                    friendListShare.show();
                }else {
                    friendListShare=new FriendListShare(context,mDataSet);
                    friendListShare.show();
                }



            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    public int SearchIndex(List_Database list_database, ArrayList<List_Database> db){
        for(int i=0;i<db.size();i++){
            if(list_database.getDate()==db.get(i).getDate()&&
                    list_database.getTime()==db.get(i).getTime()&&
                    list_database.getDescription().compareTo(db.get(i).getDescription())==0&&
                    list_database.getLocationName().compareTo(db.get(i).getLocationName())==0)
                return i;
        }
        return -1;
    }

    private Post ListToPost(List_Database listDatabase){

        Post post = new Post();
        post.setDate(MainActivity.ConvertDateToString(listDatabase.getDate()));
        post.setTime(MainActivity.ConvertTimeToString(listDatabase.getTime()));
        post.setDetail(listDatabase.getDescription());
        PositionTarget positionTarget = new PositionTarget();
        positionTarget.setName(listDatabase.getLocationName());
        positionTarget.setLatitude(listDatabase.getLatitude());
        positionTarget.setLongitude(listDatabase.getLongitude());
        post.setLocation(positionTarget);
        return post;

    }

}
