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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import devs.mulham.raee.sample.MainActivity4;

public class FriendsRequest extends AppCompatActivity {
    ListView listRequest;
    ArrayList<String> list;
    public FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_request);
        Button back =(Button)findViewById(R.id.button5);
        listRequest=(ListView)findViewById(R.id.ListRequest);
        list=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(FriendsRequest.this,MainActivity4.class);
                startActivity(back);
                finish();
            }
        });

        listRequest.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                        MainActivity4.FriendsRequest.get(position).getID()+"/ListFriend/");
                databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(4);

                databaseReference=firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+"/ListFriend/");
                databaseReference.child(String.valueOf(MainActivity4.FriendsRequest.get(position).getID())).setValue(4);
                return false;
            }
        });

        /*final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
               /* ArrayAdapter adapter = new ArrayAdapter<String>(FriendsRequest.this,android.R.layout.simple_list_item_1,android.R.id.text1,MainActivity4.FriendsRequest);
                listRequest.setAdapter(adapter);
                if(!FriendsRequest.this.isFinishing())
                    handler.postDelayed(this,500);
            }
        };
        handler.post(runnable);*/


    }
}
