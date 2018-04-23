package com.example.Update_23_04_61;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hotmildc.shareact.MainActivity;
import com.felipecsl.gifimageview.library.GifImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.CustomSearch;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.ShareType;

public class PublicZone extends AppCompatActivity {
    public String User;
    public String description="Post";
    public ListView result;
    public EditText Search;
    public ArrayList<List_Database> DataPost;
    public ArrayList<ShareType> DataShare;
    public static FirebaseDatabase firebaseDatabase;
    public static int BaseAccept=0;
    public CustomSearch adapter;
    public static String Username_search,ID_search;
    public boolean found = false;
    public String User_Daily,ID_Daily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_zone);
        /*for(int i=0;i<10;i++){
            date.add(String.valueOf(i+1)+"/04/2018");
            event.add("Chang Friend Fest"+String.valueOf(i+1));
            location.add("KMITL"+String.valueOf(i+1));
        }*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PublicZone.this, MainActivity4.class));
            }
        });
        Search=findViewById(R.id.editText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        DataPost=new ArrayList<>();
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
                                            try {
                                                int date_post=MainActivity.convertDate(ds.child("Date").getValue().toString());
                                                if(date_post>=date_compare&&
                                                        ds.child("CreatorOrJoiner").child("BaseAccept").getValue().toString().compareTo("1")==0){

                                                    List_Database list_database = new List_Database(
                                                            MainActivity.convertDateToForm(ds.child("Date").getValue().toString()),
                                                            MainActivity.convertTime(ds.child("Time").getValue().toString()),
                                                            ds.child("Detail").getValue().toString(),
                                                            ds.child("Location").child("name").getValue().toString(),
                                                            Double.parseDouble(ds.child("Location").child("latitude").getValue().toString()),
                                                            Double.parseDouble(ds.child("Location").child("longitude").getValue().toString()),
                                                            2);

                                                    ShareType shareType = new ShareType(-1,ds.getRef().toString(),ds.getKey().toString(),1,Integer.parseInt(ID_search));

                                                    DataPost.add(list_database);
                                                    DataShare.add(shareType);

                                                }
                                            }catch (NullPointerException e){

                                            }


                                        }
                                        /*CustomSearch adapter = new CustomSearch(MainActivity.this,DataPost);
                                        result.setAdapter(adapter);
                                        result.invalidate();*/

                                        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                        mRecyclerView.setHasFixedSize(true);
                                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(PublicZone.this,LinearLayoutManager.HORIZONTAL,false);
                                        mRecyclerView.setLayoutManager(mLayoutManager);
                                        final PublicZoneAdapter mAdapter = new PublicZoneAdapter(PublicZone.this,DataPost,DataShare,Integer.parseInt(ID_search),Username_search);
                                        mRecyclerView.setAdapter(mAdapter);
                                        found = false;
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

        DatabaseReference getAllPost = database.getReference("Profiles/");
        getAllPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataPost.clear();
                DataShare.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    User_Daily = ds.child("Username").getValue().toString();
                    ID_Daily = ds.child("ID").getValue().toString();

                    FirebaseDatabase getPublic = FirebaseDatabase.getInstance();
                    DatabaseReference getAct = getPublic.getReference("Users/"+ID_Daily+"/"+User_Daily+
                    "/PublicActivity/");

                    getAct.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Date date = new Date();
                            int date_compare = (1900+date.getYear())*10000+((date.getMonth()+1)*100)+(date.getDate());
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                try {
                                    int date_post=MainActivity.convertDate(ds.child("Date").getValue().toString());
                                    if(date_post>=date_compare&&
                                            ds.child("CreatorOrJoiner").child("BaseAccept").getValue().toString().compareTo("1")==0){

                                        List_Database list_database = new List_Database(
                                                MainActivity.convertDateToForm(ds.child("Date").getValue().toString()),
                                                MainActivity.convertTime(ds.child("Time").getValue().toString()),
                                                ds.child("Detail").getValue().toString(),
                                                ds.child("Location").child("name").getValue().toString(),
                                                Double.parseDouble(ds.child("Location").child("latitude").getValue().toString()),
                                                Double.parseDouble(ds.child("Location").child("longitude").getValue().toString()),
                                                2);

                                        ShareType shareType = new ShareType(-1,ds.getRef().toString(),ds.getKey().toString(),1,Integer.parseInt(ID_Daily));

                                        DataPost.add(list_database);
                                        DataShare.add(shareType);

                                    }
                                }catch (NullPointerException e){

                                }


                            }
                                        /*CustomSearch adapter = new CustomSearch(MainActivity.this,DataPost);
                                        result.setAdapter(adapter);
                                        result.invalidate();*/

                            final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                            mRecyclerView.setHasFixedSize(true);
                            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(PublicZone.this,LinearLayoutManager.HORIZONTAL,false);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            final PublicZoneAdapter mAdapter = new PublicZoneAdapter(PublicZone.this,DataPost,DataShare,Integer.parseInt(ID_Daily),User_Daily);
                            mRecyclerView.setAdapter(mAdapter);
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
