package com.example.hotmildc.shareact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import devs.mulham.raee.sample.MainActivity4;

public class ListShare extends AppCompatActivity {
    ListView listshare ;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> FriendShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_share);

        firebaseDatabase=FirebaseDatabase.getInstance();
        Button back = (Button)findViewById(R.id.button7);
        Button refresh = (Button)findViewById(R.id.button8);
        listshare = (ListView)findViewById(R.id.Sharing);
        FriendShare=new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ListShare.this, MainActivity4.class);
                startActivity(back);
                finish();
            }
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
        MainActivity4.User_ID+"/"+MainActivity4.User+"/PublicActivity/"+MainActivity4.FirebaseAct+
        "/CreatorOrJoiner/MemberList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(Integer.parseInt(ds.getValue().toString())!=0){
                        final String ID=ds.getKey().toString();
                        DatabaseReference getUser=firebaseDatabase.getReference("Users/"+ID);
                        getUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String data=dataSnapshot.toString();
                                int stop=data.indexOf("=");
                                String user=data.substring(1,stop)+" "+ID;
                                FriendShare.add(user);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListShare.this,android.R.layout.simple_list_item_1,
                android.R.id.text1,FriendShare);
        listshare.setAdapter(adapter);


        listshare.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int cut=FriendShare.get(position).indexOf(" ");
                String User=FriendShare.get(position).substring(0,cut);
                String ID=FriendShare.get(position).substring(cut+1);

                DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users/"+
                ID+"/"+User+"/PublicActivity/"+MainActivity4.FirebaseAct+"/CreatorOrJoiner/"+
                "BaseAccept/");

                databaseReference1.setValue(9);

                return false;
            }
        });

    }
}
