package com.project.kmitl57.beelife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotmildc.shareact.*;
import com.example.hotmildc.shareact.MainActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import devs.mulham.raee.sample.List_Database;
import devs.mulham.raee.sample.MainActivity4;

import static devs.mulham.raee.sample.MainActivity4.context;
import static devs.mulham.raee.sample.MainActivity4.listdate;

public class CompactCalendar extends AppCompatActivity {
    private int progressStatus = 0;
    TextView textmorning;
    TextView textafternoon;
    TextView textevening;
    TextView textday;
    TextView textstatus;
    TextView textEvent;
    TextView textnEvent;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    String[] week = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    TextView date;
    ImageButton next;
    ImageButton previous;
    int double_click;
    static ArrayList<Integer> stock_date;
    TextView textmonthyear;
    int dateiterate;
    Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_calendar);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textmorning=(TextView)findViewById(R.id.textMorning);
        textafternoon=(TextView)findViewById(R.id.textafternoon);
        textevening=(TextView)findViewById(R.id.textevening);
        textmonthyear=(TextView)findViewById(R.id.textMonthYear);
        textday=(TextView)findViewById(R.id.textDay);
        textstatus=(TextView)findViewById(R.id.textStatus);
        textEvent=(TextView)findViewById(R.id.textEvent);
        textnEvent=(TextView)findViewById(R.id.textn_act);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompactCalendar.this, MainActivity4.class));
            }
        });
        date = (TextView) findViewById(R.id.dateShow);
        next = (ImageButton) findViewById(R.id.ne);
        previous = (ImageButton) findViewById(R.id.pre);
        date.setText(dateFormatMonth.format(new Date()));
        stock_date=new ArrayList<>();

        compactCalendar = (CompactCalendarView) findViewById(R.id.cdv);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        //Set an event for Teachers' Professional Day 2016 which is 21st of October
        compactCalendar.setDayColumnNames(week);

        String monthyear = com.example.hotmildc.shareact.MainActivity.ConvertDateToStringM(MainActivity4.listdate);
        textmonthyear.setText(monthyear.substring(3));
        date1 =new Date();
        textday.setText(String.valueOf(date1.getDate()));
        textstatus.setText("TODAY");
        AddNumberOfActivity(MainActivity4.listdate);

        stock_date.clear();
        for(int i=0;i<MainActivity4.databases.size();i++){
            Date d = new Date();
            int date=MainActivity4.databases.get(i).getDate();
            d.setDate(date/10000);
            d.setMonth((date/100)%100-1);
            d.setYear(100+date%100);
            if(MainActivity4.databases.get(i).getTime()<1200&&!IsInArray(date)){
                compactCalendar.addEvent(new Event(Color.rgb(0,174,237),d.getTime()));
                stock_date.add(date);
            }
            else if(MainActivity4.databases.get(i).getTime()<1800&&!IsInArray(date)){
                compactCalendar.addEvent(new Event(Color.rgb(6,187,142),d.getTime()));
                stock_date.add(date);
            }else{
                if(!IsInArray(date)){
                    compactCalendar.addEvent(new Event(Color.rgb(14,49,117),d.getTime()));
                    stock_date.add(date);
                }
            }
        }

        /*
        compactCalendar.addEvent(new Event(Color.rgb(0,174,237),1520091470000L));
        compactCalendar.addEvent(new Event(Color.rgb(6,187,142),1520091470000L));
        compactCalendar.addEvent(new Event(Color.rgb(14,49,117),1520091470000L));
        */
        double_click=0;
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date dateClicked) {
                double_click+=dateClicked.getDate();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(double_click==2){
                            //*dateClicked.getDate()
                            MainActivity4.date=new Date(dateClicked.getYear(), dateClicked.getMonth(), dateClicked.getDate());
                            //Toast.makeText(getApplicationContext(),"Double Click",Toast.LENGTH_SHORT);
                            startActivity(new Intent(CompactCalendar.this, MainActivity4.class));
                        }
                        double_click=0;
                    }
                },1000);
                Context context = getApplicationContext();
                Date main = new Date(dateClicked.getYear(), dateClicked.getMonth(), dateClicked.getDate());
                date.setText(dateFormatMonth.format(dateClicked));
                int alldate=dateClicked.getDate()*10000+(dateClicked.getMonth()+1)*100+dateClicked.getYear()%100;
                String monthyear = MainActivity.ConvertDateToStringM(alldate).substring(3);
                textmonthyear.setText(monthyear);
                textday.setText(String.valueOf(main.getDate()));
                if(alldate==date1.getDate()*10000+(date1.getMonth()+1)*100+date1.getYear()%100){
                    textstatus.setText("TODAY");
                }else{
                    textstatus.setText(" ");
                }
                AddNumberOfActivity(alldate);


                /*Toast.makeText(context, String.valueOf(dateClicked.getDate()+"-"
                        +String.valueOf((dateClicked.getMonth()))+"-"
                        +String.valueOf((dateClicked.getYear()))), Toast.LENGTH_SHORT).show();
                /*if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                }*/

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                date.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showNextMonth();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {compactCalendar.showPreviousMonth();
            }
        });

    }

    public boolean IsInArray(int date){
        for(int i=0;i<stock_date.size();i++){
            if(date==stock_date.get(i)){
                return true;
            }
        }
        return false;
    }

    public void AddNumberOfActivity(int date){
        int morning=0;
        int afternoon=0;
        int evening=0;
        int total=0;
        for(int i=0;i<MainActivity4.databases.size();i++){
            if(MainActivity4.databases.get(i).getDate()==date){
                if(MainActivity4.databases.get(i).getTime()<1200){
                    morning++;
                }else if(MainActivity4.databases.get(i).getTime()<1800){
                    afternoon++;
                }else{
                    evening++;
                }
                total++;
            }
        }
        //textEvent.setText("YOU HAVE ");
        textnEvent.setText(String.valueOf(total)+" EVENTS");
        progressStatus = 80;
        final ProgressBar pbMorning = (ProgressBar) findViewById(R.id.pbMorning);
        final ProgressBar pbAfternoon = (ProgressBar) findViewById(R.id.pbAfternoon);
        final ProgressBar pbEvening = (ProgressBar) findViewById(R.id.pbEvening);
        pbMorning.setMax(total);
        pbAfternoon.setMax(total);
        pbEvening.setMax(total);
        pbMorning.setProgress(morning);
        //textmorning.setText(String.valueOf(progressStatus));
        //pbMorning.setMax();
        pbAfternoon.setProgress(afternoon);
        pbEvening.setProgress(evening);
        textmorning.setText(String.valueOf(morning));
        textafternoon.setText(String.valueOf(afternoon));
        textevening.setText(String.valueOf(evening));
    }
}