package com.project.kmitl57.beelife;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hotmildc.shareact.MainActivity;

import java.util.ArrayList;

import devs.mulham.raee.sample.MainActivity4;

public class ChatActivity extends AppCompatActivity {
    public int d = 10100;
    public Button date;
    private BottomSheetBehavior mBottomSheetBehavior;
    float x;
    float y;
    boolean alreadyclick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ArrayList<String> item = new ArrayList<>();
        for(int i=0;i<=20;i++){
            item.add(String.valueOf(i));
        }
        View bottomSheetView = getLayoutInflater().inflate(R.layout.maps_btm_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ChatActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetListView list = (BottomSheetListView) bottomSheetView.findViewById(R.id.list);
        CustombottombarMenu test = new CustombottombarMenu(getApplicationContext(),item);
        list.setAdapter(test);
        if(event.getAction()==event.ACTION_MOVE){
            if(event.getRawY()<y){
                if(event.getRawX()<x+10&&event.getRawX()>x-10){
                    if(!alreadyclick){
                        bottomSheetDialog.show();
                        alreadyclick=true;
                    }
                    //Toast.makeText(ChatActivity.this,String.valueOf(event.getRawX()),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ChatActivity.this,String.valueOf(event.getRawY()),Toast.LENGTH_SHORT).show();
                }
            }else{
                bottomSheetDialog.cancel();
                alreadyclick=false;
            }
        }else{
            bottomSheetDialog.cancel();
            alreadyclick=false;
        }
        x=event.getRawX();
        y=event.getRawY();
        return super.onTouchEvent(event);
    }
}
