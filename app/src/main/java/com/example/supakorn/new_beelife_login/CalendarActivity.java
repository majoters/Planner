package com.project.kmitl57.beelife;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.ptrstovka.calendarview2.CalendarDay;
import com.ptrstovka.calendarview2.CalendarView2;
import com.ptrstovka.calendarview2.OnDateSelectedListener;

import java.util.Date;

import devs.mulham.raee.sample.MainActivity4;

public class CalendarActivity extends AppCompatActivity {
    public static Date date1 = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton submit = (FloatingActionButton) findViewById(R.id.Submit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarActivity.this, MainActivity4.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarActivity.this, MainActivity4.class));
            }
        });
        final CalendarView2 calendarView = findViewById(R.id.calendar);
        final TextView testDate = findViewById(R.id.test);

        /*calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                testDate.setText(String.valueOf(dayOfMonth)+" "+String.valueOf(month+1)+" "+String.valueOf(year));
                MainActivity4.date=new Date(year-1900,month,dayOfMonth);
            }
        });*/
        calendarView.setSelectedDate(date1);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull CalendarView2 widget, @NonNull CalendarDay date, boolean selected) {

                testDate.setText(String.valueOf(date.getDay()) + " " + String.valueOf(date.getMonth() + 1) + " " + String.valueOf(date.getYear()));
                MainActivity4.date = new Date(date.getYear() - 1900, date.getMonth(), date.getDay());
            }
        });
    }

}