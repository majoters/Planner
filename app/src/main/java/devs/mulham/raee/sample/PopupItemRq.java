package devs.mulham.raee.sample;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.hotmildc.shareact.MainActivity;
import com.example.hotmildc.shareact.PositionTarget;
import com.example.hotmildc.shareact.Post;
import com.example.hotmildc.shareact.RequestType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PopupItemRq extends BaseAdapter {
    ArrayList<String> act;
    ArrayList<String> loc;
    ArrayList<String> time;
    String btn_join="ACCEPT";
    String btn_x="X";
    int i;
    ShareType shareType=null;
    public static FirebaseDatabase firebaseDatabase;
    private Context mContext;
    TextView textUsername;
    TextView textActivity;
    TextView textTime;
    TextView textLocation;
    ArrayList<String> ListName;

    PopupItemRq(Context context){
        this.mContext= context;

    }

    @Override
    public int getCount() {
        return MainActivity4.ListRequest.size();
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

        i=position;
        firebaseDatabase=FirebaseDatabase.getInstance();
        ListName=new ArrayList<>();
        final String user=MainActivity4.ListRequest.get(i).getName();
        final String ID=String.valueOf(MainActivity4.ListRequest.get(i).getID());

        if(view == null)
            view = mInflater.inflate(R.layout.nt_list_rq, parent, false);

        textUsername = (TextView) view.findViewById(R.id.textUserName2);
        textActivity = (TextView) view.findViewById(R.id.textView10);
        textLocation = (TextView) view.findViewById(R.id.textView9);
        textUsername.setText(MainActivity4.ListRequest.get(i).getName());
        textActivity.setText(MainActivity4.ListRequest.get(i).getDeta()); //Detail
        textLocation.setText(MainActivity4.ListRequest.get(i).getDat()); //Date
        TextView status = (TextView)view.findViewById(R.id.status);
        if(MainActivity4.ListRequest.get(i).getStatus()==-1){
            status.setText("Request your activity");
        }else{
            status.setText("Send your an activity");
        }

        try {


            /*
            TextView textActivity = (TextView) view.findViewById(R.id.textView10);
            textActivity.setText(act.get(position));

            TextView textTime = (TextView) view.findViewById(R.id.textView9);
            textTime.setText(time.get(position));

            TextView textLocation = (TextView) view.findViewById(R.id.textView8);
            textLocation.setText(loc.get(position));
            */
            Button join = (Button)view.findViewById(R.id.buttonJoin);
            join.setText(btn_join);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestType req=MainActivity4.ListRequest.get(i);
                    String ref="Users/"+MainActivity4.User_ID+"/"+MainActivity4.User+
                            "/PublicActivity/"+req.getFirebasekey()+"/";
                    ShareType shareType=new ShareType(-1,ref,req.getFirebasekey(),
                            req.getStatus(),req.getID());
                    Post post=GetPostByIndexOfArray(i);
                    if(MainActivity4.ListRequest.get(i).getStatus()==-2){
                        shareType.setStatus(8);
                        AutoUpdate.pushToAcceptAct(shareType,user);
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
                    if(MainActivity4.ListRequest.get(i).getStatus()==-1){
                        shareType.setStatus(4);
                        AutoUpdate.pushToAccept(post,shareType,user);
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                                MainActivity4.User_ID+"/"+MainActivity4.User+"/");
                        databaseReference.child("PublicActivity").child(shareType.getFirebaseKey()).child("CreatorOrJoiner")
                                .child("MemberList").child(String.valueOf(shareType.getOwner())).setValue(4);
                    }
                    mContext.startService(new Intent(mContext,AutoCheckRequest.class));
                    MainActivity4.ListRequest.remove(i);
                }
            });


            Button x = (Button)view.findViewById(R.id.buttonX);
            x.setText(btn_x);
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestType req=MainActivity4.ListRequest.get(i);
                    if(req.getStatus()==-1){
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                                MainActivity4.User_ID+"/"+MainActivity4.User+"/PublicActivity/"+MainActivity4.ListRequest.get(i).getFirebasekey()+
                        "/CreatorOrJoiner/MemberList/");
                        databaseReference.child(ID).setValue(null);

                    }
                    if(req.getStatus()==-2){
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                        MainActivity4.User_ID+"/"+MainActivity4.User+"/PublicActivity/");
                        databaseReference.child(req.getFirebasekey()).setValue(null);
                        MainActivity4.ListRequest.remove(i);
                    }
                }
            });
        }catch (IndexOutOfBoundsException e){

        }
        return view;
    }

    public Post GetPostByIndexOfArray(int i){
        Post post = new Post();
        RequestType req=MainActivity4.ListRequest.get(i);
        post.setDate(req.getDat());
        post.setTime(req.getTim()); //Time
        post.setDetail(req.getDeta()); //Detail
        PositionTarget position = new PositionTarget();
        position.setName(req.getLocationName());
        position.setLatitude(req.getLatitude());
        position.setLongitude(req.getLongitude());
        post.setLocation(position);
        return post;
    }

}
