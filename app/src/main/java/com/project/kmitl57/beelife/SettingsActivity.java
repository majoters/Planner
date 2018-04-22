package com.project.kmitl57.beelife;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.supakorn.new_beelife_login.Login;
import com.example.supakorn.new_beelife_login.Next;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import devs.mulham.raee.sample.MainActivity4;
import devs.mulham.raee.sample.Request;

public class SettingsActivity extends AppCompatActivity {
    Button setting;
    Button history;
    Button logout;

    public static final String TAG = "LogOut ";
    public CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        setting = (Button) findViewById(R.id.btn_setting);
        history = (Button) findViewById(R.id.btn_history);
        logout = (Button) findViewById(R.id.btn_logout);
        final ArrayList<String> acti = new ArrayList<>();
        final ArrayList<String> loca = new ArrayList<>();
        mCallbackManager = CallbackManager.Factory.create();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity4.class);
                SettingsActivity.this.startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SetTimeDialog setTimeDialog = new SetTimeDialog(SettingsActivity.this);
                        setTimeDialog.show();
                    }
                }, 300);

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HistoryActivity historyActivity = new HistoryActivity(SettingsActivity.this);
                        historyActivity.show();
                    }
                }, 300);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if( AccessToken.getCurrentAccessToken() != null){
                            LoginManager.getInstance().logOut();
                        }
                        else{
                            FirebaseAuth.getInstance().signOut();
                        }

                        startActivity(new Intent(SettingsActivity.this,Login.class));
                    }
                }, 300);

            }
        });

    }
}
