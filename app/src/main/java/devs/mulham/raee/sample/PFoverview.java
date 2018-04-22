package devs.mulham.raee.sample;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.kmitl57.beelife.ProfileFriend;
import com.project.kmitl57.beelife.R;

/**
 * Created by PREM on 2/19/2018.
 */

public class PFoverview extends Fragment {
    private int progressStatus = 0;
    TextView textmorning;
    TextView textafternoon;
    TextView textevening;
    TextView textday;
    TextView textstatus;
    TextView textEvent;
    TextView textnEvent;
    TextView textmonthyear;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_overviewpf,container,false);

        final Handler handler = new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                textmorning=(TextView)rootView.findViewById(R.id.textMorning);
                textafternoon=(TextView)rootView.findViewById(R.id.textafternoon);
                textevening=(TextView)rootView.findViewById(R.id.textevening);
                textmonthyear=(TextView)rootView.findViewById(R.id.textMonthYear);
                textday=(TextView)rootView.findViewById(R.id.textDay);
                textstatus=(TextView)rootView.findViewById(R.id.textStatus);
                textEvent=(TextView)rootView.findViewById(R.id.textEvent);
                textnEvent=(TextView)rootView.findViewById(R.id.textn_act);

                int morning=0;
                int afternoon=0;
                int evening=0;
                int total=0;

                for(int i=0;i<ProfileFriend.numberAct.size();i++){
                    if(ProfileFriend.numberAct.get(i).getDate()==ProfileFriend.date){
                        morning=ProfileFriend.numberAct.get(i).getN1();
                        afternoon=ProfileFriend.numberAct.get(i).getN2();
                        evening=ProfileFriend.numberAct.get(i).getN3();
                        total=morning+afternoon+evening;
                    }
                }
                String monthyear = com.example.hotmildc.shareact.MainActivity.ConvertDateToStringM(ProfileFriend.date);
                textmonthyear.setText(monthyear.substring(3));
                textday.setText(String.valueOf(ProfileFriend.date/10000));
                textEvent.setText("YOU HAVE ");
                textnEvent.setText(String.valueOf(total)+" EVENTS");
                progressStatus = 80;
                final ProgressBar pbMorning = (ProgressBar) rootView.findViewById(R.id.pbMorning);
                final ProgressBar pbAfternoon = (ProgressBar) rootView.findViewById(R.id.pbAfternoon);
                final ProgressBar pbEvening = (ProgressBar) rootView.findViewById(R.id.pbEvening);
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
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);

        return rootView;

    }

}
