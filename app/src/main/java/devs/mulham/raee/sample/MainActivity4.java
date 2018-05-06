package devs.mulham.raee.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.Update_23_04_61.PublicZone;
import com.example.hotmildc.shareact.MainActivity;
import com.example.hotmildc.shareact.PositionTarget;
import com.example.hotmildc.shareact.Post;
import com.example.hotmildc.shareact.RequestType;
import com.example.supakorn.checking.GetUpdateCurrent;
import com.example.supakorn.notification_morning.NoticeMain;
import com.example.supakorn.notification_morning.NotificationMain;
import com.example.supakorn.notification_morning.SettingHelper;
import com.facebook.CallbackManager;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.CompactCalendar;
import com.project.kmitl57.beelife.CustomNavbarMenu;
import com.project.kmitl57.beelife.DataAnalysis;
import com.project.kmitl57.beelife.DataForAnalysis;
import com.project.kmitl57.beelife.DayOfWeekDataAnalysis;
import com.project.kmitl57.beelife.ImportantDataAnalysis;
import com.project.kmitl57.beelife.KMean;
import com.project.kmitl57.beelife.ProfileFriend;
import com.project.kmitl57.beelife.ProfilePage;
import com.project.kmitl57.beelife.R;
import com.project.kmitl57.beelife.SettingsActivity;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

import static com.example.hotmildc.shareact.MainActivity.ConvertTimeToForm;
import static com.example.hotmildc.shareact.MainActivity.convertDateToForm;

public class MainActivity4 extends AppCompatActivity {

