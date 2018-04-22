package com.project.kmitl57.beelife;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.MyPagerAdapter3;

public class ProfilePage extends AppCompatActivity {
    public static TextView name ;
    public static TextView username2 ;
    boolean edit=false;
    EditText suggestProfile;
    TextView showSuggest;
    String data="What are you thinking?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this, MainActivity4.class);
                ProfilePage.this.startActivity(intent);
                finish();
            }
        });
        username2 =(TextView)findViewById(R.id.textUsername);
        name =(TextView)findViewById(R.id.textUName);
        suggestProfile=(EditText)findViewById(R.id.suggestProfile);
        showSuggest=(TextView)findViewById(R.id.show);
        if(!edit){
            suggestProfile.setEnabled(false);
        }
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(suggestProfile.isEnabled()){
                    showSuggest.setVisibility(View.VISIBLE);
                    if(data==null||data.length()==0){
                        showSuggest.setText("What are you thinking?");
                    }else{
                        showSuggest.setText(data);
                    }
                    suggestProfile.setEnabled(false);
                    suggestProfile.setVisibility(View.INVISIBLE);
                }else {
                    showSuggest.setVisibility(View.INVISIBLE);
                    suggestProfile.setEnabled(true);
                    suggestProfile.setVisibility(View.VISIBLE);
                }
            }
        });

        suggestProfile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data=suggestProfile.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyPagerAdapter3 pagerAdapter = new MyPagerAdapter3(this,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }
}
