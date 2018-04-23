package devs.mulham.raee.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hotmildc.shareact.MainActivity;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

public class SocialPlan extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static ArrayList<PublicActivity> publicActivities= new ArrayList<>();
    private static ArrayList<List_Database> values_filter = new ArrayList<>();
    public ArrayList<String> mUsername = new ArrayList<>();
    public ArrayList<String> mActivity = new ArrayList<>();
    public ArrayList<String> mLocation = new ArrayList<>();
    public ArrayList<String> mTime = new ArrayList<>();
    public static boolean isUpdate=true;
    public static ArrayList<Integer> memberList=new ArrayList<>();
    public static ArrayList<String> memberName=new ArrayList<>();
    public static Context mCx;

    @Override
    protected void onStart() {
        isUpdate=true;
        mUsername.clear();
        mActivity.clear();
        mLocation.clear();
        mTime.clear();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_plan);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)findViewById(R.id.textDate);
        Date d = new Date();
        textView.setText(String.valueOf(d.getDate()));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SocialPlan.this, MainActivity4.class));
                finish();
            }
        });
        mCx=SocialPlan.this;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent pullFeed = new Intent(SocialPlan.this,NewFeed.class);
                startService(pullFeed);
                if(isUpdate){
                    mUsername.clear();
                    mActivity.clear();
                    mLocation.clear();
                    mTime.clear();
                    for(int i = 0;i<publicActivities.size();i++){
                        if(publicActivities.get(i).getBaseAccept()==3) {
                            mUsername.add(publicActivities.get(i).getUsername());
                            mActivity.add(publicActivities.get(i).getDetail());
                        /*if(publicActivities.get(i).getBaseAccept()==1||publicActivities.get(i).getBaseAccept()==2){
                            mLocation.add(publicActivities.get(i).getLocationName());
                        }else{

                        }*/
                            if (publicActivities.get(i).getBaseAccept() == 3) {
                                mLocation.add("Send Request");
                            } else if (publicActivities.get(i).getBaseAccept() == 4) {
                                mLocation.add("Owner Accept Request");
                            }
                            mTime.add(publicActivities.get(i).getTime());
                        }
                    }
                    Date date = new Date();
                    /*for(int i=0;i<MainActivity4.databases.size();i++){
                        List_Database db=MainActivity4.databases.get(i);
                        if(db.getDate()==date.getDate()*10000+(date.getMonth()+1)*100+date.getYear()%100){
                            if(db.getStatus()==3||db.getStatus()==4){

                                /*PublicActivity publicActivity = new PublicActivity(MainActivity4.User_ID,
                                        MainActivity4.User,MainActivity4.SHR_Model.GetKeyFromMainDbID(db.getID()));
                                publicActivity.setBaseAccept(db.getStatus());
                                publicActivity.setPostDate(MainActivity.ConvertDateToString(db.getDate()));
                                publicActivity.setTime(MainActivity.ConvertTimeToString(db.getTime()));
                                publicActivity.setDetail(db.getDescription());
                                publicActivity.setLocationName(db.getLocationName());
                                publicActivity.setLatitude(db.getLatitude());
                                publicActivity.setLongitude(db.getLongitude());*/
                               /* mUsername.add(MainActivity4.User);
                                mActivity.add(db.getDescription());
                                if(db.getStatus()==1||db.getStatus()==2){
                                    mLocation.add(db.getLocationName());
                                }else{
                                    if(db.getStatus()==3){
                                        mLocation.add("");
                                    }else if(db.getStatus()==4){
                                        mLocation.add("Owner Accept Request");
                                    }
                                }
                                mTime.add(MainActivity.ConvertTimeToString(db.getTime()));
                            }
                        }
                    }*/
                    //swap
                    for(int i=0;i<mTime.size();i++){
                        for(int j=0;j<mTime.size()-1;j++){
                            String message = deleteColon(mTime.get(j));
                            String message2 = deleteColon(mTime.get(j+1));
                            if(Integer.parseInt(message) > Integer.parseInt(message2)){
                                String swapUser = mUsername.get(j);
                                mUsername.set(j,mUsername.get(j+1));
                                mUsername.set(j+1, swapUser);
                                String swapAct = mActivity.get(j);
                                mActivity.set(j,mActivity.get(j+1));
                                mActivity.set(j+1, swapAct);
                                String swapLoc = mLocation.get(j);
                                mLocation.set(j,mLocation.get(j+1));
                                mLocation.set(j+1, swapLoc);
                                String swapTime = mTime.get(j);
                                mTime.set(j,mTime.get(j+1));
                                mTime.set(j+1, swapTime);
                            }
                        }
                    }
                    //
                    mRecyclerView = (RecyclerView) findViewById(R.id.textRecycler);
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(SocialPlan.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new SocialPlanAdapter(SocialPlan.this,mUsername,mActivity,mLocation,mTime);
                    mRecyclerView.setAdapter(mAdapter);
                    isUpdate=false;
                }
                if(!SocialPlan.this.isFinishing())
                    handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);


    }

    public static boolean isInNewFeed(PublicActivity publicActivity){

        for(int i=0;i<publicActivities.size();i++){
            PublicActivity pb=publicActivities.get(i);
            if(pb.getBaseAccept()==publicActivity.getBaseAccept()&&
                    pb.getDetail().compareTo(publicActivity.getDetail())==0&&
                    pb.getPostDate().compareTo(publicActivity.getPostDate())==0&&
                    pb.getTime().compareTo(publicActivity.getTime())==0&&
                    pb.getUser_ID()==publicActivity.getUser_ID()){
                        return true;
            }
        }

        return false;
    }

    public static int getIDbyUsername(String name){
        for(int i=0;i<memberName.size();i++){
            if(name.compareTo(memberName.get(i))==0){
                return i;
            }
        }
        return -1;
    }

    public static String InMemberList(int id){
        try{
            for(int i=0;i<memberList.size();i++){
                if(id==memberList.get(i)){
                    return memberName.get(i);
                }
            }
        }catch (IndexOutOfBoundsException e){

        }
        return null;
    }

    public static int getindexArrayByUserID(int id){
        for(int i=0;i<memberList.size();i++){
            if(memberList.get(i)==id){
                return i;
            }
        }
        return -1;
    }
    public String deleteColon(String message){
        String one = message;
        int colon = one.indexOf(':');
        int last = one.length();
        Log.d("firstColon: ", String.valueOf(colon));
        Log.d("Last: ", String.valueOf(last));
        String parsed = one.substring(0,colon)+ one.substring(colon+1,last);
        Log.d("Parsed: ",parsed);
        return parsed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUsername.clear();
        mActivity.clear();
        mLocation.clear();
        mTime.clear();
    }

}
