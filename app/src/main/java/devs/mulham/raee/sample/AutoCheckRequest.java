package devs.mulham.raee.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.hotmildc.shareact.PositionTarget;
import com.example.hotmildc.shareact.Post;
import com.example.hotmildc.shareact.RequestType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static devs.mulham.raee.sample.MainActivity4.ListRequest;
import static devs.mulham.raee.sample.MainActivity4.User;
import static devs.mulham.raee.sample.MainActivity4.User_ID;

/**
 * Created by jameswich on 30/1/2561.
 */

public class AutoCheckRequest extends Service {


    public static FirebaseDatabase firebaseDatabase;
    public RequestType requestType_;
    RequestType requestType;
    ArrayList<RequestType> typeArrayList;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        typeArrayList=new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                User_ID+"/"+User+"/"+"PublicActivity"+"/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    int BaseAccept=ds.child("CreatorOrJoiner").getValue(CreatorOrJoiner.class).getBaseAccept();
                    try {
                        String ref=ds.getRef().toString().substring(ds.getRef().toString().indexOf("Users"));
                        String key=ds.getKey().toString();
                        requestType  = new RequestType();
                        requestType.setDeta(ds.getValue(Post.class).getDetail());
                        requestType.setDat(ds.getValue(Post.class).getDate());
                        requestType.setTim(ds.getValue(Post.class).getTime());;
                        requestType.setFirebasekey(ds.getKey().toString());
                        requestType.setLocationName(ds.child("Location").getValue(PositionTarget.class).getName());
                        requestType.setLatitude(ds.child("Location").getValue(PositionTarget.class).getLatitude());
                        requestType.setLongitude(ds.child("Location").getValue(PositionTarget.class).getLongitude());
                        if(BaseAccept==-2){
                            requestType.setID(ds.child("CreatorOrJoiner").getValue(CreatorOrJoiner.class).getJoinWith());
                            if(ItUnique(requestType)){
                                requestType.setStatus(-2);
                                ListRequest.add(requestType);
                                //ShareType shareType = new ShareType(-1,ref,key,-2,Integer.parseInt(dataSnapshot.getValue().toString()));
                            }
                        }
                        else{
                            for(DataSnapshot member:ds.child("CreatorOrJoiner").child("MemberList").getChildren()){
                                if(member.getKey().compareTo("IDMember")!=0){
                                    requestType.setID(Integer.parseInt(member.getKey()));
                                }
                                if(member.getValue(Integer.class)==-1){
                                    requestType.setStatus(-1);
                                    if(ItUnique(requestType)){
                                        ListRequest.add(requestType);
                                    }
                                }
                            }
                        }

                    }catch (StringIndexOutOfBoundsException e){

                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference("Users/"+
                User_ID+"/ListFriend/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(ds.getValue().toString().compareTo("-1")==0){
                        final int id=Integer.parseInt(String.valueOf(ds.getKey()));
                        DatabaseReference databaseReference1=firebaseDatabase.getReference("Profiles/");
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    int ID=ds.getValue(getProfiles.class).getID();
                                    if(ID==id){
                                        String User=ds.getValue(getProfiles.class).getUsername();
                                        String msg=id+" "+ User;
                                        if(!Iterate(msg)){
                                            FriendRequestType fr = new FriendRequestType(id,User);
                                            MainActivity4.FriendsRequest.add(fr);
                                        }
                                    }
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
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean ItUnique(RequestType requestType){
        if(MainActivity4.ListRequest.size()==0){
            return true;
        }else {
            for(int i=0;i<MainActivity4.ListRequest.size();i++){
                if(MainActivity4.ListRequest.get(i).getFirebasekey().compareTo(requestType.getFirebasekey())==0&&
                        MainActivity4.ListRequest.get(i).getID()==requestType.getID()){
                    return false;
                }
            }
            return true;
        }
    }

    public boolean Iterate(String str){
        if(MainActivity4.FriendsRequest.size()==0){
            return false;
        }
        for(int i=0;i<MainActivity4.FriendsRequest.size();i++){
            String msg=MainActivity4.FriendsRequest.get(i).getID()+" "+MainActivity4.FriendsRequest.get(i).getName();
            if(msg.compareTo(str)==0){
                return true;
            }
        }
        return false;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
