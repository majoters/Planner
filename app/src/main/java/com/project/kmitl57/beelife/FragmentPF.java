package com.project.kmitl57.beelife;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import devs.mulham.raee.sample.MainActivity4;

public class FragmentPF extends android.support.v4.app.Fragment {
    FirebaseDatabase firebaseDatabase;
    TextView username ;
    TextView province ;
    TextView hobby;
    TextView email;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_pf,container,false);

        firebaseDatabase= FirebaseDatabase.getInstance();
        //username2 =(TextView)rootView.findViewById(R.id.textUserName2);
        //name =(TextView)rootView.findViewById(R.id.textUName);
        username =(TextView)rootView.findViewById(R.id.textUsername);
        province =(TextView)rootView.findViewById(R.id.textProvince);
        hobby = (TextView)rootView.findViewById(R.id.textHobby);
        email = (TextView)rootView.findViewById(R.id.textEmail);

        int space= MainActivity4.Email.toString().indexOf(".");
        String useremail=MainActivity4.Email.toString().substring(0,space);
        DatabaseReference databaseReference=firebaseDatabase.getReference("Profiles/"+
                useremail+"/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username_=dataSnapshot.child("Username").getValue().toString();
                String name_=dataSnapshot.child("Name").getValue().toString();
                String surname_=dataSnapshot.child("Surname").getValue().toString();
                String province_=dataSnapshot.child("Province").getValue().toString();
                String hobby_=dataSnapshot.child("Hobby").getValue().toString();
                username.setText(username_);
                ProfilePage.name.setText(name_+" "+surname_);
                ProfilePage.username2.setText(username_);
                province.setText(province_);
                hobby.setText(hobby_);
                email.setText(MainActivity4.Email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;

    }
}
