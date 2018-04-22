package devs.mulham.raee.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

public class PopupItemFr extends BaseAdapter {
    ArrayList<FriendRequestType> Freq;
    String btn_confirm="CONFIRM";
    String btn_cancel="CANCEL";
    FirebaseDatabase firebaseDatabase;
    private String User=null;
    int publicID;
    private Context mContext;
    PopupItemFr(Context context){
        this.mContext= context;
        Freq=MainActivity4.FriendsRequest;
    }

    @Override
    public int getCount() {
        return Freq.size();
    }

    @Override
    public Object getItem(final int i) {
        return null;
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        firebaseDatabase=FirebaseDatabase.getInstance();
        publicID=position;
        if(view == null)
            view = mInflater.inflate(R.layout.nt_list_fr, parent, false);
        try {
            TextView textTime = (TextView) view.findViewById(R.id.textUserName2);
            textTime.setText(Freq.get(position).getID()+" "+Freq.get(position).getName());

            Button confirm = (Button) view.findViewById(R.id.buttonConfirm);
            confirm.setText(btn_confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                            MainActivity4.FriendsRequest.get(position).getID()+"/ListFriend/");
                    databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(4);

                    databaseReference=firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+"/ListFriend/");
                    databaseReference.child(String.valueOf(MainActivity4.FriendsRequest.get(position).getID())).setValue(4);
                    MainActivity4.FriendsRequest.remove(position);
                }
            });
            Button cancel = (Button) view.findViewById(R.id.buttonCancel);
            cancel.setText(btn_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                            MainActivity4.FriendsRequest.get(position).getID()+"/ListFriend/");
                    databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(null);

                    databaseReference=firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+"/ListFriend/");
                    databaseReference.child(String.valueOf(MainActivity4.FriendsRequest.get(publicID).getID())).setValue(null);
                    MainActivity4.FriendsRequest.remove(position);
                }
            });
        }catch (IndexOutOfBoundsException e){

        }
        return view;
    }

}
