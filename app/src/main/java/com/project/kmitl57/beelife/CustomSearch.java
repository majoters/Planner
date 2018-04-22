package com.project.kmitl57.beelife;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.hotmildc.shareact.*;
import com.example.hotmildc.shareact.MainActivity;

import java.util.ArrayList;

import devs.mulham.raee.sample.List_Database;

public class CustomSearch extends BaseAdapter implements Filterable { //old public class CustomSearch extends BaseAdapter
    ArrayList<List_Database> DataPost;
    ArrayList<List_Database> mFilterDataPost; //new
    Context mContext;
    ValueFilter valueFilter; //new
    public CustomSearch(Context context, ArrayList<List_Database> datapost){
        this.mContext = context;
        DataPost=datapost;
        mFilterDataPost = datapost; //new
    }
    @Override
    public int getCount() {
        return DataPost.size();
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
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.activity_custom_search, parent, false);
        try {
            TextView actTime = (TextView) view.findViewById(R.id.ActTime);
            actTime.setText(com.example.hotmildc.shareact.MainActivity.ConvertTimeToString(DataPost.get(i).getTime()));
            TextView actName = (TextView) view.findViewById(R.id.ActName);
            actName.setText(DataPost.get(i).getDescription());
            TextView actDate = (TextView) view.findViewById(R.id.ActDate);
            actDate.setText(MainActivity.ConvertDateToString(DataPost.get(i).getDate()));
        }catch (IndexOutOfBoundsException e){

        }
        return view;
    }
    @Override //new
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            /*if (constraint != null && constraint.length() > 0) {
                ArrayList<List_Database> filterList = new ArrayList<List_Database>();
                for (int i = 0; i < mFilterDataPost.size(); i++) {
                    if ((mFilterDataPost.get(i).getUsername().toUpperCase())
                            .contains(constraint.toString().toUpperCase())||
                            (String.valueOf(mFilterDataPost.get(i).getId()).toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        Post post = new Post();
                        post.setTime(mFilterDataPost.get(i).getTime());
                        post.setDetail(mFilterDataPost.get(i).getDetail());
                        post.setDate(mFilterDataPost.get(i).getDate());
                        filterList.add(post);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mFilterDataPost.size();
                results.values = mFilterDataPost;
            }*/
            return results;

        }
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            DataPost = (ArrayList<List_Database>) results.values;
            notifyDataSetChanged();
        }
    }
}