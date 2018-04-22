package devs.mulham.raee.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;
import com.example.hotmildc.shareact.PositionTarget;
import com.example.hotmildc.shareact.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jameswich on 19/2/2561.
 */

public class NewFeed extends Service{
    public FirebaseDatabase firebaseDatabase;
    String Today;
    Date date = new Date();
    ArrayList<DataSnapshot> data=new ArrayList<>();
    public String username;
    public int user_ID;
    public static int public_index=0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseDatabase=FirebaseDatabase.getInstance();
        Today= MainActivity.ConvertDateToString(date.getDate()*10000+(date.getMonth()+1)*100+date.getYear()%100);
        //Today="10-02-2018";

        for(int i=0;i<SocialPlan.memberList.size();i++){
            if(i<SocialPlan.memberName.size()){
                int id=SocialPlan.memberList.get(i);
                String username=SocialPlan.memberName.get(i);
                getPublicActivity(id,username);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }



    public void getPublicActivity(int ID, final String User){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                ID+"/"+User+"/PublicActivity");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //String base=ds.getValue(Post.class).getDate();
                    String username=User;
                    int baseAccept=ds.child("CreatorOrJoiner").getValue(CreatorOrJoiner.class).getBaseAccept();
                    if(baseAccept==1||baseAccept==2||baseAccept==3||baseAccept==4){
                        String date=ds.getValue(Post.class).getDate();
                        String time=ds.getValue(Post.class).getTime();
                        String detail = ds.getValue(Post.class).getDetail();
                        String locationName=ds.child("Location").getValue(PositionTarget.class).getName();
                        Double latitude =ds.child("Location").getValue(PositionTarget.class).getLatitude() ;
                        Double longitude = ds.child("Location").getValue(PositionTarget.class).getLongitude();
                        PublicActivity publicActivity = new PublicActivity(
                                SocialPlan.memberList.get(SocialPlan.getIDbyUsername(username)),
                                username,
                                ds.getKey());
                        publicActivity.setBaseAccept(baseAccept);
                        publicActivity.setPostDate(date);
                        publicActivity.setTime(time);
                        publicActivity.setDetail(detail);
                        publicActivity.setLocationName(locationName);
                        publicActivity.setLatitude(latitude);
                        publicActivity.setLongitude(longitude);

                        if(publicActivity.getBaseAccept()==2||publicActivity.getBaseAccept()==4){
                            int join=ds.child("CreatorOrJoiner").getValue(CreatorOrJoiner.class).getJoinWith();
                            publicActivity.setJoiner(join);
                        }else {
                            publicActivity.setJoiner(0);
                        }


                        if(date.compareTo(Today)==0&&!SocialPlan.isInNewFeed(publicActivity)){
                            int ref=ds.getRef().toString().indexOf("Users");
                            //Toast.makeText(getApplicationContext(),ds.getRef().toString().substring(ref),Toast.LENGTH_SHORT).show();
                            SocialPlan.isUpdate=true;
                            data.add(ds);
                            SocialPlan.publicActivities.add(0,publicActivity);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
