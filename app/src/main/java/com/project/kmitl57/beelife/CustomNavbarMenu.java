package com.project.kmitl57.beelife;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomNavbarMenu extends BaseAdapter {
    ArrayList<DataAnalysis> act = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    Context mContext;
    public CustomNavbarMenu(Context context, ArrayList<DataAnalysis> Activity){
        this.act=Activity;
        this.mContext= context;
    }
    @Override
    public int getCount() {
        try {
            return act.size();
        }catch (NullPointerException e){
            return 0;
        }
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
            view = mInflater.inflate(R.layout.activity_custom_navbar_menu, parent, false);

        try {
            int time = act.get(position).getTimeAct();
            int hours = time/100;
            int minutes = time-(hours*100);
            TextView textTimeName = (TextView) view.findViewById(R.id.textTime); //act.get(position).getTimeAct()
            textTimeName.setText(String.format("%02d:%02d",hours,minutes));
            TextView textActName = (TextView) view.findViewById(R.id.textActName);
            textActName.setText(act.get(position).getDescription());
            TextView textlocationName = (TextView)view.findViewById(R.id.textLocation);
            textlocationName.setText(act.get(position).getLocationName());
            Log.d("textTime", String.valueOf(act.get(position).getTimeAct()));
        }catch (IndexOutOfBoundsException e){

        }

        return view;
    }
}
