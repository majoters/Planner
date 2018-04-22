package devs.mulham.raee.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;


class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    ArrayList<FriendListType> mDataSet;

    public MainAdapter(final ArrayList<FriendListType> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, int position) {
        //holder.mTitle.setText(mDataSet.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        try {
            return mDataSet.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public ViewHolder(View itemView) {
            super(itemView);
           // mTitle = (TextView) itemView.findViewById(R.id.textUserName);
        }
    }
}
