package com.project.kmitl57.beelife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import devs.mulham.raee.sample.MapsActivity;

/**
 * Created by jameswich on 16/2/2561.
 */

public class CustombottombarMenu extends BaseAdapter{
    ArrayList<String> act = new ArrayList<>();
    Context mContext;
    public int index;
    public CustombottombarMenu(Context context, ArrayList<String> Activity){
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

        index=position;
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.activity_custom_bottombar, parent, false);
        try {
            TextView textActName = (TextView) view.findViewById(R.id.textActName);
            textActName.setText(act.get(position));
        }catch (IndexOutOfBoundsException e){

        }

        return view;
    }

}
