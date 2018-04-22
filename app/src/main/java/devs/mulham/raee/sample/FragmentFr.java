package devs.mulham.raee.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hotmildc.shareact.FriendsRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FragmentFr extends Fragment {
    ListView fr;
    public FirebaseDatabase firebaseDatabase;
    ArrayList<FriendRequestType> a;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_friend,container,false);

        fr = (ListView)rootView.findViewById(R.id.friend_request);
        firebaseDatabase=FirebaseDatabase.getInstance();
        a = new ArrayList<>();

        /*a.add("Orn BNK48");
        a.add("Pun BMK48");*/


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(IsChange(a)){
                    a.clear();
                    for(int i=0;i<MainActivity4.FriendsRequest.size();i++){
                        a.add(MainActivity4.FriendsRequest.get(i));
                    }

                    PopupItemFr list = new PopupItemFr(getApplicationContext());
                    fr.setAdapter(list);
                }

                if(!isRemoving())
                    handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);

        return rootView;

    }

    public boolean IsChange(ArrayList<FriendRequestType> req){
        if(MainActivity4.FriendsRequest.size()!=0){
            if(req.size()!=MainActivity4.FriendsRequest.size()){
                return true;
            }else{
                for(int i=0;i<req.size();i++){
                    if(MainActivity4.FriendsRequest.get(i).getID()!=req.get(i).getID()){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

}
