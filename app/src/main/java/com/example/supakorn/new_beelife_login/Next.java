package com.example.supakorn.new_beelife_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.project.kmitl57.beelife.R;

public class Next extends AppCompatActivity {
    public static final String TAG = "LogOut ";
    public CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Button LogOut = (Button)findViewById(R.id.LogOut);
        mCallbackManager = CallbackManager.Factory.create();
        //txt.setText(mCallbackManager);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( AccessToken.getCurrentAccessToken() != null){
                    LoginManager.getInstance().logOut();
                }
                else{
                    FirebaseAuth.getInstance().signOut();
                }

                startActivity(new Intent(Next.this,Login.class));
            }
        });
    }

}
