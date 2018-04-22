package devs.mulham.raee.sample;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.ProfileFriend;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.ViewHolder>{
    public ArrayList<String> friend;
    public String btn = "FRIEND";
    Context mCx;
    FirebaseDatabase firebaseDatabase;
    int mode;
    public FindFriendAdapter(Context context, ArrayList<String> FriendName) {
        this.friend = FriendName;
        this.mCx=context;
        this.mode=0;
    }

    public FindFriendAdapter(Context context, ArrayList<String> FriendName,int mode) {
        this.friend = FriendName;
        this.mCx=context;
        this.mode=mode;
    }

    @Override
    public FindFriendAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FindFriendAdapter.ViewHolder holder, final int position) {


        holder.textFriend.setText(friend.get(position));
        holder.btnFriend.setVisibility(View.INVISIBLE);
        int space = friend.get(position).indexOf(" ");
        String name = friend.get(position).substring(0,space).toLowerCase();
        for(int i=0;i<MainActivity4.friendList.size();i++){
            if(name.toLowerCase().compareTo(MainActivity4.friendList.get(i).getUsername().toLowerCase())==0){
                holder.btnFriend.setText(btn);
                holder.btnFriend.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return friend.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textFriend;
        public Button btnFriend;
        public ViewHolder(final View itemView) {
            super(itemView);
            textFriend = (TextView) itemView.findViewById(R.id.textFriend);
            btnFriend = (Button) itemView.findViewById(R.id.btnFriend);
            firebaseDatabase=FirebaseDatabase.getInstance();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getPosition();
                    if (mode == 0) {
                        if(friend.get(position).compareTo("Not Found")!=0){
                            int space=friend.get(position).toString().indexOf(" ");
                            final String user=friend.get(position).substring(0,space);
                            final String ID=friend.get(position).substring(space+1);

                            DatabaseReference getStatus = firebaseDatabase.getReference("Users/"+MainActivity4.User_ID+
                            "/ListFriend/");
                            getStatus.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean found=false;

                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        String id=ds.getKey().toString();
                                        int status=Integer.parseInt(ds.getValue().toString());
                                        if(id.compareTo(ID)==0){
                                            if(status!=4){
                                                DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+ID+"/ListFriend/");
                                                databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(-1);
                                                Toast.makeText(mCx,user+" "+ID,Toast.LENGTH_SHORT).show();
                                            }
                                            found=true;
                                            break;
                                        }else{
                                            Toast.makeText(mCx,user+" "+ID,Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    if(!found){
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+ID+"/ListFriend/");
                                        databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(-1);
                                        Toast.makeText(mCx,user+" "+ID,Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                    if(mode==1){
                        mCx.startActivity(new Intent(mCx, ProfileFriend.class));
                        int space=friend.get(position).toString().indexOf(" ");
                        final String user=friend.get(position).substring(0,space);
                        final String ID=friend.get(position).substring(space+1);
                        ProfileFriend.friendID=Integer.parseInt(ID);
                        ProfileFriend.username=user;
                    }
                }
            });
        }
    }
}
