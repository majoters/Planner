package com.example.hotmildc.shareact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import devs.mulham.raee.sample.MainActivity4;

public class AddFriends extends AppCompatActivity {
    public static FirebaseDatabase firebaseDatabase;
    public static ArrayList<String> contract;
    public static ListView ListSearch;
    public String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        firebaseDatabase = FirebaseDatabase.getInstance();
        final EditText Editname=(EditText)findViewById(R.id.EditName);
        final Button Search = (Button)findViewById(R.id.Search);
        ListSearch = (ListView)findViewById(R.id.ListFriends);
        contract = new ArrayList<>();

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contract.clear();
                if(isNumber(Editname.getText().toString())){
                    search = Editname.getText().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                    search);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String OwnerName=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                            String message = OwnerName+" "+search;
                            contract.add(message);
                            ArrayAdapter adapter = new ArrayAdapter<String>(AddFriends.this,android.R.layout.simple_list_item_1,android.R.id.text1,contract);
                            ListSearch.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    search = Editname.getText().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(int i=0;i<dataSnapshot.toString().length();){
                                int index_name=dataSnapshot.toString().indexOf(search,i);
                                if(index_name!=-1){
                                    i=index_name+1;
                                    int index_ID=index_name-6;
                                    String ID=dataSnapshot.toString().substring(index_ID,index_ID+4);
                                    String message = search+" "+ID;
                                    if(isNumber(ID)&&!isInList(message)){
                                        contract.add(message);
                                    }
                                    ArrayAdapter adapter = new ArrayAdapter<String>(AddFriends.this,android.R.layout.simple_list_item_1,android.R.id.text1,contract);
                                    ListSearch.setAdapter(adapter);
                                }else {
                                    i=dataSnapshot.toString().length()+1;
                                    if(contract.size()==0)
                                    {
                                        String message = "Not Found";
                                        if(!isInList(message))
                                            contract.add(message);
                                    }
                                    ArrayAdapter adapter = new ArrayAdapter<String>(AddFriends.this,android.R.layout.simple_list_item_1,android.R.id.text1,contract);
                                    ListSearch.setAdapter(adapter);
                                }
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        ListSearch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(contract.get(position).compareTo("Not Found")!=0){
                    int space=contract.get(position).toString().indexOf(" ");
                    String user=contract.get(position).substring(0,space);
                    String ID=contract.get(position).substring(space+1);
                    Toast.makeText(AddFriends.this,user+" "+ID,Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+ID+"/ListFriend");
                    databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(-1);

                }
                return false;
            }
        });

    }

    public boolean isNumber(String msg){
        for(int i=0;i<msg.length();i++){
            if(msg.charAt(i)-48>9)
                return false;
        }
        return true;
    }

    public boolean isInList(String msg){
        for(int i=0;i<contract.size();i++){
            if(contract.get(i).compareTo(msg)==0){
                return true;
            }
        }
        return false;
    }
}
