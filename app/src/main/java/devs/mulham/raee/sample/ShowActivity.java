package devs.mulham.raee.sample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

/**
 * Created by hotmildc on 16/1/2561.
 */

public class ShowActivity extends Dialog implements android.view.View.OnClickListener{

    Activity activity;
    List_Database listDatabase;
    int n;

    public ShowActivity(Activity a,List_Database list_database,int i) {
        super(a);
        activity = a;
        listDatabase=list_database;
        n=i;
    }



    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showactivity);

        TextView activity_name =(TextView)findViewById(R.id.edit_activity);
        activity_name.setText(listDatabase.getDescription());

        TextView location_name = (TextView)findViewById(R.id.edit_location);
        location_name.setText(listDatabase.getLocationName());

        TextView time = (TextView)findViewById(R.id.edit_time);

        if((listDatabase.getTime()/100)<10){
            if(listDatabase.getTime()%100<10){
                time.setText("0"+String.valueOf(listDatabase.getTime()/100)+":"+"0"+String.valueOf(listDatabase.getTime()%100));
            }
            else
                time.setText("0"+String.valueOf(listDatabase.getTime()/100)+":"+String.valueOf(listDatabase.getTime()%100));
        }
        else {
            if(listDatabase.getTime()%100<10){
                time.setText(String.valueOf(listDatabase.getTime()/100)+":"+"0"+String.valueOf(listDatabase.getTime()%100));
            }
            else
                time.setText(String.valueOf(listDatabase.getTime()/100)+":"+String.valueOf(listDatabase.getTime()%100));
        }

        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                };
                handler.postDelayed(runnable,300);
            }
        });

        if(listDatabase!=null&&listDatabase.getStatus()==2||listDatabase.getStatus()==4||listDatabase.getStatus()==8){
            findViewById(R.id.textView3).setBackgroundColor(1);
        }
        else {
            findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            CustomDialogClass cdd = new CustomDialogClass(activity,listDatabase,n);
                            cdd.show();
                            cancel();
                        }
                    };
                    handler.postDelayed(runnable,300);

                }
            });
        }


    }
}
