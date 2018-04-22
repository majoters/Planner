package com.example.supakorn.notification_morning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

/**
 * Created by PREM on 1/29/2018.
 */

public class CustomNoti extends BaseAdapter{
    ArrayList<String> t = new ArrayList<>();
    ArrayList<String> act = new ArrayList<>();
    Context mContext;
    public CustomNoti(Context context, ArrayList<String> time, ArrayList<String> activity){
        this.t=time;
        this.act=activity;
        this.mContext= context;
    }
    @Override
    public int getCount() {
        return act.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.activity_custom_noti, parent, false);
        try {
            TextView textTime = (TextView) view.findViewById(R.id.Time);
            textTime.setText(t.get(position));
            TextView textActName = (TextView) view.findViewById(R.id.Activity);
            textActName.setText(act.get(position));
        }catch (IndexOutOfBoundsException e){

        }

        return view;
    }
}
