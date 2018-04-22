package com.project.kmitl57.beelife;

import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.support.constraint.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;

public class facebookloginAct extends AppCompatActivity {
    EditText nickname;
    EditText username;
    EditText province;
    EditText hobby;
    public static String Email;
    public static String Name;
    public static String Surname;
    public static boolean show=false;
    FirebaseDatabase firebaseDatabase;
    static ArrayList<Integer> All_ID;
    public static int ID;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebooklogin);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        nickname=(EditText)findViewById(R.id.nickname);
        username=(EditText)findViewById(R.id.username);
        province=(EditText)findViewById(R.id.userprovince);
        hobby=(EditText)findViewById(R.id.userhobby);
        All_ID=new ArrayList<>();
        final ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);

        DatabaseReference getAllID=firebaseDatabase.getReference("Profiles/");
        getAllID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                All_ID.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    //Get Each ID To Array
                    int id_start=ds.getValue().toString().indexOf("ID")+3;
                    int id_stop=-1;
                    if(id_start<=-1)
                        id_stop=ds.getValue().toString().indexOf(",",id_start);
                    if(id_start>-1&&id_stop>-1){
                        String id=ds.getValue().toString().substring(id_start,id_stop);
                        int ID=Integer.parseInt(id);
                        All_ID.add(ID);
                    }
                }
                ID=GenerateID();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Email = MainActivity.Email;
        final int space=Email.toString().indexOf(".");
        DatabaseReference databaseReference =firebaseDatabase.getReference("Profiles/"+
        Email.toString().substring(0,space)+"/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot.getValue().toString().indexOf("ID")!=-1){
                        startActivity(new Intent(facebookloginAct.this,MainActivity.class));
                        MainActivity.Email=Email;
                    }else{
                        show=true;
                    }
                }catch (NullPointerException e){
                    show=true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!show){
                    layout.setVisibility(View.INVISIBLE);
                }else {
                    layout.setVisibility(View.VISIBLE);
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Usermail=Email.toString().substring(0,space);
                DatabaseReference databaseReference=firebaseDatabase.getReference("Profiles/");
                databaseReference.child(Usermail).child("Name").setValue(Name);
                databaseReference.child(Usermail).child("Surname").setValue(Surname);
                databaseReference.child(Usermail).child("Nickname").setValue(nickname.getText().toString());
                databaseReference.child(Usermail).child("Username").setValue(username.getText().toString());
                databaseReference.child(Usermail).child("Email").setValue(Email);
                databaseReference.child(Usermail).child("Province").setValue(province.getText().toString());
                databaseReference.child(Usermail).child("Hobby").setValue(hobby.getText().toString());
                databaseReference.child(Usermail).child("ID").setValue(ID);
                startActivity(new Intent(facebookloginAct.this,MainActivity.class));
                MainActivity.Email=Email;
            }
        });

    }

    public static int GenerateID(){
        Date date = new Date();
        int hour=date.getHours();
        int minute=date.getMinutes();
        int ID=hour*100+minute;
        boolean complete=false;
        while (!complete){
            int c=0;
            for(int i=0;i<All_ID.size();i++){
                if(All_ID.get(i)==ID){
                    c++;
                }
            }
            if(c==0){
                complete=true;
            }else{
                if(ID>10000)
                    ID=ID-10000;
                else
                    ID+=1;
            }
        }
        return ID;
    }

}
