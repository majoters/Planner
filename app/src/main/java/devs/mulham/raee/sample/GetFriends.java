package devs.mulham.raee.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jameswich on 19/2/2561.
 */

public class GetFriends extends Service {
    FirebaseDatabase firebaseDatabase;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+
                "/ListFriend/");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    int id=Integer.parseInt(ds.getKey());
                    if(Integer.parseInt(ds.getValue().toString())==4){
                        if(SocialPlan.InMemberList(id)==null){
                            SocialPlan.memberList.add(id);
                        }
                    }
                }
                getNameMember();
                //getNameMember(MainActivity4.User_ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    public void getNameMember(){

        for(int i=0;i<SocialPlan.memberList.size();i++){

            DatabaseReference databaseReference =firebaseDatabase.getReference("Profiles/");
            final int finalI = i;
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        try {
                            int id=Integer.parseInt(ds.child("ID").getValue().toString());
                            if(id==SocialPlan.memberList.get(finalI)){
                                String User=ds.child("Username").getValue().toString();
                                int index=SocialPlan.getindexArrayByUserID(SocialPlan.memberList.get(finalI));
                                SocialPlan.memberName.add(index,User);
                            }
                        }catch (NullPointerException e){

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
