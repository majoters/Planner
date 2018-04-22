package devs.mulham.raee.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.kmitl57.beelife.R;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

/**
 * Created by MI on 12/11/2017.
 */

public class CustomListView extends BaseAdapter {
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> act = new ArrayList<>();
    private ArrayList<String> loc = new ArrayList<>();
    private ArrayList<Boolean> overtime = new ArrayList<>();
    private ArrayList<Boolean> Arrive = new ArrayList<>();
    private ImageView imgArrive;
    Context mContext;
    public CustomListView(Context context, ArrayList<String> t, ArrayList<String> a, ArrayList<String> l){
        this.time=t;
        this.act=a;
        this.loc=l;
        this.mContext= context;
    }
    public CustomListView(Context context, ArrayList<String> t,
                          ArrayList<String> a, ArrayList<String> l,ArrayList<Boolean> overtime){
        this.time=t;
        this.act=a;
        this.loc=l;
        this.mContext= context;
        this.overtime = overtime;
    }
    public CustomListView(Context context, ArrayList<String> t,
                          ArrayList<String> a, ArrayList<String> l,ArrayList<Boolean> overtime,ArrayList<Boolean> Arrive){
        this.time=t;
        this.act=a;
        this.loc=l;
        this.mContext= context;
        this.overtime = overtime;
        this.Arrive = Arrive;
    }

    @Override
    public int getCount() {
        return act.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        if (view == null) {
            view = mInflater.inflate(R.layout.activity_block, parent, false);
            imgArrive = (ImageView) view.findViewById(R.id.forgot);
        }
        if (overtime.get(position) == true) {
            view.findViewById(R.id.constraintLayout4).setBackgroundColor(Color.rgb(192, 192, 192));
            //ImageView imgArrive = (ImageView) view.findViewById(R.id.forgot);
            Log.d("overtime(true)", String.valueOf(overtime.get(position)));
            //workdone = false
            if (Arrive.get(position) == false) {
                //view.findViewById(R.id.forgot).startAnimation(animation);
                //imgArrive.setBackgroundColor(Color.rgb(255, 191, 5));
                view.findViewById(R.id.constraintLayout4).setBackgroundColor(Color.rgb(192, 192, 192));
                imgArrive.findViewById(R.id.forgot).startAnimation(animation);
                Log.d("Arrive", String.valueOf(Arrive.get(position)));
            } else {
                //view.findViewById(R.id.forgot).setVisibility(View.GONE);
                imgArrive.findViewById(R.id.forgot).setVisibility(View.GONE);
                Log.d("Arrive", String.valueOf(Arrive.get(position)));
            }
        } else {
            //view.findViewById(R.id.constraintLayout4).setBackgroundColor(Color.rgb(255, 191, 5));
            //view.findViewById(R.id.forgot).setVisibility(View.GONE);
            imgArrive.findViewById(R.id.forgot).setVisibility(View.GONE);
            Log.d("overtime(false)", String.valueOf(overtime.get(position)));
        }
        try {
            TextView textTime = (TextView) view.findViewById(R.id.textTime);
            textTime.setText(time.get(position));
            TextView textActName = (TextView) view.findViewById(R.id.textActName);
            textActName.setText(act.get(position));
            TextView textLocation = (TextView) view.findViewById(R.id.textLocation);
            textLocation.setText(loc.get(position));

        }catch (IndexOutOfBoundsException e){

        }

        return view;
    }
}
