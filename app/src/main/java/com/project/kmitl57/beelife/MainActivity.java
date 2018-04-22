package com.project.kmitl57.beelife;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import devs.mulham.raee.sample.MainActivity4;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    public static Intent change;
    FirebaseDatabase firebaseDatabase;
    public static String Email;
    public static boolean show=false;
    private GifImageView gifImageView;
    public String User;
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        int space=Email.toString().indexOf(".");
        String usermail=Email.toString().substring(0,space);
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Profiles/"+
        usermail+"/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
        //        if(dataSnapshot.getValue().toString().indexOf("Type")!=-1){
                    int index_start=dataSnapshot.getValue().toString().indexOf("Username")+9;
                    int index_stop=dataSnapshot.getValue().toString().indexOf(",",index_start);
                    User=dataSnapshot.getValue().toString().substring(index_start,index_stop);
                    int id_start=dataSnapshot.getValue().toString().indexOf("ID")+3;
                    int id_stop=dataSnapshot.getValue().toString().indexOf(",",id_start);
                    id=Integer.parseInt(dataSnapshot.getValue().toString().substring(id_start,id_stop));
                    try{
                        InputStream inputStream = getAssets().open("loading.gif");
                        byte[] bytes = IOUtils.toByteArray(inputStream);
                        gifImageView.setBytes(bytes);
                        gifImageView.startAnimation();

                    }
                    catch(IOException ex){

                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, MainActivity4.class));
                            MainActivity4.User=User;
                            MainActivity4.User_ID=id;
                            MainActivity4.Email=Email;
                            finish();
                        }
                    },3000);
        //        }else{
        //            show=true;
        //        }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //viewPager = (ViewPager)findViewById(R.id.viewPager);
        //if(show){
        //    viewPager.setVisibility(View.VISIBLE);
        //    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,Email);

        //    viewPager.setAdapter(viewPagerAdapter);
        //}else{
        //    viewPager.setVisibility(View.INVISIBLE);
        //}
    }


}
