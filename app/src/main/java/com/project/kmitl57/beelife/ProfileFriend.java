package com.project.kmitl57.beelife;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import devs.mulham.raee.sample.FriendDataForPF;
import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.MyPagerAdapter;
import devs.mulham.raee.sample.MyPagerAdapter2;
import devs.mulham.raee.sample.Request;

public class ProfileFriend extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    public static int  friendID;
    public static String username=null;
    public static ArrayList<FriendDataForPF> numberAct;
    public static int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_friend);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileFriend.this, MainActivity4.class);
                ProfileFriend.this.startActivity(intent);
            }
        });
        numberAct=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    if(Integer.parseInt(dataSnapshot1.child("ID").getValue().toString())==friendID){
                        username=dataSnapshot1.child("Username").getValue().toString();
                        String name=dataSnapshot1.child("Name").getValue().toString();
                        String surname=dataSnapshot1.child("Surname").getValue().toString();
                        TextView textusername=(TextView)findViewById(R.id.textFriend);
                        TextView textname=(TextView)findViewById(R.id.textUName);
                        textusername.setText(username);
                        textname.setText(name+" "+surname);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyPagerAdapter2 pagerAdapter2 = new MyPagerAdapter2(getApplicationContext(),getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter2);
        TabLayout tabLayout =(TabLayout)findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

    }


}
