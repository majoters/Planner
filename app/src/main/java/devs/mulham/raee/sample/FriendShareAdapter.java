package devs.mulham.raee.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;


public class FriendShareAdapter extends BaseAdapter{
    ArrayList<FriendListType> friend;
    ArrayList<FriendListType> checkfriend;
    private Context mContext;
    CheckBox checkBox;
    int complete;
    FriendShareAdapter(Context context, ArrayList<FriendListType> Friend_Name){
        this.friend=Friend_Name;
        this.mContext= context;
        this.complete=0;
    }

    @Override
    public int getCount() {
        return friend.size();
    }

    @Override
    public Object getItem(final int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = mInflater.inflate(R.layout.friendlist_row, viewGroup, false);
        TextView textFriend = (TextView) view.findViewById(R.id.textFriend);
        textFriend.setText(friend.get(i).getUsername());
        checkBox = (CheckBox) view.findViewById(R.id.chk_select);
        checkBox.setChecked(friend.get(i).isCheck());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                friend.get(i).setCheck(isChecked);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("On Click",friend.get(i).getUsername());
                Toast.makeText(mContext,String.valueOf(i),Toast.LENGTH_SHORT).show();
                //checkBox.setChecked(!checkBox.isChecked());
                friend.get(i).setCheck(!friend.get(i).isCheck());
                Log.w(friend.get(i).getUsername(),String.valueOf(friend.get(i).isCheck()));
            }
        });

        return view;
    }
}
