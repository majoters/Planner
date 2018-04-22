package devs.mulham.raee.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;
import com.example.hotmildc.shareact.PositionTarget;
import com.example.hotmildc.shareact.Post;
import com.example.supakorn.notification_morning.NotificationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.hotmildc.shareact.MainActivity.ConvertTimeToForm;
import static com.example.hotmildc.shareact.MainActivity.convertDate;
import static com.example.hotmildc.shareact.MainActivity.convertDateToForm;
import static com.example.hotmildc.shareact.MainActivity.convertTime;
import static devs.mulham.raee.sample.MainActivity4.SearchIndex;
import static devs.mulham.raee.sample.MainActivity4.User_ID;
import static devs.mulham.raee.sample.MainActivity4.getforiegnkeyFromFirebasekey;

/**
 * Created by supakorn on 23/1/2561.
 */

public class AutoUpdate extends Service {

    public static ArrayList<Post> DataPost;
    public static String transmit="";
    public static FirebaseDatabase firebaseDatabase;
    public ArrayList<ShareType> DataShare;
    private NotificationHelper mNotificationHelper;
    public int index_loop;
    public List_Database listDatabase_Clone;
    public int owner;
    public int status;
    public ArrayList<List_Database> List;
    public static List_Database listDatabase;
    public static int User_ID_Memb;
    public static ShareType shareType_pub;
    public static int AddTomemberList_ID;
    public static String key;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        DataPost = new ArrayList<>();
        DataShare = new ArrayList<>();
        DataPost.clear();
        DataShare.clear();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        firebaseDatabase = FirebaseDatabase.getInstance();
        mNotificationHelper = new NotificationHelper(MainActivity4.context);
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                MainActivity4.User_ID+"/"+MainActivity4.User+"/PublicActivity/");

        transmit="On Check";
        DataPost = new ArrayList<>();
        DataShare = new ArrayList<>();
        DataPost.clear();
        DataShare.clear();
        List = MainActivity4.databases;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showDataPost(dataSnapshot,MainActivity4.User);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }


    private void showDataPost(DataSnapshot dataSnapshot, String user) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

        for(final DataSnapshot ds: dataSnapshot.getChildren()) {

            databaseReference = firebaseDatabase.getReference("Users/" +
                    MainActivity4.User_ID+"/"+user + "/PublicActivity/" + ds.getKey() + "/Location/");

            status=ds.toString().charAt(ds.toString().indexOf("BaseAccept")+11)-48;
            try {
                int index_start=ds.getValue().toString().indexOf("JoinWith")+9;
                int index_stop=ds.getValue().toString().substring(index_start).indexOf("}");
                owner=Integer.parseInt(ds.getValue().toString().substring(index_start,index_start+index_stop));
            }catch (NumberFormatException e){
                owner=0;
            }
            if(ds.toString().charAt(ds.toString().indexOf("BaseAccept")+11)-48==2||ds.toString().charAt(ds.toString().indexOf("BaseAccept")+11)-48==4||
            ds.toString().charAt(ds.toString().indexOf("BaseAccept")+11)-48==5||ds.toString().charAt(ds.toString().indexOf("BaseAccept")+11)-48==6||
                    ds.toString().charAt(ds.toString().indexOf("BaseAccept")+11)-48==9){
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            try{
                                PositionTarget positionTarget = new PositionTarget();
                                Post post = new Post();
                                post.setDetail(ds.getValue(Post.class).getDetail());
                                post.setDate(ds.getValue(Post.class).getDate());
                                post.setTime(ds.getValue(Post.class).getTime());
                                positionTarget.setName(dataSnapshot.getValue(PositionTarget.class).getName());
                                positionTarget.setLatitude(dataSnapshot.getValue(PositionTarget.class).getLatitude());
                                positionTarget.setLongitude(dataSnapshot.getValue(PositionTarget.class).getLongitude());
                                post.setLocation(positionTarget);

                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                DateFormat timeFormat = new SimpleDateFormat("hh:mm");
                                Date d = new Date();
                                ShareType shareType =new ShareType(-1,ds.getRef().toString(),ds.getKey(),
                                        status,owner);
                                if (convertDate(post.getDate()) > convertDate(String.valueOf(dateFormat.format(d)))) {
                                    putPost(post);
                                    getAllPost();
                                    if(!IsInShareAuto(shareType)){
                                        List_Database list = PostToList(post);
                                        list.setStatus(status);
                                        DataShare.add(shareType);
                                    }
                                    UpdateMyListAct();
                                } else {
                                    if (convertDate(post.getDate()) == convertDate(String.valueOf(dateFormat.format(d)))) {
                                        if (convertTime(post.getTime()) > convertTime(String.valueOf(timeFormat
                                                .format(d)))) {
                                            putPost(post);
                                            getAllPost();
                                            if(!IsInShareAuto(shareType)){
                                                List_Database list = PostToList(post);
                                                list.setStatus(status);
                                                DataShare.add(shareType);
                                            }
                                            UpdateMyListAct();
                                        }
                                    }
                                }
                            }catch (NullPointerException e){

                            }
                            //Toast.makeText(getApplicationContext(), String.valueOf(dateFormat.format(d)),
                            //        Toast.LENGTH_LONG).show();
                            //result.append(post.getDetail()+"\n");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
    }

    //get Online Activity checkin mySQlite database
    public void UpdateMyListAct(){
        ArrayList<Post> posts = getAllPost();

        for(int i=0;i<posts.size();i++){
            index_loop=i;
            List_Database listDatabase = new List_Database(convertDateToForm(posts.get(i).getDate()),
                    ConvertTimeToForm(posts.get(i).getTime()),posts.get(i).getDetail(),
                    posts.get(i).getLocation().getName(),posts.get(i).getLocation().getLatitude(),
                    posts.get(i).getLocation().getLongitude(),DataShare.get(i).getStatus());
            listDatabase_Clone=listDatabase;
            final int i_clone=i;
            if(DataShare.get(i).getStatus()==4){
                List_Database list_database = new List_Database(convertDateToForm(DataPost.get(i).getDate()),
                        ConvertTimeToForm("00.00"),"Detail",
                        "Empty",0,
                        0,-1);
                if(SearchIndex(listDatabase_Clone)==-1){
                    MainActivity4.mDbAdabter_Model.UpdateList(list_database,listDatabase_Clone);
                    MainActivity4.Refresh();
                }
                ShareType shareType = new ShareType(SearchIndex(listDatabase_Clone),
                        DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),DataShare.get(i).getStatus(),
                        DataShare.get(i).getOwner());
                if(!MainActivity4.IsInShare(shareType)&&MainActivity4.database_share.size()>0){
                    MainActivity4.SHR_Model.UpdateShare(DataShare.get(i).getFirebaseKey(),shareType);
                    MainActivity4.RefreshShareList();
                }
                if(MainActivity4.SearchIndex(list_database)!=-1){
                    MainActivity4.databases.remove(MainActivity4.getArrayIndex(list_database));
                }
                MainActivity4.listView.invalidate();
            }else{
                if(DataShare.get(i).getStatus()==5){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("Users/"+MainActivity4.User_ID+"/"+
                    MainActivity4.User+"/PublicActivity/"+DataShare.get(i).getFirebaseKey()+""+
                            "/CreatorOrJoiner/BaseAccept");
                    final DatabaseReference getIDOwner = database.getReference("Users/"+MainActivity4.User_ID+"/"+
                            MainActivity4.User+"/PublicActivity/"+DataShare.get(i).getFirebaseKey()+""+
                            "/CreatorOrJoiner/JoinWith");
                    final int[] id = new int[1];
                    final ShareType shareType = DataShare.get(i);
                    getIDOwner.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            id[0] =Integer.parseInt(dataSnapshot.getValue().toString());
                            final DatabaseReference getName = database.getReference("Users/"+id[0]);
                            getName.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String OwnerName=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                    DatabaseReference myReference = database.getReference("Users/"+id[0]+"/"+
                                            OwnerName+"/PublicActivity/"+shareType.getFirebaseKey()+"/CreatorOrJoiner/BaseAccept");
                                    myReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(Integer.parseInt(dataSnapshot.getValue().toString())==1){
                                                myRef.setValue(2);
                                                int ID_owner=MainActivity4.SHR_Model.GetMainDbIDFromFirebaseKey(DataShare.get(index_loop).getFirebaseKey());
                                                List_Database oldList=getListfromID(ID_owner);

                                                MainActivity4.mDbAdabter_Model.UpdateList(oldList,listDatabase_Clone);
                                                /*MainActivity4.listView.invalidate();
                                                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation);
                                                MainActivity4.listView.setAdapter(adapter);*/
                                                MainActivity4.Refresh();
                                                DataShare.get(index_loop).setStatus(2);

                                                //Notice It was update
                                                try {
                                                    notificate("Update Act: "+oldList.getDescription(),"" +
                                                            oldList.getDescription()+"\n"+
                                                            MainActivity.ConvertDateToString(oldList.getDate())+"\n"+
                                                            MainActivity.ConvertTimeToString(oldList.getTime())+"\n"+
                                                            oldList.getLocationName()
                                                    );
                                                }catch (NullPointerException e){
                                                    notificate("Update Activity Already ","");
                                                }

                                            }
                                            if(Integer.parseInt(dataSnapshot.getValue().toString())==3){
                                                myRef.setValue(4);
                                                int ID_owner=MainActivity4.SHR_Model.GetMainDbIDFromFirebaseKey(DataShare.get(index_loop).getFirebaseKey());
                                                List_Database oldList=getListfromID(ID_owner);
                                                MainActivity4.mDbAdabter_Model.UpdateList(oldList,listDatabase_Clone);
                                                /*MainActivity4.listView.invalidate();
                                                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation);
                                                MainActivity4.listView.setAdapter(adapter);*/
                                                MainActivity4.Refresh();
                                                DataShare.get(index_loop).setStatus(4);

                                                //Notice It was update
                                                try {

                                                    notificate("Update Act: "+oldList.getDescription(),"" +
                                                            oldList.getDescription()+"\n"+
                                                            MainActivity.ConvertDateToString(oldList.getDate())+"\n"+
                                                            MainActivity.ConvertTimeToString(oldList.getTime())+"\n"+
                                                            oldList.getLocationName());
                                                }catch (NullPointerException e){
                                                    notificate("Update Activity Already ","");
                                                }

                                            }
                                            if(Integer.parseInt(dataSnapshot.getValue().toString())==7){
                                                myRef.setValue(8);
                                                int ID_owner=MainActivity4.SHR_Model.GetMainDbIDFromFirebaseKey(DataShare.get(index_loop).getFirebaseKey());
                                                List_Database oldList=getListfromID(ID_owner);
                                                MainActivity4.mDbAdabter_Model.UpdateList(oldList,listDatabase_Clone);
                                                /*MainActivity4.listView.invalidate();
                                                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation);
                                                MainActivity4.listView.setAdapter(adapter);*/
                                                MainActivity4.Refresh();
                                                DataShare.get(index_loop).setStatus(8);

                                                //Notice It was update
                                                try {
                                                    notificate("Friends Update Act: "+oldList.getDescription(),"" +
                                                            oldList.getDescription()+"\n"+
                                                            MainActivity.ConvertDateToString(oldList.getDate())+"\n"+
                                                            MainActivity.ConvertTimeToString(oldList.getTime())+"\n"+
                                                            oldList.getLocationName());
                                                }catch (NullPointerException e){
                                                    notificate("Update Activity Already ","");
                                                }

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

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }else {
                    if(DataShare.get(i).getStatus()==6){
                        int old_status=MainActivity4.SHR_Model.GetStatusFromFirebaseKey(DataShare.get(i).getFirebaseKey());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myReference = database.getReference("Users/"+MainActivity4.User_ID+"/"+
                                MainActivity4.User+"/PublicActivity/"+DataShare.get(i).getFirebaseKey()+"/CreatorOrJoiner/BaseAccept");
                        myReference.setValue(old_status);
                        DataShare.get(index_loop).setStatus(old_status);
                        //Notice
                        int Owner = MainActivity4.SHR_Model.GetOwner(DataShare.get(i).getFirebaseKey());
                        Toast.makeText(MainActivity4.context,String.valueOf(Owner)+" is delete Activity" +
                                " from his shared ",Toast.LENGTH_SHORT).show();
                        notificate("Owner deleted Shared Activity",String.valueOf(Owner)+" is delete Activity" +
                                " from his shared ");
                    }
                    //force delete
                    if(DataShare.get(i).getStatus()==9){

                    }
                }
            }
        }

    }


    /*public void UpdateMyListAct(Post post_,ShareType shareType_){
        ArrayList<Post> posts = getAllPost();

        //for(int i=0;i<posts.size();i++){
        //index_loop=i;
        List_Database listDatabase = new List_Database(convertDateToForm(post_.getDate()),
                ConvertTimeToForm(post_.getTime()),post_.getDetail(),
                post_.getLocation().getName(),post_.getLocation().getLatitude(),
                post_.getLocation().getLongitude(),shareType_.getStatus());
        listDatabase_Clone=listDatabase;
        if(shareType_.getStatus()==4){
            List_Database list_database = new List_Database(convertDateToForm(post_.getDate()),
                    ConvertTimeToForm("00.00"),"Detail",
                    "Empty",0,
                    0,-1);
            if(SearchIndex(listDatabase_Clone)==-1){
                MainActivity4.mDbAdabter_Model.UpdateList(list_database,listDatabase_Clone);
                MainActivity4.Refresh();
            }
            ShareType shareType = new ShareType(SearchIndex(listDatabase_Clone),
                    shareType_.getRef(),shareType_.getFirebaseKey(),shareType_.getStatus(),
                    shareType_.getOwner());
            if(!MainActivity4.IsInShare(shareType)&&MainActivity4.database_share.size()>0){
                MainActivity4.SHR_Model.UpdateShare(shareType_.getFirebaseKey(),shareType);
                MainActivity4.RefreshShareList();
            }
            if(MainActivity4.SearchIndex(list_database)!=-1){
                MainActivity4.databases.remove(MainActivity4.getArrayIndex(list_database));
            }
            MainActivity4.listView.invalidate();
        }else{
            if(shareType_.getStatus()==5){
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("Users/"+MainActivity4.User_ID+"/"+
                        MainActivity4.User+"/PublicActivity/"+shareType_.getFirebaseKey()+""+
                        "/CreatorOrJoiner/BaseAccept");
                final DatabaseReference getIDOwner = database.getReference("Users/"+MainActivity4.User_ID+"/"+
                        MainActivity4.User+"/PublicActivity/"+shareType_.getFirebaseKey()+""+
                        "/CreatorOrJoiner/JoinWith");
                final int[] id = new int[1];
                final ShareType shareType = shareType_;
                getIDOwner.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id[0] =Integer.parseInt(dataSnapshot.getValue().toString());
                        final DatabaseReference getName = database.getReference("Users/"+id[0]);
                        getName.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String OwnerName=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                DatabaseReference myReference = database.getReference("Users/"+id[0]+"/"+
                                        OwnerName+"/PublicActivity/"+shareType.getFirebaseKey()+"/CreatorOrJoiner/BaseAccept");
                                myReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(Integer.parseInt(dataSnapshot.getValue().toString())==1){
                                            myRef.setValue(2);
                                            int ID_owner=MainActivity4.SHR_Model.GetMainDbIDFromFirebaseKey(DataShare.get(index_loop).getFirebaseKey());
                                            List_Database oldList=getListfromID(ID_owner);

                                            MainActivity4.mDbAdabter_Model.UpdateList(oldList,listDatabase_Clone);
                                                /*MainActivity4.listView.invalidate();
                                                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation);
                                                MainActivity4.listView.setAdapter(adapter);*/
    /*                                        MainActivity4.Refresh();
                                            shareType.setStatus(2);

                                            //Notice It was update
                                            try {
                                                notificate("Update Act: "+oldList.getDescription(),"" +
                                                        oldList.getDescription()+"\n"+
                                                        MainActivity.ConvertDateToString(oldList.getDate())+"\n"+
                                                        MainActivity.ConvertTimeToString(oldList.getTime())+"\n"+
                                                        oldList.getLocationName()
                                                );
                                            }catch (NullPointerException e){
                                                notificate("Update Activity Already ","");
                                            }

                                        }
                                        if(Integer.parseInt(dataSnapshot.getValue().toString())==3){
                                            myRef.setValue(4);
                                            int ID_owner=MainActivity4.SHR_Model.GetMainDbIDFromFirebaseKey(DataShare.get(index_loop).getFirebaseKey());
                                            List_Database oldList=getListfromID(ID_owner);
                                            MainActivity4.mDbAdabter_Model.UpdateList(oldList,listDatabase_Clone);
                                                /*MainActivity4.listView.invalidate();
                                                final CustomListView adapter = new CustomListView(getApplicationContext(),ActivityTime,ActivityName,ActivityLocation);
                                                MainActivity4.listView.setAdapter(adapter);*/
    /*                                        MainActivity4.Refresh();
                                            shareType.setStatus(4);

                                            //Notice It was update
                                            try {

                                                notificate("Update Act: "+oldList.getDescription(),"" +
                                                        oldList.getDescription()+"\n"+
                                                        MainActivity.ConvertDateToString(oldList.getDate())+"\n"+
                                                        MainActivity.ConvertTimeToString(oldList.getTime())+"\n"+
                                                        oldList.getLocationName());
                                            }catch (NullPointerException e){
                                                notificate("Update Activity Already ","");
                                            }

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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }else {
                if(shareType_.getStatus()==6){
                    int old_status=MainActivity4.SHR_Model.GetStatusFromFirebaseKey(shareType_.getFirebaseKey());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myReference = database.getReference("Users/"+MainActivity4.User_ID+"/"+
                            MainActivity4.User+"/PublicActivity/"+shareType_.getFirebaseKey()+"/CreatorOrJoiner/BaseAccept");
                    myReference.setValue(old_status);
                    shareType_.setStatus(old_status);
                    //Notice
                    int Owner = MainActivity4.SHR_Model.GetOwner(shareType_.getFirebaseKey());
                    Toast.makeText(MainActivity4.context,String.valueOf(Owner)+" is delete Activity" +
                            " from his shared ",Toast.LENGTH_SHORT).show();
                    notificate("Owner deleted Shared Activity",String.valueOf(Owner)+" is delete Activity" +
                            " from his shared ");
                }
            }
        }
        //}

    }*/


    private void notificate(String title,String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title,message);
        mNotificationHelper.getManager().notify(0,nb.build());
    }

    public boolean checkInAllList(Post post){
        ArrayList<List_Database> listDatabases = MainActivity4.databases;

        for(int i=0;i<listDatabases.size();i++){
            if(Integer.parseInt(post.getDate())==listDatabases.get(i).getDate()){
                Toast.makeText(MainActivity4.context,"Post In Array",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public ArrayList<List_Database> filterDate(){
        Date date = new Date();
        int date_int=date.getDate()*10000+date.getMonth()*100+date.getYear()%100;
        ArrayList<List_Database> lists = MainActivity4.mDbAdabter_Model.fecthAllList();
        ArrayList<List_Database> filter_lists = new ArrayList<>();
        for(int i=0;i<lists.size();i++){
            if(lists.get(i).getDate()>=date_int){
                filter_lists.add(lists.get(i));
            }
        }
        return filter_lists;
    }

    public void putPost(Post post){

        if(!IsInPost(post)){
            Post post1 = new Post();
            PositionTarget positionTarget = new PositionTarget();
            post1.setDetail(post.getDetail());
            post1.setDate(post.getDate());
            post1.setTime(post.getTime());
            positionTarget.setName(post.getLocation().getName());
            positionTarget.setLatitude(post.getLocation().getLatitude());
            positionTarget.setLongitude(post.getLocation().getLongitude());
            post1.setLocation(positionTarget);
            DataPost.add(post1);
        }
    }

    public ArrayList<Post> getAllPost() {
        return DataPost;
    }

    //when you send public activity
    public static void pushToFire(Post post,ShareType shareType){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        String key = null;
        if(shareType.getFirebaseKey()==null){
            key=myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").push().getKey();
        }
        else{
            key=shareType.getFirebaseKey();
        }
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Detail").setValue(post.getDetail());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Date").setValue(post.getDate());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Time").setValue(post.getTime());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("name").setValue(post.getLocation().getName());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(post.getLocation().getLatitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(post.getLocation().getLongitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(shareType.getStatus());
        shareType.setRef(myRef.child(MainActivity4.User).child("PublicActivity").getRef()+"/"+key);
        shareType.setFirebaseKey(key);
        shareType.setStatus(shareType.getStatus());
        MainActivity4.SHR_Model.InsertData(shareType);
    }

    public List_Database getListfromID(int id){
        for(int i=0;i<MainActivity4.databases.size();i++){
            if(MainActivity4.databases.get(i).getID()==id){
                return MainActivity4.databases.get(i);
            }
        }
        return null;
    }

    //when you push private fire to online
    public static void pushToFirePrivate(Post post,ShareType shareType){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        String key = myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").push().getKey();
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Detail").setValue(post.getDetail());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Date").setValue(post.getDate());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Time").setValue(post.getTime());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("name").setValue(post.getLocation().getName());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(post.getLocation().getLatitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(post.getLocation().getLongitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(shareType.getStatus());
        shareType.setRef(myRef.child(MainActivity4.User).child("PublicActivity").getRef()+key);
        shareType.setFirebaseKey(key);
        shareType.setStatus(shareType.getStatus());
        MainActivity4.SHR_Model.InsertData(shareType);
    }

    //when owner press accept will push to who's requested
    public static void pushToAccept(Post post,ShareType shareType,String User){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        String key = shareType.getFirebaseKey();
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("Detail").setValue(post.getDetail());
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("Date").setValue(post.getDate());
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("Time").setValue(post.getTime());
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("Location").child("name").setValue(post.getLocation().getName());
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(post.getLocation().getLatitude());
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(post.getLocation().getLongitude());
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(User_ID);
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(4);
        //Notice owner accept
    }

    public static void pushToAcceptAct(ShareType shareType,String User){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        String key = shareType.getFirebaseKey();
        myRef.child(String.valueOf(shareType.getOwner())).child(User).child("PublicActivity").child(key)
                .child("CreatorOrJoiner").child("MemberList").child(String.valueOf(MainActivity4.User_ID)).setValue(8);
        //Notice owner accept
    }

    //Push When you send activity to your friend
    public static void sendActivity(List_Database listDatabase_, int User_ID){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        listDatabase = listDatabase_;
        User_ID_Memb=User_ID;
        int id=MainActivity4.SearchIndex(listDatabase_);
        shareType_pub = new ShareType(id,"","",7,MainActivity4.User_ID);
        DatabaseReference getName = database.getReference("Users/"+User_ID);
        getName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                if(User.compareTo("ListFriend")==0){
                    //{ListFriend={1547=4, 1549=4}, Sampling={PublicActivity={-L4vZ_2JPqOn-_am40WE={Time=23.00, CreatorOrJoiner={MemberList={IDMember=0, 1549=1}, JoinWith=0, BaseAccept=1}, Date=10-02-2018, Detail=Appointment, Location={latitude=13.729828834533691, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7298288,hasLongitude=true,longitude=100.7793595,phone=null,url=null,extras=null], longitude=100.77935791015625}}, -L53tzAXnElyEol5AYAt={Time=19.48, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=project, Location={latitude=13.729828834533691, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7298288,hasLongitude=true,longitude=100.7793595,phone=null,url=null,extras=null], longitude=100.77935791015625}}, -L59kZ4Jr6ddQq5z2sKR={Time=19.10, CreatorOrJoiner={MemberList={IDMember=0}, JoinWith=0, BaseAccept=1}, Date=14-02-2018, Detail=test4, Location={latitude=13.729212760925293, name=Address[addressLines=[0:"ตึก ECC แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=ตึก ECC,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=null,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.729212799999999,hasLongitude=true,longitude=100.7755694,phone=null,url=null,extras=null], longitude=100.77556610107422}}, -L53tYUF0WI1psrft0tK={CreatorOrJoiner={BaseAccept=6}}, -L53tTJjdYvfMTD2Sc8w={Time=19.43, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=dinner, Location={latitude=13.732502937316895, name=Address[addressLines=[0:"กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.732502499999999,hasLongitude=true,longitude=100.78143039999999,phone=null,url=null,extras=null], longitude=100.78143310546875}}, -L53tVCzSqR4IIeucYVh={Time=19.43, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=dinner, Location={latitude=13.732502937316895, name=Address[addressLines=[0:"กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.732502499999999,hasLongitude=true,longitude=100.78143039999999,phone=null,url=null,extras=null], longitude=100.78143310546875}}, -L53tVzEi40CZ9S8NttF={Time=19.43, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=dinner, Location={latitude=13.732502937316895, name=Address[addressLines=[0:"กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.732502499999999,hasLongitude=true,longitude=100.78143039999999,phone=null,url=null,extras=null], longitude=100.78143310546875}}, -L53vJF-RNUPKfBrPpZ2={Time=19.48, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=project, Location={latitude=13.729828834533691, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7298288,hasLongitude=true,longitude=100.7793595,phone=null,url=null,extras=null], longitude=100.77935791015625}}, -L53uz8RRpqZv6GE3hVr={Time=19.48, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=project, Location={latitude=13.729828834533691, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7298288,hasLongitude=true,longitude=100.7793595,phone=null,url=null,extras=null], longitude=100.77935791015625}}, -L53tbmGLXLMBez3H8ro={Time=19.43, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=dinner, Location={latitude=13.732502937316895, name=Address[addressLines=[0:"กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.732502499999999,hasLongitude=true,longitude=100.78143039999999,phone=null,url=null,extras=null], longitude=100.78143310546875}}, -L53tSXhc0u4XMVAGBK2={Time=19.43, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=dinner, Location={latitude=13.732502937316895, name=Address[addressLines=[0:"กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=กรุงเทพ-ชลบุรี สายใหม่ ถนนหน้าบ้าน,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.732502499999999,hasLongitude=true,longitude=100.78143039999999,phone=null,url=null,extras=null], longitude=100.78143310546875}}, -L53wsuiRMfuWx0ZkUQw={Time=19.59, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=กินข้าว, Location={latitude=13.730995178222656, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7309954,hasLongitude=true,longitude=100.7811808,phone=null,url=null,extras=null], longitude=100.78118133544922}}, -L54NN6cSBXc1_mREw1_={Time=20.40, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=กินข้าว, Location={latitude=13.730995178222656, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7309954,hasLongitude=true,longitude=100.7811808,phone=null,url=null,extras=null], longitude=100.78118133544922}}, -L54NdSlSkjctks-UP6k={Time=20.40, CreatorOrJoiner={MemberList={IDMember=0, 1548=-2}, JoinWith=0, BaseAccept=7}, Date=11-02-2018, Detail=กินข้าว, Location={latitude=13.730995178222656, name=Address[addressLines=[0:"3 ถนน ฉลองกรุง แขวง ลำปลาทิว เขต ลาดกระบัง กรุงเทพมหานคร 10520 ประเทศไทย"],feature=3,admin=กรุงเทพมหานคร,sub-admin=null,locality=กรุงเทพมหานคร,thoroughfare=ถนน ฉลองกรุง,postalCode=10520,countryCode=TH,countryName=ประเทศไทย,hasLatitude=true,latitude=13.7309954,hasLongitude=true,longitude=100.7811808,phone=null,url=null,extras=null], longitude=100.78118133544922}}}, NumberOfActivities={16-02-2018={23:48=0, 20:32=0}, 17-02-2018={21:11=0}}}}
                    int index=dataSnapshot.getValue().toString().indexOf("}")+3;
                    int index_stop=dataSnapshot.getValue().toString().indexOf("=",index);
                    User=dataSnapshot.getValue().toString().substring(index,index_stop);
                }
                DatabaseReference myRef = database.getReference("Users");
                String key = myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").push().getKey();
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("Detail").setValue(listDatabase.getDescription());
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("Date").setValue(MainActivity.ConvertDateToString(listDatabase.getDate()));
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("Time").setValue(MainActivity.ConvertTimeToString(listDatabase.getTime()));
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("Location").child("name").setValue(listDatabase.getLocationName());
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(listDatabase.getLatitude());
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(listDatabase.getLongitude());
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(MainActivity4.User_ID);
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);
                myRef.child(String.valueOf(User_ID_Memb)).child(User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(-2);
                shareType_pub.setFirebaseKey(key);
                shareType_pub.setRef(dataSnapshot.getRef().toString());
                /*
                if(MainActivity4.IsFirebaseKeyInAllShare(key)){
                    MainActivity4.SHR_Model.UpdateShare(key,shareType1);
                }else {
                    MainActivity4.SHR_Model.InsertData(shareType1);
                }*/
                pushToFire(ListToPost(listDatabase),shareType_pub);
                AddActMemberToList(shareType_pub,User_ID_Memb);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    //When you clone private activity
    public static void PushEmpty(String key,ShareType shareType,String date){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Detail").setValue("Detail");
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Date").setValue(date);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Time").setValue("00.00");
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("name").setValue("Empty");
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(shareType.getOwner());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(-1);
        shareType.setStatus(-1);
        MainActivity4.SHR_Model.InsertData(shareType);
        AddToMemberListPermiss(shareType);
    }

    //when you update activity or edit then will push your update to online data
    public static void PushUpdate(List_Database listDatabase){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        int id=MainActivity4.SearchIndex(listDatabase);
        String key=MainActivity4.SHR_Model.GetKeyFromMainDbID(id);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Detail").setValue(listDatabase.getDescription());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Date").setValue(MainActivity.ConvertDateToString(listDatabase.getDate()));
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Time").setValue(MainActivity.ConvertTimeToString(listDatabase.getTime()));
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("name").setValue(listDatabase.getLocationName());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(listDatabase.getLatitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(listDatabase.getLongitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(0);
        PushUpdateMemberList(listDatabase);
    }

    //push update to your MemberList
    public static void PushUpdateMemberList(List_Database listDatabase_){
        final List_Database listDatabase=listDatabase_;
        int id = MainActivity4.SearchIndex(listDatabase);
        final String key=MainActivity4.SHR_Model.GetKeyFromMainDbID(id);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+MainActivity4.User_ID+"/"+
        MainActivity4.User+"/"+"PublicActivity/"+key+"/CreatorOrJoiner/MemberList");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(ds.getKey().toString().compareTo("IDMember")!=0){
                        final int ID_member=Integer.parseInt(ds.getKey());
                        final String[] Username_member = new String[1];
                        final FirebaseDatabase databaseMember = FirebaseDatabase.getInstance();
                        final DatabaseReference MemberRef = databaseMember.getReference("Users/"+ID_member);
                    MemberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Username_member[0] =dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                            DatabaseReference MemberRef_=database.getReference("Users");
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("Detail").setValue(listDatabase.getDescription());
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("Date").setValue(MainActivity.ConvertDateToString(listDatabase.getDate()));
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("Time").setValue(MainActivity.ConvertTimeToString(listDatabase.getTime()));
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("Location").child("name").setValue(listDatabase.getLocationName());
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("Location").child("latitude").setValue(listDatabase.getLatitude());
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("Location").child("longitude").setValue(listDatabase.getLongitude());
                            MemberRef_.child(String.valueOf(ID_member)).child(Username_member[0]).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(5);

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
    }


    //when you clone public activity
    public static void pullFromFire(Post post,ShareType shareType,String key){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Detail").setValue(post.getDetail());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Date").setValue(post.getDate());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Time").setValue(post.getTime());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("name").setValue(post.getLocation().getName());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("latitude").setValue(post.getLocation().getLatitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("Location").child("longitude").setValue(post.getLocation().getLongitude());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("JoinWith").setValue(shareType.getOwner());
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("MemberList").child("IDMember").setValue(0);
        myRef.child(String.valueOf(MainActivity4.User_ID)).child(MainActivity4.User).child("PublicActivity").child(key).child("CreatorOrJoiner").child("BaseAccept").setValue(2);
        shareType.setRef(myRef.child(MainActivity4.User).child("PublicActivity").getRef()+key);
        shareType.setFirebaseKey(key);
        shareType.setStatus(2);
        MainActivity4.SHR_Model.InsertData(shareType);
    }



    public static void AddToMemberList(ShareType shareType){
        key=shareType.getFirebaseKey();
        //MainActivity.getUser(shareType.getOwner());
        AddTomemberList_ID=shareType.getOwner();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Profiles");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(Integer.parseInt(ds.child("ID").getValue().toString())==AddTomemberList_ID){
                        String Username = ds.child("Username").getValue().toString();

                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                                AddTomemberList_ID+"/"+MainActivity.getUserName()+"/PublicActivity/"+key+"/CreatorOrJoiner/MemberList");

                        databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(1);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public static void AddToMemberListPermiss(ShareType shareType){
        shareType_pub=shareType;
        DatabaseReference getUser = firebaseDatabase.getReference("Users/"+
                shareType.getOwner());

        getUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String User=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                            shareType_pub.getOwner()+"/"+User+"/PublicActivity/"+shareType_pub.getFirebaseKey()+"/CreatorOrJoiner/MemberList");

                    databaseReference.child(String.valueOf(MainActivity4.User_ID)).setValue(-1);
                }catch (NullPointerException e){
                    Toast.makeText(MainActivity4.context,"NullPoint",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void AddActMemberToList(ShareType shareType,int ID){
        shareType_pub=shareType;
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+
                MainActivity4.User_ID+"/"+MainActivity4.User+"/PublicActivity/"+shareType_pub.getFirebaseKey()+"/CreatorOrJoiner/MemberList");

        databaseReference.child(String.valueOf(ID)).setValue(-2);
    }

    public static Post ListToPost(List_Database listDatabase){
        Post post = new Post();
        post.setDetail(listDatabase.getDescription());
        post.setDate(MainActivity.ConvertDateToString(listDatabase.getDate()));
        post.setTime(MainActivity.ConvertTimeToString(listDatabase.getTime()));
        PositionTarget positionTarget = new PositionTarget();
        positionTarget.setName(listDatabase.getLocationName());
        positionTarget.setLatitude(listDatabase.getLatitude());
        positionTarget.setLongitude(listDatabase.getLongitude());
        post.setLocation(positionTarget);
        return post;
    }

    public static List_Database PostToList(Post post){
        List_Database list = new List_Database(
                MainActivity.convertDateToForm(post.getDate()),
                MainActivity.ConvertTimeToForm(post.getTime()),
                post.getDetail(),
                post.getLocation().getName(),
                post.getLocation().getLatitude(),
                post.getLocation().getLongitude(),
                0
        );

        return list;
    }


    public static void deleteOnlineAct(String key_){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String key = key_;
        DatabaseReference myRef = database.getReference("Users/"+MainActivity4.User_ID+"/"+
        MainActivity4.User+"/PublicActivity/"+key+"/CreatorOrJoiner/MemberList");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().toString().compareTo("IDMember")!=0){
                        final int ID=Integer.parseInt(ds.getKey());
                        FirebaseDatabase databaseMember = FirebaseDatabase.getInstance();
                        DatabaseReference myRef_Member=databaseMember.getReference("Users/"+ID);
                        myRef_Member.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String UserName=dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("="));
                                FirebaseDatabase databaseMember = FirebaseDatabase.getInstance();
                                DatabaseReference myRef_Member_Act = databaseMember.getReference("Users/"+ID+"/"
                                        +UserName+"/PublicActivity/"+key+"/CreatorOrJoiner/BaseAccept");
                                myRef_Member_Act.setValue(6);
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
    }

    public boolean IsInPost(Post post){
        for(int i=0;i<DataPost.size();i++){
            Post postTest = DataPost.get(i);
            if(post.getDate()==postTest.getDate()&&
                    post.getTime()==postTest.getTime()&&
                    post.getDetail().compareTo(postTest.getDetail())==0){
                return true;
            }
        }
        return false;
    }

    public boolean IsInShareAuto(ShareType shareType){
        try{
            for(int i=0;i<DataShare.size();i++){
                if(DataShare.get(i).getFirebaseKey().compareTo(shareType.getFirebaseKey())==0&&
                        DataShare.get(i).getStatus()==shareType.getStatus()){
                    return true;
                }
            }
            return false;
        }catch (NullPointerException e){
            return false;
        }
    }

    /*public void AutoDelete(List_Database listDatabase){
        if(SearchIndex(listDatabase)!=-1){

        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataShare.clear();
        DataPost.clear();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
