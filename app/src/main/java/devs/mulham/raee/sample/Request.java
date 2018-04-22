package devs.mulham.raee.sample;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.ProfilePage;
import com.project.kmitl57.beelife.R;

public class Request extends AppCompatActivity {
    static public int fragmentPosition;
    ViewPager viewPager;
    FirebaseDatabase firebaseDatabase;
    public Context mCx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mCx=getBaseContext();
        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFriendDialog fnd =new FindFriendDialog(Request.this);
                fnd.show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Request.this, MainActivity4.class);
                Request.this.startActivity(intent);
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getApplicationContext(),getSupportFragmentManager());
        viewPager.setCurrentItem(fragmentPosition);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        firebaseDatabase=FirebaseDatabase.getInstance();
        Toast.makeText(this,String.valueOf(fragmentPosition),Toast.LENGTH_SHORT).show();


        for(int i=0;i<MainActivity4.ListRequest.size();i++){
            final int pb_key=i;
            DatabaseReference databaseReference = firebaseDatabase.getReference("Profiles/");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        int id=ds.getValue(getProfiles.class).getID();
                        if(MainActivity4.ListRequest.get(pb_key).getID()==id){
                            String User=ds.getValue(getProfiles.class).getUsername();
                            MainActivity4.ListRequest.get(pb_key).setName(User);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(fragmentPosition);
            }
        },1000);
    }
}
