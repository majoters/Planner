package com.example.hotmildc.shareact;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.CustomSearch;
import com.project.kmitl57.beelife.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import devs.mulham.raee.sample.AutoUpdate;
import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.ShareType;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    public String User;
    public ArrayList<String> User2;
    public String description="Post";
    public ListView result;
    public EditText Search;
    public ArrayList<List_Database> DataPost;
    public ArrayList<ShareType> DataShare;
    public ArrayList<String> List_Result;
    public static int User_ID;
    public ArrayList<Integer> Uid;
    public String txt;
    public static FirebaseDatabase firebaseDatabase;
    public static int BaseAccept=0;
    private static String user="";
    public int owner;
    public int index_list;
    private String keyword;
    public CustomSearch adapter;
    private GifImageView gifImageView;
    private ImageView failed;
    private TextView noResult;
    public String Username_search,ID_search;
    public boolean found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity4.class));
            }
        });
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        result = (ListView)findViewById(R.id.result);
        Search=findViewById(R.id.editText);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DataPost=new ArrayList<>();
        List_Result=new ArrayList<>();
        DataShare=new ArrayList<>();

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                found = false;
                DataPost.clear();
                DataShare.clear();
                if(!found){
                    DatabaseReference FindUser = firebaseDatabase.getReference("Profiles");
                    FindUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                if(ds.child("ID").getValue().toString().compareTo(Search.getText().toString())==0||
                                        ds.child("Username").getValue().toString().compareTo(Search.getText().toString())==0){
                                        Username_search = ds.child("Username").getValue().toString();
                                        ID_search = ds.child("ID").getValue().toString();
                                        found = true;
                                        break;
                                }
                            }
                            if(found){
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference getResult = database.getReference("Users/"+ID_search+"/"+Username_search+"/PublicActivity/");
                                getResult.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Date date = new Date();
                                        int date_compare = (1900+date.getYear())*10000+((date.getMonth()+1)*100)+(date.getDate());
                                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                                            int date_post=convertDate(ds.child("Date").getValue().toString());
                                            if(date_post>date_compare&&
                                                    ds.child("CreatorOrJoiner").child("BaseAccept").getValue().toString().compareTo("1")==0){

                                                List_Database list_database = new List_Database(
                                                        convertDateToForm(ds.child("Date").getValue().toString()),
                                                        convertTime(ds.child("Time").getValue().toString()),
                                                        ds.child("Detail").getValue().toString(),
                                                        ds.child("Location").child("name").getValue().toString(),
                                                        Double.parseDouble(ds.child("Location").child("latitude").getValue().toString()),
                                                        Double.parseDouble(ds.child("Location").child("longitude").getValue().toString()),
                                                        2);

                                                ShareType shareType = new ShareType(-1,ds.getRef().toString(),ds.getKey().toString(),1,Integer.parseInt(ID_search));

                                                DataPost.add(list_database);
                                                DataShare.add(shareType);

                                            }

                                        }
                                        CustomSearch adapter = new CustomSearch(MainActivity.this,DataPost);
                                        result.setAdapter(adapter);
                                        result.invalidate();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }
        });

        result.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                index_list=i;

                ListSelect(i,Integer.parseInt(ID_search),Username_search);

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*public void showDataPost(DataSnapshot dataSnapshot,int id,String username) { //public void showDataPost(DataSnapshot dataSnapshot) old

        for(DataSnapshot ds: dataSnapshot.getChildren()){
           if(ds.child("CreatorOrJoiner/BaseAccept").exists()) {
               int baseAccept = Integer.parseInt(ds.child("CreatorOrJoiner").child("BaseAccept").getValue().toString());
               //Log.d("BaseAccept: ", String.valueOf(baseAccept));
               if (baseAccept == 1 || baseAccept == 3) {
                   Log.d("baseAccept: ", "ThisStep");
                   int status = baseAccept;
                   //owner=Integer.parseInt(ds.child("CreatorOrJoiner").child("Joiner").getValue().toString());

                   PositionTarget positionTarget = new PositionTarget();
                   Post post = new Post();
                   post.setId(id); //new
                   post.setUsername(username); //new
                   post.setDetail(ds.child("Detail").getValue().toString());
                   post.setDate(ds.child("Date").getValue().toString());
                   post.setTime(ds.child("Time").getValue().toString());
                   positionTarget.setName(ds.child("Location").getValue(PositionTarget.class).getName());
                   positionTarget.setLatitude(ds.child("Location").getValue(PositionTarget.class).getLatitude());
                   positionTarget.setLongitude(ds.child("Location").getValue(PositionTarget.class).getLongitude());
                   post.setLocation(positionTarget);

                   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                   DateFormat timeFormat = new SimpleDateFormat("hh:mm");
                   Date d = new Date();
                   Log.d("GetPost: ", String.valueOf(convertDate(post.getDate())));
                   if (convertDate(post.getDate()) > convertDate(String.valueOf(dateFormat.format(d)))) {
                       Log.d("putPost: ", "ThisStep");
                       if (!IsInDataPost(post)) {
                           putPost(post);
                       }
                       DataShare.add(new ShareType(-1, ds.getRef().toString(),
                               ds.getKey(), status, id));   //old ds.getKey(), status, User_ID));
                       getAllPost();
                       Toast.makeText(MainActivity.this, String.valueOf(id), Toast.LENGTH_SHORT).show(); //old String.valueOf(User_ID)
                   } else {
                       Log.d("putPost2: ", "ThisStep");
                       if (convertDate(post.getDate()) == convertDate(String.valueOf(dateFormat.format(d)))) {
                           Log.d("putPost2: ", "convertDate==");
                           if (convertTime(post.getTime()) > convertTime(String.valueOf(timeFormat
                                   .format(d)))) {
                               Log.d("putPost2: ", "ThisStep");
                               if (!IsInDataPost(post)) {
                                   putPost(post);
                               }
                               DataShare.add(new ShareType(-1, ds.getRef().toString(),
                                       ds.getKey(), status, id)); //old ds.getKey(), status, User_ID));
                               getAllPost();
                               Toast.makeText(MainActivity.this, String.valueOf(id), Toast.LENGTH_SHORT).show(); //old String.valueOf(User_ID)
                           }
                       }
                   }


               }
           }
        }
    }*/

    public static int convertTime(String time) {
        int TimeInt =0;

        if(time!=null){
            TimeInt=Integer.parseInt(String.valueOf(time.subSequence(0,2)))*100;
            TimeInt+=Integer.parseInt(String.valueOf(time.subSequence(3,5)));
        }
        return TimeInt;
    }

    public void putPost(Post post){
        if(!IsInDataPost(post)){
            Post p1=new Post();
            p1=getnewPost(post);
            //DataPost.add(p1);
            List_Result.add(post.getDetail()+"\n"+post.getDate()+"\n"+post.getTime()+"\n"+post.getLocation().getName());
            //old
            //result.invalidate();
            //CustomSearch adapter = new CustomSearch(MainActivity.this,DataPost);
            //result.setAdapter(adapter);
            //result.invalidate();
        }
    }


    public void getAllPost() {
        String txt = "";
        if (DataPost.size() > 0) {
            for (int i = 0; i < DataPost.size(); i++) {
                txt += String.valueOf(DataPost.get(i).getDate()) + "\n";

            }
            //result.setText(txt);
        }

    }

    public static int convertDate(String input){
        int dateInt =0;

        if(input!=null){
            dateInt=Integer.parseInt(String.valueOf(input.subSequence(6,input.length())))*10000;
            dateInt+=Integer.parseInt(String.valueOf(input.subSequence(3,5)))*100;
            dateInt+=Integer.parseInt(String.valueOf(input.subSequence(0,2)));
        }

        return dateInt;

    }


    public static int convertDateToForm(String input){
        int dateInt=0;
        if(input!=null){
            dateInt=Integer.parseInt(String.valueOf(input.subSequence(0,2)))*10000;
            dateInt+=Integer.parseInt(String.valueOf(input.subSequence(3,5)))*100;
            dateInt+=Integer.parseInt(String.valueOf(input.subSequence(8,input.length())));
        }
        return dateInt;
    }

    public static int ConvertTimeToForm(String input){
        int TimeInt=0;
        if(input!=null){
            TimeInt=Integer.parseInt(String.valueOf(input.subSequence(0,2)))*100;
            TimeInt+=Integer.parseInt(String.valueOf(input.subSequence(3,5)));
        }
        return TimeInt;
    }

    public static String ConvertTimeToDouble(Double input){
        String TimeInt="";
        int d;
        if (String.valueOf(input/100).indexOf('.',0)==1){
            d=Integer.parseInt(String.valueOf(input/100).substring(0,1));
        }
        else{

            d=Integer.parseInt(String.valueOf(input/100).substring(0,2));
        }
        if(input!=null){
            if(d<10){
                TimeInt="0"+String.valueOf(d)+":";
            }else{
                TimeInt=String.valueOf(d)+":";
            }
            if(input-100*d<10){
                TimeInt+="0"+String.valueOf(input-100*d);
            }else{
                TimeInt+=String.valueOf(input-100*d);
            }
        }
        return TimeInt;
    }

    public static String ConvertTimeToString(int i){
        String string="";
        if(i/100<10){
            string+="0"+String.valueOf(i/100);
        }
        else{
            string+=String.valueOf(i/100);
        }
        if(i%100<10){
            string+=":0"+String.valueOf(i%100);
        }
        else{
            string+=":"+String.valueOf(i%100);
        }
        return string;
    }

    public static String ConvertDateToString(int n){
        String string="";

        if(n/10000<10){
            string+="0"+String.valueOf(n/10000);
        }else {
            string+=String.valueOf(n/10000);
        }

        if(n%10000/100<10){
            string+="-0"+String.valueOf(n%10000/100);
        }else{
            string+="-"+String.valueOf(n%10000/100);
        }

        if(n%100<10){
            string+="-0"+String.valueOf(2000+n%100);
        }else{
            string+="-"+String.valueOf(2000+n%100);
        }

        return string;
    }


    //from public activity online to your mobile
    public void ListSelect(int i,int id,String username){
        Date date = new Date();
        List_Database list_database = new List_Database(DataPost.get(i).getDate(),
                DataPost.get(i).getTime(),DataPost.get(i).getDescription(),
                DataPost.get(i).getLocationName(),DataPost.get(i).getLatitude(),
                DataPost.get(i).getLongitude(),2);

        MainActivity4.mDbAdabter_Model.createActivityList(list_database);
        MainActivity4.Refresh();

        MainActivity4.mDbDataForAnalysis_Model.InsertData(date.getHours()*100+date.getMinutes(),
                list_database);

        MainActivity4.values_filter.add(list_database);

        MainActivity4.SHR_Model.InsertData(new ShareType(MainActivity4.SearchIndex(list_database),
                DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,id)); //old DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,User_ID));

        MainActivity4.RefreshShareList();
        MainActivity4.listView.invalidate();

        Post post = AutoUpdate.ListToPost(DataPost.get(i));

        AutoUpdate.pullFromFire(post,new ShareType(MainActivity4.mDbAdabter_Model.ListToID(list_database),
                DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,id),DataShare.get(i).getFirebaseKey()); //new
                //DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,User_ID),DataShare.get(i).getFirebaseKey());
        AutoUpdate.AddToMemberList(DataShare.get(i));

        Toast.makeText(MainActivity.this,"Already Added",Toast.LENGTH_SHORT).show();

    }

    //
    /*public void OnRequest(int i,int id,String username){
       List_Database list_database = new List_Database(convertDateToForm(DataPost.get(i).getDate()),
                ConvertTimeToForm("00.00"),"Detail",
                "Empty",0,
                0,-1);

        MainActivity4.mDbAdabter_Model.createActivityList(list_database);
        MainActivity4.Refresh();
        MainActivity4.values_filter.add(list_database);

        MainActivity4.SHR_Model.InsertData(new ShareType(MainActivity4.mDbAdabter_Model.ListToID(list_database),
                DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),-1,id)); //new
                //DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),-1,User_ID));
        MainActivity4.RefreshShareList();
        MainActivity4.listView.invalidate();

        AutoUpdate.PushEmpty(DataShare.get(i).getFirebaseKey(),DataShare.get(i),DataPost.get(i).getDate());
        AutoUpdate.AddToMemberListPermiss(DataShare.get(i));/*
        Toast.makeText(MainActivity.this,"Already Added Private",Toast.LENGTH_SHORT).show();


    }*/

    public static void SetUserName(String user_){
        user=user_;
    }

    public static String getUserName(){
        return user;
    }
    public static void getUser(int ID){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                ID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SetUserName(dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("=")));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static String ConvertDateToStringM(int n){
        String string="";
        String m="";
        if(n/10000<10){
            string+="0"+String.valueOf(n/10000);
        }else {
            string+=String.valueOf(n/10000);
        }

        if(n%10000/100<10){
            switch (n%10000/100){
                case 1:
                    m="JAN";
                    break;
                case 2:
                    m="FEB";
                    break;
                case 3:
                    m="MAR";
                    break;
                case 4:
                    m="APR";
                    break;
                case 5:
                    m="MAY";
                    break;
                case 6:
                    m="JUNE";
                    break;
                case 7:
                    m="JULY";
                    break;
                case 8:
                    m="AUG";
                    break;
                case 9:
                    m="SEPT";
                    break;
                default:
                    m="MONTH";
                    break;
            }
            string+=" "+m;
        }else{
            switch (n%10000/100){
                case 10:
                    m="OCT";
                    break;
                case 11:
                    m="NOV";
                    break;
                case 12:
                    m="DEC";
                    break;
                default:
                    m="MONTH";
                    break;
            }
            string+=" "+m;
        }

        if(n%100<10){
            string+=" 0"+String.valueOf(2000+n%100);
        }else{
            string+=" "+String.valueOf(2000+n%100);
        }

        return string;
    }

    public boolean IsInDataPost(Post post){


        for(int i=0;i<DataPost.size();i++){
            Post datapost=new Post();
            //datapost=getnewPost(DataPost.get(i));
            if(datapost.getDate()==post.getDate()&&
                    datapost.getTime()==post.getTime()&&
                    datapost.getDetail().compareTo(post.getDetail())==0&&
                    datapost.getLocation().getName().compareTo(post.getLocation().getName())==0){
                return true;
            }
        }
        return false;
    }

    public Post getnewPost(Post post){
        Post post1 = new Post();
        PositionTarget positionTarget = new PositionTarget();
        post1.setDetail(post.getDetail());
        post1.setDate(post.getDate());
        post1.setTime(post.getTime());
        positionTarget.setName(post.getLocation().getName());
        positionTarget.setLatitude(post.getLocation().getLatitude());
        positionTarget.setLongitude(post.getLocation().getLongitude());
        post1.setLocation(positionTarget);
        return post1;
    }
    private class GetSelectTask extends AsyncTask<Void, Void, String> {
        private Post post;
        int index_list;
        public GetSelectTask(Post obj,int i){
            post=obj;
            index_list = i;
        }
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }
        protected String doInBackground(Void... params)   {
            String result="";
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+   //new
                    post.getId()+"/"+post.getUsername()+"/"+"PublicActivity"+"/"+DataShare.get(index_list).getFirebaseKey()); //new
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        int BaseAccept = Integer.parseInt(dataSnapshot.child("CreatorOrJoiner").child("BaseAccept").getValue().toString());
                        if (BaseAccept == 1) {
                           // ListSelect(index_list,post.getId(),post.getUsername());
                        } else if (BaseAccept == 3) {
                            //OnRequest(index_list,post.getId(),post.getUsername());
                        }
                    }
                    else{
                        failed = (ImageView)findViewById(R.id.no_status);
                        noResult = (TextView)findViewById(R.id.noResult);
                        noResult.setText("No searching");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            result = "Already Added";
            return result;
        }

        protected void onPostExecute(String result)  {
            // Dismiss ProgressBar
            //updateWebView(result);
        }
    }
    private class GetResultTask extends AsyncTask<Void, Void, String> {
        private String keyword2;
        public GetResultTask(String message){
            keyword2=message;
        }
        @Override
        protected void onPreExecute() {
            //InputStream inputStream = null; //new
            //try {
            //    inputStream = getAssets().open("loading.gif");
            //    byte[] bytes = IOUtils.toByteArray(inputStream); //new
            //    gifImageView.setBytes(bytes); //new
            //    gifImageView.startAnimation(); //new
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }
        protected String doInBackground(Void... params)   {
            String result ="";
            DatabaseReference Profiles=firebaseDatabase.getReference("Profiles/");
            Profiles.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d("Profile","profile loop step");
                            try {
                                int id = Integer.parseInt(ds.child("ID").getValue().toString());
                                String Username = ds.child("Username").getValue().toString();
                                Log.d("id loop",String.valueOf(id));
                                Log.d("username loop",Username);
                                try {
                                    Log.d("word loop", String.valueOf(keyword2.toLowerCase().contains(String.valueOf(id))));
                                    if (keyword2.toLowerCase().contains(String.valueOf(id)) ||
                                            keyword2.toLowerCase().contains(Username.toLowerCase())) {
                                        Uid.add(id); //User_id
                                        User2.add(Username); //Username
                                        //if(id==Integer.parseInt(keyword)||Username.compareTo(keyword)==0){
                                        //User_ID=id;
                                        //User=Username;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    if (keyword2.toLowerCase().contains(Username.toLowerCase())) {
                                        Uid.add(id); //User_id
                                        User2.add(Username); //Username
                                        //if(Username.compareTo(keyword)==0){
                                        //User_ID=id;
                                        //User=Username;
                                        break;
                                    }
                                }
                            } catch (NullPointerException e) {

                            }

                        }
                        if (User2 != null && Uid != null) {
                            //if(User!=null&&User_ID!=-1){
                            for (int i = 0; i < User2.size(); i++) {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + //new
                                        Uid.get(i) + "/" + User2.get(i) + "/PublicActivity/"); //new
                                final int finalI = i; //new
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //showDataPost(dataSnapshot, Uid.get(finalI), User2.get(finalI)); //showDataPost(dataSnapshot) old
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return result;
        }

        protected void onPostExecute(String result)  {
            // Dismiss ProgressBar
            //gifImageView.stopAnimation(); //new
        }
    }
}
