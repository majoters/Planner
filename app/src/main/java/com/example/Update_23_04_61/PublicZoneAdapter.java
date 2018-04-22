package com.example.Update_23_04_61;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;
import com.example.hotmildc.shareact.Post;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

import devs.mulham.raee.sample.AutoUpdate;
import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.ShareType;


public class PublicZoneAdapter extends RecyclerView.Adapter<PublicZoneAdapter.ViewHolder> {

    ArrayList<List_Database> List;
    ArrayList<ShareType> DataShare;
    int ID;
    String Username;
    Context mContext;
    public PublicZoneAdapter(Context context, ArrayList<List_Database> list,ArrayList<ShareType> DataShare,int ID,String Username) {
        mContext = context;
        List = list;
        this.DataShare=DataShare;
        this.ID = ID;
        this.Username = Username;
    }
    @Override
    public PublicZoneAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.publiczone_row,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PublicZoneAdapter.ViewHolder holder, final int position) {
        holder.textEvent.setText(List.get(position).getDescription());
        holder.textDate.setText(MainActivity.ConvertDateToStringM(List.get(position).getDate()));
        holder.textLocation.setText(List.get(position).getLocationName());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textEvent;
        public TextView textDate;
        public TextView textLocation;
        public ViewHolder(final View itemView) {
            super(itemView);
            textEvent = (TextView) itemView.findViewById(R.id.textEvent);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textLocation = (TextView) itemView.findViewById(R.id.textLocation);

            Button join = (Button)itemView.findViewById(R.id.join);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    ListSelect(position,ID,Username);
                    Toast.makeText(mContext,"Activity Already Added",Toast.LENGTH_LONG).show();

                }
            });

        }
    }

    public void ListSelect(int i,int id,String username){
        Date date = new Date();
        List_Database list_database = new List_Database(List.get(i).getDate(),
                List.get(i).getTime(),List.get(i).getDescription(),
                List.get(i).getLocationName(),List.get(i).getLatitude(),
                List.get(i).getLongitude(),2);

        MainActivity4.mDbAdabter_Model.createActivityList(list_database);
        MainActivity4.Refresh();

        MainActivity4.mDbDataForAnalysis_Model.InsertData(date.getHours()*100+date.getMinutes(),
                list_database);

        MainActivity4.values_filter.add(list_database);

        MainActivity4.SHR_Model.InsertData(new ShareType(MainActivity4.SearchIndex(list_database),
                DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,id)); //old DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,User_ID));

        MainActivity4.RefreshShareList();
        MainActivity4.listView.invalidate();

        Post post = AutoUpdate.ListToPost(List.get(i));

        AutoUpdate.pullFromFire(post,new ShareType(MainActivity4.mDbAdabter_Model.ListToID(list_database),
                DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,id),DataShare.get(i).getFirebaseKey()); //new
        //DataShare.get(i).getRef(),DataShare.get(i).getFirebaseKey(),2,User_ID),DataShare.get(i).getFirebaseKey());
        AutoUpdate.AddToMemberList(DataShare.get(i));

        Toast.makeText(mContext,"Already Added",Toast.LENGTH_SHORT).show();

    }
}
