package com.project.kmitl57.beelife;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import devs.mulham.raee.sample.HistoryActAdapter;
import devs.mulham.raee.sample.MainActivity4;

public class HistoryActivity extends Dialog {
    Button ok;
    ConstraintLayout nohis;
    public HistoryActivity(@NonNull final Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.hislist);
        ok = (Button) findViewById(R.id.ok);
        nohis = (ConstraintLayout) findViewById(R.id.nf);
        final ArrayList<String> acti = new ArrayList<>();
        final ArrayList<String> loca = new ArrayList<>();
        ArrayList<DataAnalysis> data = new ArrayList<>();
        data= MainActivity4.mDbDataForAnalysis_Model.GetAll();
        for(int i = 0;i<data.size();i++){
            acti.add(0,"Activity : "+data.get(i).getDescription());
            loca.add(0,"KMITL"+data.get(i).getLocationName());
        }
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.Adapter mAdapter = new HistoryActAdapter(acti, loca);
        mRecyclerView.setAdapter(mAdapter);

        if (data.size()<=0){
            nohis.setVisibility(View.VISIBLE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                }, 500);

            }
        });

    }

}
