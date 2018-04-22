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

import devs.mulham.raee.sample.AutoCheckRequest;
import devs.mulham.raee.sample.AutoUpdate;
import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.ShareType;

public class RequestActivity extends AppCompatActivity {


    public static FirebaseDatabase firebaseDatabase;
    ListView listView;
    Bundle saveInstanceState;
    ArrayList<String> allrequest;
    ShareType shareType=null;
    public String User=null;
    public Post post=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveInstanceState=savedInstanceState;
        setContentView(R.layout.activity_request1);
        listView = (ListView)findViewById(R.id.List);
        allrequest = new ArrayList<>();
        Button back = (Button)findViewById(R.id.back);
        allrequest.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        /*for(int i=0;i<MainActivity4.ListRequest.size();i++){
            String txt="";
            if(MainActivity4.ListRequest.get(i).getStatus()==-1){
                txt+=String.valueOf(MainActivity4.ListRequest.get(i).getID())+"\n";
                txt+="Send Request To You About Follow Activity"+"\n";
                txt+=MainActivity4.ListRequest.get(i).getDetail()+"\n";
                txt+=MainActivity4.ListRequest.get(i).getDate()+"\n";
            }
            if(MainActivity4.ListRequest.get(i).getStatus()==-2){
                txt+=String.valueOf(MainActivity4.ListRequest.get(i).getID())+"\n";
                txt+="Send Activity To You"+"\n";
                txt+=MainActivity4.ListRequest.get(i).getDetail()+"\n";
                txt+=MainActivity4.ListRequest.get(i).getDate()+"\n";
            }
            allrequest.add(txt);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1, allrequest);
        listView.setAdapter(adapter);
        listView.invalidate();*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(RequestActivity.this,MainActivity4.class);
                startActivity(back);
                finish();
            }
        });

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!RequestActivity.this.isFinishing()){
                    Intent j = new Intent(RequestActivity.this,AutoCheckRequest.class);
                    RequestActivity.this.startService(j);
                }

                allrequest.clear();
                firebaseDatabase = FirebaseDatabase.getInstance();
                for(int i=0;i<MainActivity4.ListRequest.size();i++){
                    String txt="";
                    if(MainActivity4.ListRequest.get(i).getStatus()==-1){
                        txt+=String.valueOf(MainActivity4.ListRequest.get(i).getID())+"\n";
                        txt+="Send Request To You About Follow Activity"+"\n";
                        txt+=MainActivity4.ListRequest.get(i).getDeta()+"\n"; //Detail
                        txt+=MainActivity4.ListRequest.get(i).getDat()+"\n";
                    }
                    if(MainActivity4.ListRequest.get(i).getStatus()==-2){
                        txt+=String.valueOf(MainActivity4.ListRequest.get(i).getID())+"\n";
                        txt+="Send Activity To You"+"\n";
                        txt+=MainActivity4.ListRequest.get(i).getDeta()+"\n";
                        txt+=MainActivity4.ListRequest.get(i).getDat()+"\n";
                    }
                    allrequest.add(txt);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1, allrequest);
                listView.setAdapter(adapter);
                listView.invalidate();

                if(!isFinishing())
                    handler.postDelayed(this,500);
            }
        };
        handler.post(runnable);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //MainActivity4.ListRequest.get(position);
                final int i=position;
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                MainActivity4.ListRequest.get(position).getID());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                        int ID=0;
                        List_Database list_database;
                        try {
                            ID=MainActivity4.getforiegnkeyFromFirebasekey(MainActivity4.ListRequest.get(i).getFirebasekey());
                            list_database = MainActivity4.IDtoList(ID);
                        }catch (IndexOutOfBoundsException e){
                            list_database =null;
                        }

                        post = new Post();
                        shareType=null;
                        if(list_database!=null){
                            post.setDetail(list_database.getDescription());
                            post.setDate(MainActivity.ConvertDateToString(list_database.getDate()));
                            post.setTime(MainActivity.ConvertTimeToString(list_database.getTime()));
                            PositionTarget positionTarget = new PositionTarget();
                            positionTarget.setLatitude(list_database.getLatitude());
                            positionTarget.setLongitude(list_database.getLongitude());
                            positionTarget.setName(list_database.getLocationName());
                            post.setLocation(positionTarget);
                            shareType = new ShareType(ID,dataSnapshot.getRef().toString(),
                                    MainActivity4.ListRequest.get(i).getFirebasekey(),4,
                                    MainActivity4.ListRequest.get(i).getID());
                        }else {
                            DatabaseReference getPost = firebaseDatabase.getReference("Users/"+
                                    MainActivity4.ListRequest.get(position).getID()+"/"+User+"/PublicActivity/"+MainActivity4.ListRequest.get(i).getFirebasekey()
                            );
                            shareType = new ShareType(ID,dataSnapshot.getRef().toString(),
                                    MainActivity4.ListRequest.get(i).getFirebasekey(),4,
                                    MainActivity4.ListRequest.get(i).getID());
                            getPost.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DatabaseReference getLocation = firebaseDatabase.getReference("Users/"+
                                            MainActivity4.ListRequest.get(position).getID()+"/"+User+"/PublicActivity/"+MainActivity4.ListRequest.get(i).getFirebasekey()+
                                            "/Location"
                                    );
                                    post.setDetail(dataSnapshot.getValue(Post.class).getDetail());
                                    post.setDate(dataSnapshot.getValue(Post.class).getDate());
                                    post.setTime(dataSnapshot.getValue(Post.class).getTime());
                                    final DataSnapshot getPost=dataSnapshot;
                                    getLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            PositionTarget positionTarget = new PositionTarget();
                                            positionTarget.setName(dataSnapshot.getValue(PositionTarget.class).getName());
                                            positionTarget.setLatitude(dataSnapshot.getValue(PositionTarget.class).getLatitude());
                                            positionTarget.setLongitude(dataSnapshot.getValue(PositionTarget.class).getLongitude());
                                            post.setLocation(positionTarget);
                                            if(MainActivity4.ListRequest.get(i).getStatus()==-2){
                                                shareType.setStatus(8);
                                                AutoUpdate.pushToAcceptAct(shareType,User);
                                                List_Database listDatabase=new List_Database(MainActivity.convertDateToForm(post.getDate()),
                                                        MainActivity.ConvertTimeToForm(post.getTime()),post.getDetail(),post.getLocation().getName(),
                                                        post.getLocation().getLatitude(),post.getLocation().getLongitude(),8);
                                                MainActivity4.mDbAdabter_Model.createActivityList(listDatabase);
                                                MainActivity4.Refresh();
                                                shareType.setForiegnKey(MainActivity4.SearchIndex(listDatabase));
                                                if(MainActivity4.IsInShare(shareType)){
                                                    MainActivity4.SHR_Model.UpdateShare(shareType.getFirebaseKey(),shareType);
                                                }else {
                                                    MainActivity4.SHR_Model.InsertData(shareType);
                                                }
                                                MainActivity4.RefreshShareList();
                                                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                                                        MainActivity4.User_ID+"/"+MainActivity4.User+"/");
                                                databaseReference.child("PublicActivity").child(shareType.getFirebaseKey()).child("CreatorOrJoiner")
                                                        .child("BaseAccept").setValue(8);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if(MainActivity4.ListRequest.get(i).getStatus()==-1){
                            shareType.setStatus(4);
                            AutoUpdate.pushToAccept(post,shareType,User);
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                                    MainActivity4.User_ID+"/"+MainActivity4.User+"/");
                            databaseReference.child("PublicActivity").child(shareType.getFirebaseKey()).child("CreatorOrJoiner")
                                    .child("MemberList").child(String.valueOf(shareType.getOwner())).setValue(4);
                        }

                        //databaseReference.child(Data)
                        /*allrequest.clear();
                        MainActivity4.ListRequest.clear();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1, allrequest);
                        listView.setAdapter(adapter);
                        listView.invalidate();*/

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
