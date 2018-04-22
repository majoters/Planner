package com.example.Update_23_04_61;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TimePicker;

import com.project.kmitl57.beelife.R;

public class EditActivityDialog extends Dialog {

    public EditActivityDialog(@NonNull final Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dialog);
        final TimePicker simpleTimePicker = (TimePicker) findViewById(R.id.timepicker1);
        simpleTimePicker.setIs24HourView(true);
    }
}
