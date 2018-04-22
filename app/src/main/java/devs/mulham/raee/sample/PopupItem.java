package devs.mulham.raee.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

/**
 * Created by PREM on 2/8/2018.
 */

public class PopupItem extends BaseAdapter {
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> act = new ArrayList<>();
    ArrayList<String> loc = new ArrayList<>();
    Context mContext;
    public PopupItem(Context context, ArrayList<String> t, ArrayList<String> a, ArrayList<String> l){
        this.time=t;
        this.act=a;
        this.loc=l;
        this.mContext= context;
    }
    @Override
    public int getCount() {
        return act.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.activity_block, parent, false);
        TextView textTime = (TextView) view.findViewById(R.id.textTime);
        textTime.setText(time.get(position));
        TextView textActName = (TextView) view.findViewById(R.id.textActName);
        textActName.setText(act.get(position));
        TextView textLocation = (TextView) view.findViewById(R.id.textLocation);
        textLocation.setText(loc.get(position));

        return view;
    }
}
