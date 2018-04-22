package devs.mulham.raee.sample;


import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

public class HistoryActAdapter extends RecyclerView.Adapter<HistoryActAdapter.ViewHolder> {
    public ArrayList<String> act;
    public ArrayList<String> loc;
    public HistoryActAdapter(final ArrayList<String> Activity,final ArrayList<String> Location) {
        this.act = Activity;
        this.loc = Location;
    }

    @Override
    public HistoryActAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final HistoryActAdapter.ViewHolder holder, final int position) {
        holder.textAct.setText(act.get(position));
        holder.textLoc.setText(loc.get(position));
    }

    @Override
    public int getItemCount() {
        return act.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textAct;
        public TextView textLoc;
        public ViewHolder(final View itemView) {
            super(itemView);
            textAct = (TextView) itemView.findViewById(R.id.textActName);
            textLoc = (TextView) itemView.findViewById(R.id.textLocation);
        }
    }
}
