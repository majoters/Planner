package devs.mulham.raee.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hotmildc.shareact.AddFriends;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FindFriendDialog extends Dialog {
    Button cancel;
    public static FirebaseDatabase firebaseDatabase;
    public static ArrayList<String> contract;
    public String search;
    public FindFriendDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_dialog);
        cancel = (Button) findViewById(R.id.cancel);

        //----------------------------------------------------
        firebaseDatabase = FirebaseDatabase.getInstance();
        final EditText Editname=(EditText)findViewById(R.id.editText);
        ImageButton Search = (ImageButton)findViewById(R.id.search);
        contract = new ArrayList<>();

        //----------------------------------------------------
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.friend_list);
        cancel = (Button) findViewById(R.id.cancel);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.Adapter mAdapter = new FindFriendAdapter(getContext(),contract);
        mRecyclerView.setAdapter(mAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                }, 500);
            }
        });


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contract.clear();
                if(isNumber(Editname.getText().toString())){
                    search = Editname.getText().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                try{
                                    if(ds.getValue(getProfiles.class).getID()==Integer.parseInt(search)){
                                        String OwnerName=ds.getValue(getProfiles.class).getUsername();
                                        String message = OwnerName+" "+search;
                                        contract.add(message);
                                    }
                                }catch (NullPointerException e){
                                    /*String message = "Not Found";
                                    if(!isInList(message))
                                        contract.add(message);*/
                                    Toast.makeText(getContext(),"Not Found",Toast.LENGTH_LONG).show();
                                }
                            }
                            RecyclerView.Adapter mAdapter = new FindFriendAdapter(getContext(),contract);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    search = Editname.getText().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                if(ds.getValue(getProfiles.class).getUsername().compareTo(search)==0){
                                    String ID=String.valueOf(ds.getValue(getProfiles.class).getID());
                                    String message = search+" "+ID;
                                    contract.add(message);
                                }
                            }

                            if(contract.size()<=0){
                                String message = "Not Found";
                                contract.add(message);
                            }

                            RecyclerView.Adapter mAdapter = new FindFriendAdapter(getContext(),contract);
                            mRecyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
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
