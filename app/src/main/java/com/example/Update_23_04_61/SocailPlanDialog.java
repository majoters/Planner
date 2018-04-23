package com.example.Update_23_04_61;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.SocialPlan;

/**
 * Created by root on 23/4/2561.
 */

public class SocailPlanDialog extends Dialog {
    String username;
    String activity;
    String time;
    int index;
    Context mCx;
    FirebaseDatabase firebaseDatabase;
    boolean checkPress=false;
    public SocailPlanDialog(@NonNull Context context,String Username,String Activity,String Time,int index) {
        super(context);
        this.username=Username;
        this.activity=Activity;
        this.time=Time;
        this.index=index;
        mCx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_plan_detail);
        TextView username_show = (TextView)findViewById(R.id.edit_activity);
        TextView activity_show = (TextView)findViewById(R.id.textView23);
        TextView Time = (TextView)findViewById(R.id.textTime);
        username_show.setText(username);
        activity_show.setText(activity);
        Time.setText(time);
        firebaseDatabase = FirebaseDatabase.getInstance();
        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int BaseAccept= SocialPlan.publicActivities.get(index).getBaseAccept();
                    Toast.makeText(mCx,username,Toast.LENGTH_SHORT).show();

                    if(BaseAccept==1) {
                        String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getUser_ID())+
                                "/"+SocialPlan.publicActivities.get(index).getUsername()+"/PublicActivity/"+
                                SocialPlan.publicActivities.get(index).getFirebasekey()+"/";
                        MainActivity4.Clone1(ref, index);
                    }else if(BaseAccept==3){
                        String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getUser_ID())+
                                "/"+SocialPlan.publicActivities.get(index).getUsername()+"/PublicActivity/"+
                                SocialPlan.publicActivities.get(index).getFirebasekey()+"/";
                        MainActivity4.RequestAct3(ref,index);
                    }else if(BaseAccept==2){
                        String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getJoiner())+
                                "/";
                        DatabaseReference db = firebaseDatabase.getReference(ref);
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getJoiner())+
                                        "/"+User+"/PublicActivity/"+SocialPlan.publicActivities.get(index).getFirebasekey();
                                MainActivity4.Clone2(ref,index);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else if(BaseAccept==4){
                        checkPress=true;
                        String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getJoiner())+
                                "/";
                        DatabaseReference db = firebaseDatabase.getReference(ref);
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getJoiner())+
                                        "/"+User+"/PublicActivity/"+SocialPlan.publicActivities.get(index).getFirebasekey();
                                if(checkPress){
                                    MainActivity4.RequestAct4(ref,index);
                                    checkPress=false;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }catch (IndexOutOfBoundsException e) {
                    Toast.makeText(mCx,"It's Your Activity",Toast.LENGTH_SHORT).show();
                }
                cancel();
            }

        });

    }
}
