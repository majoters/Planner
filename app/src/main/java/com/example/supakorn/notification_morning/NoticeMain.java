package com.example.supakorn.notification_morning;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hotmildc.shareact.MainActivity;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

import devs.mulham.raee.sample.ActivityDbAdapter;
import devs.mulham.raee.sample.CustomListView;
import devs.mulham.raee.sample.HistoryActAdapter;
import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

public class NoticeMain extends Dialog implements android.view.View.OnClickListener {

    private static final int SECOND60 = 60*1000;
    ArrayList<List_Database> databases ;
    ArrayList<String> all_time;
    ArrayList<String> all_description;
    public Date date;
    ActivityDbAdapter mDb;

    public NoticeMain(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificate);
        ListView listView = findViewById(R.id.list);
        date = new Date();
        all_time=new ArrayList<>();
        all_description=new ArrayList<>();
        databases=new ArrayList<>();
        mDb=new ActivityDbAdapter(getContext());
        mDb.open();
        String s = "";
        MainActivity4.Refresh();
        databases = mDb.fecthAllList();
        TextView today=(TextView)findViewById(R.id.date);
        int td=date.getDate()*10000+(date.getMonth()+1)*100+date.getYear()%100;
        String d = MainActivity.ConvertDateToStringM(td);
        today.setText(d);
        try {
            for(int i=0; i<databases.size(); i++){
                if(databases.get(i).getDate()==date.getDate()*10000+(date.getMonth()+1)*100+date.getYear()%100){
                    all_description.add(databases.get(i).getDescription());
                    all_time.add(MainActivity.ConvertTimeToString(databases.get(i).getTime()));
                }
            }
        }catch (NullPointerException e){
            s="Today : No Activity";
            all_description.add(s);
        }

        CustomNoti actAdapter = new CustomNoti(getContext(),all_time,all_description);
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(NoticeMain.this, android.R.layout.simple_list_item_1, android.R.id.text1, show_act);
        listView.setAdapter(actAdapter);

        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                MainActivity4.date=date;
                getContext().startActivity(new Intent(getContext(),MainActivity4.class));
                cancel();
            }
        });

    }


    @Override
    public void onClick(View v) {

    }
}
