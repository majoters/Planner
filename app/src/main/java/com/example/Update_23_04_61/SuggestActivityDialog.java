package com.example.Update_23_04_61;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

public class SuggestActivityDialog extends Dialog {
    public SuggestActivityDialog(@NonNull final Context context) {
        super(context);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_activity_dialog);
        ListView suggest = (ListView) findViewById(R.id.suggest_act);
        ArrayList time = new ArrayList();
        ArrayList activity = new ArrayList();
        ArrayList location = new ArrayList();
        for(int i=0;i<10;i++){
            time.add("0"+String.valueOf(i)+":00");
            activity.add("Run"+String.valueOf(i));
            location.add("KMITL"+String.valueOf(i));
        }
        SuggestActAdapter suggestActAdapter = new SuggestActAdapter(getContext(),time,activity,location);
        suggest.setAdapter(suggestActAdapter);
    }
}
