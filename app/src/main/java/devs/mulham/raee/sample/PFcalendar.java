package devs.mulham.raee.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hotmildc.shareact.MainActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.CompactCalendar;
import com.project.kmitl57.beelife.ProfileFriend;
import com.project.kmitl57.beelife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PFcalendar extends Fragment{
    private Handler handler = new Handler();
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    String[] week = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    TextView date;
    ImageButton next;
    ImageButton previous;
    FirebaseDatabase firebaseDatabase;
    int ID=1547;
    String name;
    String surname;
    String username=null;
    ArrayList<Integer> check;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendarpf,container,false);
        date = (TextView) rootView.findViewById(R.id.dateShow);
        next = (ImageButton) rootView.findViewById(R.id.ne);
        previous = (ImageButton) rootView.findViewById(R.id.pre);
        date.setText(dateFormatMonth.format(new Date()));
        check=new ArrayList();
        firebaseDatabase=FirebaseDatabase.getInstance();
        compactCalendar = (CompactCalendarView) rootView.findViewById(R.id.cdv);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        //Set an event for Teachers' Professional Day 2016 which is 21st of October
        compactCalendar.setDayColumnNames(week);

        DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/");

        Date dateClicked=new Date();

        int d=dateClicked.getDate()*10000+(dateClicked.getMonth()+1)*100+dateClicked.getYear()%100;
        ProfileFriend.date=d;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String User=null;
                if(ProfileFriend.username!=null){
                    User=ProfileFriend.username;
                    ID = ProfileFriend.friendID;
                }else {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        int id=Integer.parseInt(ds.child("ID").getValue().toString());
                        if(id==ID){
                            User=ds.child("Username").getValue().toString();
                            break;
                        }
                    }
                }
                DatabaseReference databaseReference1=firebaseDatabase.getReference("Users/"+
                ID+"/"+User+"/NumberOfActivities/");
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Date d = new Date();
                        int n1,n2,n3;
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            n1=0;
                            n2=0;
                            n3=0;
                            int date= MainActivity.convertDateToForm(ds.getKey().toString());
                            for(DataSnapshot In_ds:ds.getChildren()){
                                int time=MainActivity.ConvertTimeToForm(In_ds.getKey().toString());
                                d.setDate(date/10000);
                                d.setMonth((date/100)%100-1);
                                d.setYear(100+date%100);
                                if(time<1200&&!IsAdd(1)){
                                    if(!IsAdd(1)){
                                        compactCalendar.addEvent(new Event(Color.rgb(0,174,237),d.getTime()));
                                        check.add(1);
                                    }
                                    n1++;
                                }else if(time<1800){
                                    if(!IsAdd(2)){
                                        compactCalendar.addEvent(new Event(Color.rgb(6,187,142),d.getTime()));
                                        check.add(2);
                                    }
                                    n2++;
                                }else{
                                    if(!IsAdd(3)){
                                        compactCalendar.addEvent(new Event(Color.rgb(14,49,117),d.getTime()));
                                        check.add(3);
                                    }
                                    n3++;
                                }
                            }
                            FriendDataForPF friendDataForPF = new FriendDataForPF(date,n1,n2,n3);
                            ProfileFriend.numberAct.add(friendDataForPF);
                            check.clear();
                        }
                        compactCalendar.invalidate();
                        new PFoverview();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        compactCalendar.addEvent(new Event(Color.rgb(0,174,237),1520091470000L));
        compactCalendar.addEvent(new Event(Color.rgb(6,187,142),1520091470000L));
        compactCalendar.addEvent(new Event(Color.rgb(14,49,117),1520091470000L));

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                int d=dateClicked.getDate()*10000+(dateClicked.getMonth()+1)*100+dateClicked.getYear()%100;
                ProfileFriend.date=d;
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

        return rootView;

    }

    public boolean IsAdd(int time){
        for(int i=0;i<check.size();i++){
            if(check.get(i)==time){
                return true;
            }
        }
        return false;
    }

}
