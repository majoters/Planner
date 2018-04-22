package devs.mulham.raee.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hotmildc.shareact.RequestActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by PREM on 2/11/2018.
 */

public class FragmentRq extends Fragment {
    public static FirebaseDatabase firebaseDatabase;
    ArrayList<String> a;
    ListView fr;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_request, container, false);

        fr = (ListView)rootView.findViewById(R.id.request);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent j = new Intent(getApplicationContext(),AutoCheckRequest.class);
                getApplicationContext().startService(j);

                firebaseDatabase=FirebaseDatabase.getInstance();

                for(int i=0;i<MainActivity4.ListRequest.size();i++){
                    final int pb_key=i;
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                try {
                                    int id=ds.getValue(getProfiles.class).getID();
                                    if(MainActivity4.ListRequest.get(pb_key).getID()==id){
                                        String User=ds.getValue(getProfiles.class).getUsername();
                                        MainActivity4.ListRequest.get(pb_key).setName(User);
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                PopupItemRq list = new PopupItemRq(getApplicationContext());
                fr.setAdapter(list);
                fr.invalidate();

                if(!isRemoving())
                    handler.postDelayed(this,500);
            }
        };
        handler.post(runnable);




        return rootView;
    }
}
