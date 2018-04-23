package devs.mulham.raee.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Update_23_04_61.SocailPlanDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

class SocialPlanAdapter extends RecyclerView.Adapter<SocialPlanAdapter.ViewHolder> {
    ArrayList<String> UserName;
    ArrayList<String> Activity;
    ArrayList<String> Location;
    ArrayList<String> Time;
    public static Context mCx;
    FirebaseDatabase firebaseDatabase;
    public static boolean checkPress=false;

    public SocialPlanAdapter(Context context,final ArrayList<String> userName,
                             final ArrayList<String> activity,
                             final ArrayList<String> location,
                             final ArrayList<String> time) {
        mCx=context;
        this.UserName = userName;
        this.Activity = activity;
        this.Location = location;
        this.Time = time;
        firebaseDatabase=FirebaseDatabase.getInstance();
    }



    @Override
    public SocialPlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_feed_row, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final SocialPlanAdapter.ViewHolder holder, int position) {
        try {
            holder.uN.setText(UserName.get(position));
            holder.act.setText(Activity.get(position));
            holder.loc.setText(Location.get(position));
            holder.tim.setText(Time.get(position));
        }catch (IndexOutOfBoundsException e){

        }

    }

    @Override
    public int getItemCount() {
        return UserName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uN;
        public TextView act;
        public TextView loc;
        public TextView tim;
        public ViewHolder(final View itemView) {
            super(itemView);
            uN = (TextView) itemView.findViewById(R.id.textUserName2);
            act = (TextView) itemView.findViewById(R.id.textActivity);
            loc = (TextView) itemView.findViewById(R.id.textLocation);
            tim = (TextView) itemView.findViewById(R.id.textTime);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index=getPosition();
                    SocailPlanDialog socailPlanDialog = new SocailPlanDialog(mCx,UserName.get(index),
                            Activity.get(index),Time.get(index),index);
                    socailPlanDialog.show();

                    /*try{
                        int BaseAccept=SocialPlan.publicActivities.get(index).getBaseAccept();
                        Toast.makeText(mCx,UserName.get(index),Toast.LENGTH_SHORT).show();

                        if(BaseAccept==1) {
                            String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getUser_ID())+
                                    "/"+SocialPlan.publicActivities.get(index).getUsername()+"/PublicActivity/"+
                                    SocialPlan.publicActivities.get(index).getFirebasekey()+"/";
                            MainActivity4.Clone1(ref, index);
                        }else if(BaseAccept==3){
                            String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getUser_ID())+
                                    "/"+SocialPlan.publicActivities.get(index).getUsername()+"/PublicActivity/"+
                                    SocialPlan.publicActivities.get(index).getFirebasekey()+"/";
                            MainActivity4.RequestAct3(ref,index);
                        }else if(BaseAccept==2){
                            String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getJoiner())+
                                    "/";
                            DatabaseReference db = firebaseDatabase.getReference(ref);
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                    String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(getPosition()).getJoiner())+
                                            "/"+User+"/PublicActivity/"+SocialPlan.publicActivities.get(getPosition()).getFirebasekey();
                                    MainActivity4.Clone2(ref,getPosition());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else if(BaseAccept==4){
                            checkPress=true;
                            String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(index).getJoiner())+
                                    "/";
                            DatabaseReference db = firebaseDatabase.getReference(ref);
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                    String ref="Users/"+String.valueOf(SocialPlan.publicActivities.get(getPosition()).getJoiner())+
                                            "/"+User+"/PublicActivity/"+SocialPlan.publicActivities.get(getPosition()).getFirebasekey();
                                    if(checkPress){
                                        MainActivity4.RequestAct4(ref,getPosition());
                                        checkPress=false;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }catch (IndexOutOfBoundsException e) {
                        Toast.makeText(mCx,"It's Your Activity",Toast.LENGTH_SHORT).show();
                    }*/
                }
            });
            //itemView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            //    @Override
            //    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            //        ShareDialog share = new ShareDialog();
            //    }
            //});

        }
    }
}
