package com.example.Update_23_04_61;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by PREM on 4/22/2018.
 */

public class SuggestActAdapter extends BaseAdapter {
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> act = new ArrayList<>();
    private ArrayList<String> loc = new ArrayList<>();
    Context mContext;
    public SuggestActAdapter(Context context, ArrayList<String> Time, ArrayList<String> Activity, ArrayList<String> Location){
        this.time=Time;
        this.act=Activity;
        this.loc=Location;
        this.mContext= context;
    }
    @Override
    public int getCount() {
        return act.size();
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater mInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.suggest_activity_row, viewGroup, false);
        TextView textTime = (TextView) view.findViewById(R.id.textTime);
        TextView textActivity = (TextView) view.findViewById(R.id.textActivity);
        TextView textLocation = (TextView) view.findViewById(R.id.textLocation);
        textTime.setText(time.get(i));
        textActivity.setText(act.get(i));
        textLocation.setText(loc.get(i));
        return view;
    }
}