    public static final int SHARE_ACTIVITIED = 1;
    public static final int NO_SHARE_ACTIVITY = 0;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private HorizontalCalendar horizontalCalendar;
    private ActivityDbAdapter mDbAdapter;
    public static ActivityDbAdapter mDbAdabter_Model;
    private DataForAnalysis mDbDataForAnalysis;
    public static DataForAnalysis mDbDataForAnalysis_Model;
    private DayOfWeekDataAnalysis mDbDayOfWeekAnalysis;
    public static DayOfWeekDataAnalysis mDbDayOfWeekAnalysis_Model;
    private ImportantDataAnalysis mDbImportantAnalysis;
    public static ImportantDataAnalysis mDbImportantAnalysis_Model;
    private  KMean mDbKmean;
    public static KMean mDbKmean_Model;
    private ShareDatabase SHR;
    public static ShareDatabase SHR_Model;
    public static ListView listView ;
    public static String a = new String();
    public static ArrayList<String> ActivityName = new ArrayList<>();
    public static ArrayList<String> ActivityTime = new ArrayList<>();
    public static ArrayList<String> ActivityLocation = new ArrayList<>();
    public static ArrayList<Boolean> ActivityOld = new ArrayList<>();
    public static ArrayList<Boolean> ActivityArrive = new ArrayList<>();
    public static ArrayList<Boolean> ActivityImportant = new ArrayList<>();
    public static ArrayList<List_Database> values_filter = new ArrayList<>();
    public static ArrayList<List_Database> databases = new ArrayList<>();
    public  static int listdate ;
    public static boolean oldtime;
    public static boolean Arrive;
    public static Place location=null;
    static int id_date=0;
    TextView txt;
    public static Date date = new Date();
    //public static Date dateNow = new Date();
    int PLACE_PICKER_REQUEST = 1;
    public static CustomDialogClass cdd;
    public static ArrayList<DataAnalysis> ResultSuggest;
    public static ArrayList<DataAnalysis> ResultDayOfWeek;
    public static ArrayList<DataAnalysis> ResultImportant;
    public int doublecheck=0;
    public static String User; //Simson
    public static int User_ID; //1549
    public static Context context;
    public CallbackManager mCallbackmanager;
    public static ArrayList<RequestType> ListRequest;
    public static ListView suggest ;
    public static ListView recent ;
    ArrayList<DataAnalysis> choice;
    ArrayList<DataAnalysis> historical;
    public static ArrayList<ShareType> database_share;
    TextView textCartItemCount;
    TextView textCartItemCount2;
    public static int mCartItemCount = 0;
    public static int mCartItemCount2 = 0;
    public static ArrayList<FriendRequestType> FriendsRequest;
    public static String FirebaseAct;
    public MapDatabase mDbMapDatabase;
    public static MapDatabase mDbMapDatabase_clone;
    public SettingHelper mSetting;
    public static  SettingHelper mSetting_clone;
    public static ArrayList<NearPlaceType> all_history_map;
    FirebaseDatabase firebaseDatabase;
    public String firebasekey;
    public static int ID_For_clone;
    public static int public_index;
    public static boolean routine=false;
    public static String Email;
    public static int id_checking;
    public static ArrayList<FriendListType> friendList;
    public static ArrayList<FriendListType> allUsers;
    public DrawerLayout drawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        friendList=new ArrayList<>();
        allUsers = new ArrayList<>();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        //long now=date.getYear()%100*100000000+(date.getMonth()+1)*1000000+date.getDate()%100*10000+date.getHours()*100+date.getMinutes();
        //Log.d("now : ", String.valueOf(date.getMonth()));
        /*Log.d("User",User);
        Log.d("User id", String.valueOf(User_ID));
        Log.d("Email",Email);*/
        firebaseDatabase=FirebaseDatabase.getInstance();
        listView = (ListView) findViewById(R.id.list);
        suggest = (ListView)findViewById(R.id.Suggest);
        recent = (ListView)findViewById(R.id.Recent);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ResultSuggest=new ArrayList<>();
        ResultDayOfWeek=new ArrayList<>();
        ResultImportant=new ArrayList<>();
        ListRequest=new ArrayList<>();
        FriendsRequest=new ArrayList<>();
        all_history_map=new ArrayList<>();
        ActivityCompat.requestPermissions(MainActivity4.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},123);
        //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), );
        Toast.makeText(this,Email,Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),GetUpdateCurrent.class);
        getApplicationContext().startService(i);

        Intent dataForNewFeed = new Intent(getApplicationContext(),GetFriends.class);
        getApplicationContext().startService(dataForNewFeed);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent update = new Intent(MainActivity4.this,AutoUpdate.class);
                        MainActivity4.this.startService(update);
                        Log.d("AutoUpdateStatus","Updating");
                        Refresh();
                        Intent update2 = new Intent(MainActivity4.this,AutoCheckRequest.class);
                        MainActivity4.this.startService(update2);
                        mCartItemCount=ListRequest.size();
                        mCartItemCount2=FriendsRequest.size();
                        //Toast.makeText(MainActivity4.this,String.valueOf(mCartItemCount),Toast.LENGTH_SHORT).show();
                        setupBadge();
                        setupBadge2();
                    }
                }).run();
                handler.postDelayed(this,500);
            }
        };

        handler.post(runnable);

        context=MainActivity4.this;

        /** end after 2 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 12);

        /** start 2 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, -10000);

        final Calendar defaultDate = Calendar.getInstance();
        defaultDate.add(Calendar.MONTH, -1);
        defaultDate.add(Calendar.DAY_OF_WEEK, +5);

        final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation,ActivityOld,ActivityArrive,ActivityImportant);
        listView.setAdapter(adapter);



        //Create Database
        mDbAdapter = new ActivityDbAdapter(getApplicationContext());
        mDbAdapter.open();
        mDbAdabter_Model=mDbAdapter;

        mDbDataForAnalysis = new DataForAnalysis(getApplicationContext());
        mDbDataForAnalysis.open();
        mDbDataForAnalysis_Model=mDbDataForAnalysis;

        mDbDayOfWeekAnalysis = new DayOfWeekDataAnalysis(getApplicationContext());
        mDbDayOfWeekAnalysis.open();
        mDbDayOfWeekAnalysis_Model=mDbDayOfWeekAnalysis;

        mDbImportantAnalysis = new ImportantDataAnalysis(getApplicationContext());
        mDbImportantAnalysis.open();
        mDbImportantAnalysis_Model=mDbImportantAnalysis;

        mDbKmean = new KMean(getApplicationContext());
        mDbKmean.open();
        mDbKmean_Model=mDbKmean;

        SHR=new ShareDatabase(getApplicationContext());
        SHR.Open();
        SHR_Model=SHR;

        mDbMapDatabase = new MapDatabase(getApplicationContext());
        mDbMapDatabase.open();
        mDbMapDatabase_clone=mDbMapDatabase;

        mSetting=new SettingHelper(getApplicationContext());
        mSetting.open();
        mSetting_clone=mSetting;
        if(!mSetting.isCreate()){
            mSetting.createSetting(6,0);
        }

        NotificationMain ncc = new NotificationMain(MainActivity4.this);
        ncc.setRoutine();
        //Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show();

        Intent update = new Intent(MainActivity4.this,AutoUpdate.class);
        MainActivity4.this.startService(update);

        ProfileFriend.friendID=1313;

        if(routine){
            NoticeMain noticeMain=new NoticeMain(MainActivity4.this);
            noticeMain.show();
            routine=false;
        }

        FirebaseDatabase getAllUser = FirebaseDatabase.getInstance();
        DatabaseReference get_user = getAllUser.getReference("Profiles");
        get_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    FriendListType friendListType = new FriendListType(ds.child("Username").getValue().toString(),false);
                    friendListType.setID(ds.child("ID").getValue().toString());
                    allUsers.add(friendListType);
                }

                FirebaseDatabase getFriends =FirebaseDatabase.getInstance();
                final DatabaseReference ListFriends = getFriends.getReference("Users/"+User_ID+"/ListFriend/");
                ListFriends.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            for(int i=0;i<allUsers.size();i++){
                                if(ds.getKey().toString().compareTo(allUsers.get(i).getID())==0){
                                    friendList.add(allUsers.get(i));
                                }
                            }
                        }
                        allUsers.clear();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .showDayName(true)
                .showMonthName(true)
                .selectedDateBackground(ContextCompat.getDrawable(this, R.drawable.sample_selected_background))
                .defaultSelectedDate(defaultDate.getTime())
                .textColor(Color.LTGRAY, Color.WHITE)
                .build();
        //horizontalCalendar.goToday(false);
        horizontalCalendar.selectDate(date,true);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                //Toast.makeText(MainActivity4.this, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
                Log.d("Selected Item: ", DateFormat.getDateInstance().format(date));
                TestSuggest.date=date;
                listdate=date.getDate()*10000+(date.getMonth()+1)*100+ date.getYear()%100;
                values_filter.clear();
                ActivityLocation.clear();
                ActivityName.clear();
                ActivityTime.clear();
                ActivityOld.clear();
                ActivityArrive.clear();
                ActivityImportant.clear();

                if(mDbAdapter.fecthAllList()!=null){
                    databases=mDbAdapter.fecthAllList();
                    database_share=SHR.getAllData();
                    all_history_map=mDbMapDatabase.fecthAllList();
                }

                for(int i=0;i<databases.size();i++){
                    if(databases.get(i).getDate()==listdate){
                        values_filter.add(databases.get(i));
                    }
                }

                for(int i=0;i<values_filter.size();i++){
                    for(int j=0;j<values_filter.size()-1;j++){
                        if(values_filter.get(j).getTime()>values_filter.get(j+1).getTime()){
                            List_Database swap=values_filter.get(j);
                            values_filter.set(j,values_filter.get(j+1));
                            values_filter.set(j+1,swap);
                        }
                    }
                }

                //check past time
                for(int i=0;i<values_filter.size();i++){
                    String time="";
                    if(values_filter.get(i).getTime()/100<10)
                        time+="0"+String.valueOf(values_filter.get(i).getTime()/100)+":";
                    else
                        time+=String.valueOf(values_filter.get(i).getTime()/100)+":";
                    if(values_filter.get(i).getTime()%100<10)
                        time+="0"+String.valueOf(values_filter.get(i).getTime()%100);
                    else
                        time+=String.valueOf(values_filter.get(i).getTime()%100);

                    //check old time
                    Date dateNow = new Date();
                    long now=dateNow.getYear()%100*100000000+(dateNow.getMonth()+1)*1000000+dateNow.getDate()%100*10000+dateNow.getHours()*100+(dateNow.getMinutes());
                    int factor = values_filter.get(i).getDate()%100*100000000+values_filter.get(i).getDate()%10000/100*1000000
                            +values_filter.get(i).getDate()/10000*10000+(values_filter.get(i).getTime()-values_filter.get(i).getTime()/100)+values_filter.get(i).getTime()/100;
                    Log.d("now : ", String.valueOf(now));
                    Log.d("factor : ", String.valueOf(factor));
                    oldtime = false;
                    if(now>=factor){
                        oldtime = true;
                    }
                    ActivityTime.add(time);
                    ActivityName.add(values_filter.get(i).getDescription());
                    ActivityLocation.add(values_filter.get(i).getLocationName());
                    ActivityOld.add(oldtime);
                    ActivityArrive.add(values_filter.get(i).getArrive());
                    ActivityImportant.add(values_filter.get(i).getImportant());
                    Log.d("ArriveValue",String.valueOf(values_filter.get(i).getArrive()));
                }

                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation,ActivityOld,ActivityArrive,ActivityImportant);
                // Save the ListView state (= includes scroll position) as a Parceble
                Parcelable state = listView.onSaveInstanceState();
                // e.g. set new items
                listView.setAdapter(adapter);
                // Restore previous state (including selected item index and scroll position)
                listView.onRestoreInstanceState(state);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultSuggest.clear();
                ResultDayOfWeek.clear();
                ResultImportant.clear();
                if(mDbKmean.GetAll().size()>0){
                    ArrayList<DataAnalysis> dataAnalyses = mDbKmean.GetShow();
                    ResultSuggest=dataAnalyses;
                }
                if(mDbDayOfWeekAnalysis.GetNumberDatabase()>0){
                    ArrayList<DataAnalysis> dataAnalysis = mDbDayOfWeekAnalysis.GetMaxDayOfWeek(getIntDayOfWeek(listdate));
                    ResultDayOfWeek=dataAnalysis;
                }
                if(mDbImportantAnalysis.GetNumberDatabase()>0){
                    ArrayList<DataAnalysis> dataAnalysis = mDbImportantAnalysis.GetImportant();
                    ResultImportant=dataAnalysis;
                }
                //drawer.openDrawer(GravityCompat.END);
                drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }

                    @Override
                    public void onDrawerOpened(View view) {

                    }

                    @Override
                    public void onDrawerClosed(View view) {
                        //CustomDialogClass cdc = new CustomDialogClass(MainActivity4.this,null);
                        //cdc.show();
                    }

                    @Override
                    public void onDrawerStateChanged(int i) {

                    }
                });
                CustomDialogClass cdc = new CustomDialogClass(MainActivity4.this,null);
                cdc.show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mDbAdapter.DeleteList(mDbAdapter.ListToID(values_filter.get(position)));
                                //mDbAdabter_Model.DeleteList();
                                txt.setText(String.valueOf(mDbAdapter.NumberOfList()));
                                Toast.makeText(getApplicationContext(),String.valueOf(values_filter.get(position)
                                        .getDescription()),Toast.LENGTH_LONG).show();
                                databases.remove(SearchIndex(values_filter.get(position)));
                                values_filter.remove(position);
                                ActivityLocation.remove(position);
                                ActivityName.remove(position);
                                ActivityTime.remove(position);
                                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation);
                                listView.setAdapter(adapter);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity4.this);
                ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                */try{
                    ShowDelete showDelete = new ShowDelete(MainActivity4.this,values_filter.get(position),position);
                    showDelete.show();
                }catch (IndexOutOfBoundsException e){

                }

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               /*TextView txt6 = (TextView)findViewById(R.id.textView6);
               txt6.setText(ActivityTime.get(0));
               /*CustomDialogClass cdd = new CustomDialogClass(MainActivity4.this,values_filter.get(i),i);
               cdd.show();*/
                final int position = i;
                doublecheck++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(doublecheck==1){
                            //Toast.makeText(MainActivity4.this,"FirstClick",Toast.LENGTH_SHORT).show();
                            try{

                                int ID=SearchIndex(values_filter.get(position));
                                MainActivity4.FirebaseAct=IDDatabaseToFirebasekey(ID);
                                //if(MainActivity4.FirebaseAct==null){
                                    ShowActivity editdialog = new ShowActivity(MainActivity4.this,values_filter.get(position),position);
                                    editdialog.show();
                                /*}
                                else{
                                    Intent Sharing = new Intent(MainActivity4.this, ListShare.class);
                                    startActivity(Sharing);
                                }*/
                            }catch (IndexOutOfBoundsException e){

                            }
                        }else if(doublecheck==2){
                            //Toast.makeText(MainActivity4.this,"SecondClick",Toast.LENGTH_SHORT).show();
                            DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(i==dialogInterface.BUTTON_POSITIVE){
                                        List_Database TempList=values_filter.get(position);
                                        TempList.setStatus(SHARE_ACTIVITIED);
                                        mDbAdabter_Model.UpdateList(values_filter.get(position),TempList);
                                        Toast.makeText(MainActivity4.this,String.valueOf(mDbAdapter.ListToID(TempList))
                                                ,Toast.LENGTH_SHORT).show();
                                        PositionTarget positionTarget = new PositionTarget(values_filter.get(position).getLocationName(),
                                                values_filter.get(position).getLatitude(),values_filter.get(position).getLongitude());
                                        Post post = new Post(values_filter.get(position).getDescription(),
                                                MainActivity.ConvertDateToString(values_filter.get(position).getDate()),
                                                MainActivity.ConvertTimeToString(values_filter.get(position).getTime()),
                                                positionTarget);
                                        ShareType shareType = new ShareType(SearchIndex(TempList),
                                                null,null,1,User_ID);
                                        AutoUpdate.pushToFire(post,shareType);
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final DatabaseReference myRef = database.getReference("Users");
                                        String key = myRef.child(User).child("PublicActivity").push().getKey();
                                        myRef.child(User).child("PublicActivity").child(key).child("Detail").setValue(values_filter.get(position).getDescription());
                                        myRef.child(User).child("PublicActivity").child(key).child("Date").setValue(MainActivity.ConvertDateToString(values_filter.get(position).getDate()));
                                        myRef.child(User).child("PublicActivity").child(key).child("Time").setValue(MainActivity.ConvertTimeToString(values_filter.get(position).getTime()));
                                        myRef.child(User).child("PublicActivity").child(key).child("Location").child("name").setValue(values_filter.get(position).getLocationName());
                                        myRef.child(User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(values_filter.get(position).getLatitude());
                                        myRef.child(User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(values_filter.get(position).getLongitude());
                                        Toast.makeText(getApplicationContext(),String.valueOf(myRef.child(User).getRef()),Toast.LENGTH_SHORT).show();
                                    }else{
                                        List_Database TempList=values_filter.get(position);
                                        TempList.setStatus(3);
                                        mDbAdabter_Model.UpdateList(values_filter.get(position),TempList);
                                        Toast.makeText(MainActivity4.this,String.valueOf(mDbAdapter.ListToID(TempList))
                                                ,Toast.LENGTH_SHORT).show();
                                        PositionTarget positionTarget = new PositionTarget(values_filter.get(position).getLocationName(),
                                                values_filter.get(position).getLatitude(),values_filter.get(position).getLongitude());
                                        Post post = new Post(values_filter.get(position).getDescription(),
                                                MainActivity.ConvertDateToString(values_filter.get(position).getDate()),
                                                MainActivity.ConvertTimeToString(values_filter.get(position).getTime()),
                                                positionTarget);
                                        String firebase=SHR.GetKeyFromMainDbID(values_filter.get(position).getID());
                                        ShareType shareType = new ShareType(SearchIndex(TempList),
                                                null,null,3,User_ID);
                                        if(firebase!=null){
                                            SHR.UpdateShare(firebase,shareType);
                                        }
                                        AutoUpdate.pushToFirePrivate(post,shareType);
                                        Toast.makeText(MainActivity4.this,"B",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity4.this);
                            alert.setMessage("Are you want to share in public or private").setPositiveButton("Public",dialogInterface)
                                    .setNegativeButton("Private",dialogInterface).show();
                           Toast.makeText(MainActivity4.this,"B",Toast.LENGTH_SHORT).show();


                           /*ShareType shareType = new ShareType(SearchIndex(values_filter.get(position)),
                                   null,null,7,User_ID);
                           AutoUpdate.sendActivity(values_filter.get(position),shareType,1548);
                            */
                        }
                        doublecheck=0;
                    }
                },300);

            }
        });

        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity4.this, CompactCalendar.class)); //calendar
            }
        });
        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity4.this, PublicZone.class));
                //startActivity(new Intent(MainActivity4.this, MainActivity.class)); //searchshare
            }
        });
        findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity4.this, SocialPlan.class)); //socilaplan
            }
        });
        findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity4.this, ShowDatabase.class));
                PopupMenu popupMenu = new PopupMenu(MainActivity4.this,view);    //setting
                popupMenu.inflate(R.menu.profile_menu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.action_profile:
                                startActivity(new Intent(MainActivity4.this, ProfilePage.class));
                                return true;
                            default:
                                return onOptionsItemSelected(menuItem);
                        }
                    }
                });
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        suggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //new
                NamePlace place = new NamePlace(context);
                place.getMap(choice.get(i).getLocationName(),choice.get(i).getLatitude(),choice.get(i).getLongitude());
                //
                List_Database list_database = new List_Database(listdate,choice.get(i).getTime(),choice.get(i).getDescription(),choice.get(i).getLocationName(),choice.get(i).getLatitude(),choice.get(i).getLongitude(),0);
                CustomDialogClass ccd = new CustomDialogClass(MainActivity4.this,list_database);
                ccd.suggest=true;
                ccd.show();
            }
        });

        recent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //new
                NamePlace place = new NamePlace(context);
                place.getMap(historical.get(i).getLocationName(),historical.get(i).getLatitude(),historical.get(i).getLongitude());
                //
                List_Database list_database = new List_Database(listdate,historical.get(i).getTime(),historical.get(i).getDescription(),historical.get(i).getLocationName(),historical.get(i).getLatitude(),historical.get(i).getLongitude(),0);
                CustomDialogClass ccd = new CustomDialogClass(MainActivity4.this,list_database);
                ccd.suggest=true;
                ccd.show();
            }
        });

        final Handler handle_suggest = new Handler();
        Runnable run_suggest=new Runnable() {
            @Override
            public void run() {
                Suggest();
                ListView suggest =(ListView)findViewById(R.id.Suggest);
                CustomNavbarMenu sug = new CustomNavbarMenu(MainActivity4.this,choice);
                // Save the ListView state (= includes scroll position) as a Parceble
                Parcelable state = suggest.onSaveInstanceState();
                // e.g. set new items
                suggest.setAdapter(sug);
                // Restore previous state (including selected item index and scroll position)
                suggest.onRestoreInstanceState(state);
                handle_suggest.postDelayed(this,500);
            }
        };
        handle_suggest.post(run_suggest);

        final Handler handle_recent = new Handler();
        Runnable run_recent=new Runnable() {
            @Override
            public void run() {
                ListView recent =(ListView)findViewById(R.id.Recent);
                CustomNavbarMenu rec = new CustomNavbarMenu(MainActivity4.this,historical);
                // Save the ListView state (= includes scroll position) as a Parceble
                Parcelable state = recent.onSaveInstanceState();
                // e.g. set new items
                recent.setAdapter(rec);
                // Restore previous state (including selected item index and scroll position)
                recent.onRestoreInstanceState(state);
                handle_recent.postDelayed(this,1000);
            }
        };
        handle_recent.post(run_recent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(MainActivity4.this,AutoUpdate.transmit,Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mCallbackmanager= CallbackManager.Factory.create();
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_noti);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();

        final MenuItem AddFriends = menu.findItem(R.id.action_noti_fr);

        View actionView2 = MenuItemCompat.getActionView(AddFriends);
        textCartItemCount2 = (TextView) actionView2.findViewById(R.id.cart_badge_fr);
        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        actionView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(AddFriends);
            }
        });

        setupBadge2();
        return true;
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setupBadge2() {

        if (textCartItemCount2 != null) {
            if (mCartItemCount2 == 0) {
                if (textCartItemCount2.getVisibility() != View.GONE) {
                    textCartItemCount2.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount2.setText(String.valueOf(Math.min(mCartItemCount2, 99)));
                if (textCartItemCount2.getVisibility() != View.VISIBLE) {
                    textCartItemCount2.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            /*case R.id.action_logout:
                if(AccessToken.getCurrentAccessToken()!=null){
                    LoginManager.getInstance().logOut();
                }
                else {
                    FirebaseAuth.getInstance().signOut();
                }

                startActivity(new Intent(MainActivity4.this, Login.class));
                return true;*/
            case R.id.ShowDatabase:
                startActivity(new Intent(MainActivity4.this,ShowDatabase.class));
                return true;
            /*case R.id.Request:
                startActivity(new Intent(MainActivity4.this,RequestActivity.class));

                return true;

            case R.id.Freinds:
                FindFriendDialog find =new FindFriendDialog(context);
                find.show();
                //startActivity(new Intent(MainActivity4.this, AddFriends.class));
                return true;

            case R.id.FriendsRequest:
                startActivity(new Intent(MainActivity4.this, com.example.hotmildc.shareact.FriendsRequest.class));
                return true;

            case R.id.ShowList:
                startActivity(new Intent(MainActivity4.this, ShowListFriends.class));
                return true;*/

            case R.id.action_noti:
                Request.fragmentPosition=1;
                startActivity(new Intent(MainActivity4.this, Request.class));
                return true;

            case R.id.action_noti_fr:
                Request.fragmentPosition=0;
                startActivity(new Intent(MainActivity4.this,Request.class));
                return true;

            /*case R.id.profile_friend:
                startActivity(new Intent(MainActivity4.this, ProfileFriend.class));
                return true;*/

            case R.id.setting:
                startActivity(new Intent(MainActivity4.this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String DataToTime(int time){
        int Hours=time/100;
        int Minutes=time%100;
        String Texttime="";
        if (Hours < 10) {
            Texttime = "0" + String.valueOf(Hours);
        } else {
            Texttime = String.valueOf(Hours);
        }
        Texttime+=":";
        if (Minutes < 10) {
            Texttime += "0" + String.valueOf(Minutes);
        } else {
            Texttime += String.valueOf(Minutes);
        }
        return Texttime;
    }

    public static int SearchIndex(List_Database list_database){
        try {
            for(int i=0;i<MainActivity4.databases.size();i++){
                if(list_database.getDate()==MainActivity4.databases.get(i).getDate()&&
                        list_database.getTime()==MainActivity4.databases.get(i).getTime()&&
                        list_database.getDescription().compareTo(MainActivity4.databases.get(i).getDescription())==0&&
                        list_database.getLocationName().compareTo(MainActivity4.databases.get(i).getLocationName())==0)
                    return MainActivity4.databases.get(i).getID();
            }
        }catch (NullPointerException e){
            return -1;
        }
        return -1;
    }

    public static int getArrayIndex(List_Database listDatabase){
        for(int i=0;i<MainActivity4.databases.size();i++){
            if(listDatabase.getDate()==MainActivity4.databases.get(i).getDate()&&
                    listDatabase.getTime()==MainActivity4.databases.get(i).getTime()&&
                    listDatabase.getDescription().compareTo(MainActivity4.databases.get(i).getDescription())==0&&
                    listDatabase.getLocationName().compareTo(MainActivity4.databases.get(i).getLocationName())==0)
                return i;
        }
        return -1;
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        if (requestCode==PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                MainActivity4.location=place;
                cdd.locate.setText(String.valueOf(place.getName()));
                cdd.locate.invalidate();


            }
        }

    }

    public boolean NotIterateChecking(final ArrayList<List_Database> values_filter_check){
        int sameN=0;
        if(values_filter_check.size()!=values_filter.size()){
            return true;
        }else{
            for(int i=0;i<values_filter.size();i++){
                if(values_filter.get(i).getDate()==values_filter_check.get(i).getDate()&&
                        values_filter.get(i).getTime()==values_filter_check.get(i).getTime()&&
                        values_filter.get(i).getDescription().compareTo(values_filter_check.get(i).getDescription())==0&&
                        values_filter.get(i).getLocationName().compareTo(values_filter_check.get(i).getLocationName())==0&&
                        values_filter.get(i).getLatitude()==values_filter_check.get(i).getLatitude()&&
                        values_filter.get(i).getLongitude()==values_filter_check.get(i).getLongitude()&&
                        values_filter.get(i).getStatus()==values_filter_check.get(i).getStatus()) {
                    sameN++;
                }else{
                    return true;
                }
            }
            return false;
        }
    }


    public static void Refresh(){
        values_filter.clear();
        ActivityLocation.clear();
        ActivityName.clear();
        ActivityTime.clear();
        ActivityOld.clear();
        ActivityArrive.clear();

        if(mDbAdabter_Model.fecthAllList()!=null){
            databases=mDbAdabter_Model.fecthAllList();
        }

        for(int i=0;i<databases.size();i++){
            if(databases.get(i).getDate()==listdate){
                values_filter.add(databases.get(i));
            }
        }

        for(int i=0;i<values_filter.size();i++){
            for(int j=0;j<values_filter.size()-1;j++){
                if(values_filter.get(j).getTime()>values_filter.get(j+1).getTime()){
                    List_Database swap=values_filter.get(j);
                    values_filter.set(j,values_filter.get(j+1));
                    values_filter.set(j+1,swap);
                }
            }
        }

        for(int i=0;i<values_filter.size();i++){
            String time="";
            if(values_filter.get(i).getTime()/100<10)
                time+="0"+String.valueOf(values_filter.get(i).getTime()/100)+":";
            else
                time+=String.valueOf(values_filter.get(i).getTime()/100)+":";
            if(values_filter.get(i).getTime()%100<10)
                time+="0"+String.valueOf(values_filter.get(i).getTime()%100);
            else
                time+=String.valueOf(values_filter.get(i).getTime()%100);
            //check old time
            Date dateNow = new Date();
            long now=dateNow.getYear()%100*100000000+(dateNow.getMonth()+1)*1000000+dateNow.getDate()%100*10000+dateNow.getHours()*100+(dateNow.getMinutes());
            int factor = values_filter.get(i).getDate()%100*100000000+values_filter.get(i).getDate()%10000/100*1000000
                    +values_filter.get(i).getDate()/10000*10000+(values_filter.get(i).getTime()-values_filter.get(i).getTime()/100)+values_filter.get(i).getTime()/100;
            Log.d("now(Refresh) : ", String.valueOf(now));
            Log.d("factor(Refresh) : ", String.valueOf(factor));
            oldtime = false;
            if(now>=factor){
                oldtime = true;
            }
            ActivityTime.add(time);
            ActivityName.add(values_filter.get(i).getDescription());
            ActivityLocation.add(values_filter.get(i).getLocationName());
            ActivityOld.add(oldtime);
            ActivityArrive.add(values_filter.get(i).getArrive());
            ActivityImportant.add(values_filter.get(i).getImportant());
            Log.d("ArriveValue",String.valueOf(values_filter.get(i).getArrive()));
        }

        final CustomListView adapter = new CustomListView(MainActivity4.context,ActivityTime,ActivityName,ActivityLocation,ActivityOld,ActivityArrive,ActivityImportant);
        // Save the ListView state (= includes scroll position) as a Parceble
        Parcelable state = listView.onSaveInstanceState();
        // e.g. set new items
        listView.setAdapter(adapter);
        // Restore previous state (including selected item index and scroll position)
        listView.onRestoreInstanceState(state);
    }

    public void Suggest(){
        choice = new ArrayList<>();
        historical = new ArrayList<>();
        choice.clear();
        historical.clear();
        Date date = new Date();
        int dateNow = date.getDate()*10000+(date.getMonth()+1)*100+ date.getYear()%100;
        int timeNow = date.getHours()*100+(date.getMinutes());
        if(MainActivity4.mDbDataForAnalysis_Model.GetNumberDatabase()>5||MainActivity4.mDbDayOfWeekAnalysis_Model.GetNumberDatabase()>3||MainActivity4.mDbImportantAnalysis_Model.GetNumberDatabase()>3){
            if(listdate == dateNow){ //select now date in calendar
                choice=ResultSuggest;
                for(int i=0;i<choice.size();i++) {
                    for (int j = 0; j < choice.size(); j++) {
                        if (choice.get(j).getTimeAct() < timeNow) {
                            choice.remove(j); //remove past suggest activity
                        }
                    }
                }
            }else if(listdate>dateNow){ //if select future date
                //Calculate DayOfWeek
                choice=ResultDayOfWeek;
                for(int i=0;i<ResultImportant.size();i++){
                    choice.add(ResultImportant.get(i));
                }
                for(int k=0;k<choice.size();k++) {
                    for (int l = 0; l < choice.size()-1; l++) {
                        if (choice.get(l).getFrequency() == choice.get(l+1).getFrequency()) {
                            if(choice.get(l).getTimeAct() == choice.get(l+1).getTimeAct()){
                                if(choice.get(l).getDescription()==choice.get(l+1).getDescription()){
                                    if(choice.get(l).getLocationName()==choice.get(l+1).getLocationName()){
                                        if((choice.get(l).getArrive() == choice.get(l+1).getArrive()) && (choice.get(l).getImportant() == choice.get(l+1).getImportant())){
                                            choice.remove(l);
                                        }

                                    }
                                }
                            }
                       }
                    }
                }
            }

            for(int i=0;i<choice.size();i++){
                for(int j=0;j<choice.size()-1;j++){
                    if(choice.get(j).getFrequency()>choice.get(j+1).getFrequency()){
                        DataAnalysis swap=choice.get(j);
                        choice.set(j,choice.get(j+1));
                        choice.set(j+1,swap);
                    }
                }
            }

        }
        else{
            ArrayList<DataAnalysis> data = new ArrayList<>();
            DataAnalysis a = new DataAnalysis(0,0,"Examination","",
                    0,0,0,0);
            data.add(a);
            a = new DataAnalysis(0,0,"Study Specail","",
                    0,0,0,0);
            data.add(a);
            a = new DataAnalysis(0,0,"Appointment","",
                    0,0,0,0);
            data.add(a);
            a = new DataAnalysis(0,0,"Examination","",
                    0,0,0,0);
            data.add(a);
            choice=data;
        }

        for (int n=0,i=MainActivity4.mDbDataForAnalysis_Model.GetNumberDatabase();
             i>0;i--){
            historical.add(MainActivity4.mDbDataForAnalysis_Model.GetByID(i));
            if(n>3)
                break;
            n++;
        }

        //ListView suggest = (ListView) findViewById(R.id.Suggest);
        //ListView recent = (ListView) findViewById(R.id.Recent);

    }

    public static boolean IsFirebaseKeyInAllShare(String firebasekey){
        try{
            for(int i=0;i<database_share.size();i++){
                if(database_share.get(i).getFirebaseKey().compareTo(firebasekey)==0){
                    return true;
                }
            }
            return false;
        }catch (NullPointerException e){
            return false;
        }
    }

    public static boolean IsInShare(ShareType shareType){
        try{
            for(int i=0;i<database_share.size();i++){
                if(database_share.get(i).getFirebaseKey().compareTo(shareType.getFirebaseKey())==0&&
                        database_share.get(i).getForiegnKey()==shareType.getForiegnKey()&&
                        database_share.get(i).getStatus()==shareType.getStatus()&&
                        database_share.get(i).getOwner()==shareType.getOwner()){
                    return true;
                }
            }
            return false;
        }catch (NullPointerException e){
            return false;
        }
    }

    public static void RefreshShareList(){
        database_share=SHR_Model.getAllData();
    }

    public static int getforiegnkeyFromFirebasekey(String key){
        try {
            for(int i=0;i<database_share.size();i++){
                if(database_share.get(i).getFirebaseKey().compareTo(key)==0){
                    return database_share.get(i).getForiegnKey();
                }
            }
        }catch (NullPointerException e){
            return -1;
        }
        return -1;
    }

    public static List_Database IDtoList(int ID){
        for(int i=0;i<databases.size();i++){
            if(ID==databases.get(i).getID()){
                return databases.get(i);
            }
        }
        return null;
    }

    public static String IDDatabaseToFirebasekey(int ID){
        for(int i=0;i<database_share.size();i++){
            if(database_share.get(i).getForiegnKey()==ID){
                return database_share.get(i).getFirebaseKey();
            }
        }
        return null;
    }

    public static void NumberOfActivities(int key_date,int key_time){
        int n=0;
        for(int i=0;i<databases.size();i++){
            if(databases.get(i).getDate()==key_date){
                if(databases.get(i).getTime()==key_time){
                    n++;
                }
            }
        }
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+"/"+
                User+"/");
        databaseReference.child("NumberOfActivities").child(MainActivity.ConvertDateToString(key_date))
                .child(MainActivity.ConvertTimeToString(key_time)).setValue(n);

    }

    public static void RefreshMapHistory(){
        all_history_map=mDbMapDatabase_clone.fecthAllList();
    }

    public static boolean isInMap(NearPlaceType nearPlaceType){

        try {
            for(int i=0;i<all_history_map.size();i++){
                NearPlaceType nearPlaceType1=all_history_map.get(i);
                double bound_lat=nearPlaceType1.getLatitude();
                double bound_long=nearPlaceType1.getLongitude();
                double current_lat = nearPlaceType.getLatitude();
                double current_long = nearPlaceType.getLongitude();

                if(current_lat<bound_lat+0.0001&&current_lat>bound_lat-0.0001){
                    if(current_long<bound_long+0.0005&&current_long>bound_long-0.0005){
                        return true;
                    }
                }
            }
        }catch (NullPointerException e){
            return false;
        }

        return false;
    }

    public static String getNameByLatLng(Double lat,Double lng){

        try {
            for(int i=0;i<all_history_map.size();i++){
                NearPlaceType nearPlaceType=all_history_map.get(i);
                double bound_lat=nearPlaceType.getLatitude();
                double bound_long=nearPlaceType.getLongitude();
                double current_lat = lat;
                double current_long = lng;

                if(current_lat<bound_lat+0.0001&&current_lat>bound_lat-0.0001){
                    if(current_long<bound_long+0.0005&&current_long>bound_long-0.0005){
                        return nearPlaceType.getName();
                    }
                }
            }
        }catch (NullPointerException e){
            return "";
        }

        return "";
    }

    public static void Clone1(String ref,int position){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(ref);
        PublicActivity pb=SocialPlan.publicActivities.get(position);
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                int date_index = ds.getValue().toString().indexOf("Date");
                int time_index = ds.getValue().toString().indexOf("Time") + 5;
                int detail_index = ds.getValue().toString().indexOf("Detail") + 7;
                int detail_stop_index = ds.getValue().toString().indexOf(",", detail_index);
                int locationName_index = ds.getValue().toString().indexOf("name");
                int locationName_stop_index = ds.getValue().toString().indexOf("latitude", locationName_index) - 1;
                int latitude_index = ds.getValue().toString().indexOf("latitude") + 9;
                int latitude_index_stop = ds.getValue().toString().indexOf(",", latitude_index);
                int longitude_index = ds.getValue().toString().indexOf("longitude") + 10;
                int longitude_stop_index = ds.getValue().toString().indexOf(",", longitude_index);
                String date = ds.getValue().toString().substring(date_index + 5, date_index + 15);
                String time = ds.getValue().toString().substring(time_index, time_index + 5);
                String detail = ds.getValue().toString().substring(detail_index, detail_stop_index);
                String locationName = ds.getValue().toString().substring(locationName_index, locationName_stop_index);
                Double latitude = Double.parseDouble(ds.getValue().toString().substring(latitude_index, latitude_index_stop));
                Double longitude = Double.parseDouble(ds.getValue().toString().substring(longitude_index, longitude_stop_index));
                List_Database listDatabase = new List_Database(MainActivity.convertDateToForm(date),
                        MainActivity.ConvertTimeToForm(time),detail,locationName,latitude,longitude,2);
                Toast.makeText(SocialPlan.mCx,listDatabase.getDescription(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        List_Database list_database = new List_Database(convertDateToForm(pb.getPostDate()),
                ConvertTimeToForm(pb.getTime()),pb.getDetail(),
                pb.getLocationName(),pb.getLatitude(),
                pb.getLongitude(),2);
        mDbAdabter_Model.createActivityList(list_database);
        int index=SearchIndex(list_database);
        ShareType shareType = new ShareType(index,ref,pb.getFirebasekey(),2,pb.getUser_ID());
        SHR_Model.InsertData(shareType);

        databaseReference.child("CreatorOrJoiner").child("MemberList").child(String.valueOf(MainActivity4.User_ID))
                .setValue(1);

        DatabaseReference clone = firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+"/"
        +MainActivity4.User+"/PublicActivity/"+SocialPlan.publicActivities.get(position).getFirebasekey()+"/");
        clone.child("Date").setValue(pb.getPostDate());
        clone.child("Time").setValue(pb.getTime());
        clone.child("Detail").setValue(pb.getDetail());
        clone.child("Location").child("name").setValue(pb.getLocationName());
        clone.child("Location").child("latitude").setValue(pb.getLatitude());
        clone.child("Location").child("longitude").setValue(pb.getLongitude());
        clone.child("CreatorOrJoiner").child("BaseAccept").setValue(2);
        clone.child("CreatorOrJoiner").child("JoinWith").setValue(pb.getUser_ID());
        clone.child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);

    }

    public static void Clone2(String ref,int position){
        PublicActivity pb=SocialPlan.publicActivities.get(position);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                pb.getJoiner()+"/");
        ID_For_clone=pb.getJoiner();
        public_index=position;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1=firebaseDatabase1.getReference("Users/"+ID_For_clone+"/"
                +User+"/PublicActivity/"+SocialPlan.publicActivities.get(public_index).getFirebasekey()+"/");
                databaseReference1.child("CreatorOrJoiner").child("MemberList").child(String.valueOf(MainActivity4.User_ID))
                        .setValue(1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        List_Database list_database = new List_Database(convertDateToForm(pb.getPostDate()),
                ConvertTimeToForm(pb.getTime()),pb.getDetail(),
                pb.getLocationName(),pb.getLatitude(),
                pb.getLongitude(),2);
        mDbAdabter_Model.createActivityList(list_database);
        int index=SearchIndex(list_database);
        ShareType shareType = new ShareType(index,ref,pb.getFirebasekey(),2,pb.getJoiner());
        SHR_Model.InsertData(shareType);


        DatabaseReference clone = firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+"/"
                +MainActivity4.User+"/PublicActivity/"+SocialPlan.publicActivities.get(position).getFirebasekey()+"/");
        clone.child("Date").setValue(pb.getPostDate());
        clone.child("Time").setValue(pb.getTime());
        clone.child("Detail").setValue(pb.getDetail());
        clone.child("Location").child("name").setValue(pb.getLocationName());
        clone.child("Location").child("latitude").setValue(pb.getLatitude());
        clone.child("Location").child("longitude").setValue(pb.getLongitude());
        clone.child("CreatorOrJoiner").child("BaseAccept").setValue(2);
        clone.child("CreatorOrJoiner").child("JoinWith").setValue(pb.getUser_ID());
        clone.child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);

    }

    public static void RequestAct3(String ref,int position){

        PublicActivity pb=SocialPlan.publicActivities.get(position);
        List_Database list_database = new List_Database(convertDateToForm(pb.getPostDate()),
                ConvertTimeToForm("00.00"),"Detail",
                "Empty",0,
                0,-1);
        mDbAdabter_Model.createActivityList(list_database);
        int index=SearchIndex(list_database);
        ShareType shareType = new ShareType(index,ref,pb.getFirebasekey(),-1,pb.getUser_ID());
        SHR_Model.InsertData(shareType);
        AutoUpdate.PushEmpty(pb.getFirebasekey(),shareType,pb.getPostDate());
        AutoUpdate.AddToMemberListPermiss(shareType);
    }

    public static void RequestAct4(String ref,int position){

        PublicActivity pb=SocialPlan.publicActivities.get(position);
        List_Database list_database = new List_Database(convertDateToForm(pb.getPostDate()),
                ConvertTimeToForm("00.00"),"Detail",
                "Empty",0,
                0,-1);
        mDbAdabter_Model.createActivityList(list_database);
        int index=SearchIndex(list_database);
        ShareType shareType = new ShareType(index,ref,pb.getFirebasekey(),-1,pb.getJoiner());
        SHR_Model.InsertData(shareType);
        AutoUpdate.PushEmpty(pb.getFirebasekey(),shareType,pb.getPostDate());
        AutoUpdate.AddToMemberListPermiss(shareType);
    }

    public static ShareType GetShareFromForeignkey(int id){
        for(int i=0;i<database_share.size();i++){
            if(database_share.get(i).getForiegnKey()==id){
                return database_share.get(i);
            }
        }
        return null;

    }

    public static int getIntDayOfWeek(int listdate) {
        String[] dates = new String[] { "SUNDAY", "MONDAY", "TUESDAY", //
                "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
        int day = listdate/10000;
        int month = (listdate/100)-(day*100);
        int year = listdate-((listdate/100)*100)+2000;
        Calendar cal = Calendar.getInstance();
        cal.set(year, //
                month - 1, // <-- add -1
                day);
        int date_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return date_of_week;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity4.this,"You had already logined",Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
    }
}