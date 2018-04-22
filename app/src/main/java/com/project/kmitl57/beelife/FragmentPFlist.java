package com.project.kmitl57.beelife;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import devs.mulham.raee.sample.FindFriendAdapter;
import devs.mulham.raee.sample.SocialPlan;

public class FragmentPFlist extends Fragment {
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_pf_list,container,false);
        ArrayList<String> a = new ArrayList<>();
        final ArrayList<String> fri = new ArrayList<>();
        try {
            for(int i = 0; i<= SocialPlan.memberList.size(); i++){
                fri.add(String.valueOf(SocialPlan.memberName.get(i))+" "+SocialPlan.memberList.get(i));
            }
        }catch(IndexOutOfBoundsException e){

        }


        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.friend_list);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.Adapter mAdapter = new FindFriendAdapter(getContext(),fri,1);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;

    }
}
