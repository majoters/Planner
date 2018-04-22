package com.example.hotmildc.shareact;

import android.content.Intent;
import android.os.Handler;
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

public class ShowListFriends extends AppCompatActivity {
    ListView showlist;
    ArrayList<String> friends;
    FirebaseDatabase firebaseDatabase;
    String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_friends);
        Button back = (Button)findViewById(R.id.button6);
        showlist=(ListView)findViewById(R.id.ListFriends);
        friends=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ShowListFriends.this, MainActivity4.class);
                startActivity(back);
                finish();
            }
        });

        final Handler handler = new Handler();
        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                        MainActivity4.User_ID+"/ListFriend/");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            if(ds.getValue().toString().compareTo("-1")!=0){
                                msg=ds.getKey().toString();
                                friends.clear();
                                DatabaseReference databaseReference1=firebaseDatabase.getReference(
                                        "Users/"+msg
                                );
                                databaseReference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String OwnerName=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                        String ID=msg;
                                        if(!isInArray(OwnerName+" "+ID)){
                                            friends.add(OwnerName+" "+ID);
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                                    ShowListFriends.this,android.R.layout.simple_list_item_1,android.R.id.text1,friends);
                                            showlist.setAdapter(adapter);
                                        }
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
                handler.postDelayed(this,100);
            }
        };
        handler.post(runnable);

        showlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int space=friends.get(position).indexOf(" ");
                String user=friends.get(position).substring(0,space);
                String ID=friends.get(position).substring(space+1);
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"
                +ID+"/ListFriend/");
                databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(null);

                databaseReference = firebaseDatabase.getReference("Users/"
                        +MainActivity4.User_ID+"/ListFriend/");
                databaseReference.child(String.valueOf(ID)).setValue(null);

                /*friends.remove(position);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        ShowListFriends.this,android.R.layout.simple_list_item_1,android.R.id.text1,friends);
                showlist.setAdapter(adapter);*/

                return false;
            }
        });
    }

    public boolean isInArray(String msg){
        for(int i=0;i<friends.size();i++){
            if(friends.get(i).compareTo(msg)==0){
                return true;
            }
        }
        return false;
    }

}
