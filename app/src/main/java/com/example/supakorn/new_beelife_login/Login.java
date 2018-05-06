package com.example.supakorn.new_beelife_login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kmitl57.beelife.MainActivity;
import com.project.kmitl57.beelife.R;
import com.project.kmitl57.beelife.facebookloginAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import devs.mulham.raee.sample.MainActivity4;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    public static final String TAG = "Login ";
    public CallbackManager mCallbackManager;
    public String email;
    public String password;
    public String name;
    public String surname;
    LoginButton Facebooklogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        //Initialize the FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        final EditText Email = (EditText)findViewById(R.id.Email);
        final EditText Password = (EditText)findViewById(R.id.Password);
        Button SignIn = (Button) findViewById(R.id.btn_login);
        Button Signup = (Button)findViewById(R.id.btn_signup);
        boolean send=getIntent().getBooleanExtra("routine",false);

        if(currentUser!=null){
            MainActivity.Email = currentUser.getEmail();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            //int fullstop=currentUser.getEmail().indexOf(".");
            //email=currentUser.getEmail().substring(0,fullstop);
            //DatabaseReference databaseReference =firebaseDatabase.getReference("Profiles/"+
            //email+"/");
            //databaseReference.addValueEventListener(new ValueEventListener() {
             //   @Override
            //    public void onDataChange(DataSnapshot dataSnapshot) {
            //        startActivity(new Intent(Login.this,facebookloginAct.class));
            //        facebookloginAct.Email=dataSnapshot.child("Email").getValue().toString();
            //        facebookloginAct.Name=dataSnapshot.child("Name").getValue().toString();
             //       facebookloginAct.Surname=dataSnapshot.child("Surname").getValue().toString();
             //       Login.this.finish();}

             //   @Override
             //   public void onCancelled(DatabaseError databaseError) {

            //    }
            //});

        }

        if(send){
            MainActivity4.routine=true;
        }

        //mCallbackManager = CallbackManager.Factory.create();
        //Facebooklogin = (LoginButton)findViewById(R.id.btn_login_facebook);
        //Facebooklogin.setReadPermissions("email", "public_profile");
        //Facebooklogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
        //    @Override
        //    public void onSuccess(LoginResult loginResult) {
         //       Log.d(TAG, "facebook:onSuccess:" + loginResult);
         //       handleFacebookAccessToken(loginResult.getAccessToken());
         //       GraphRequest request = GraphRequest.newMeRequest(
         //               loginResult.getAccessToken(),
         //               new GraphRequest.GraphJSONObjectCallback() {
         //                   @Override
         //                   public void onCompleted(JSONObject object, GraphResponse response) {
         //                       try {
        //                          Log.w("Email",object.getString("email").toString());
         //                           Log.w("Name",object.getString("name").toString());
         //                           Log.w("ToString",object.toString());
         //                           int space=object.getString("name").indexOf(" ");
         //                           name=object.getString("name").toString().substring(0,space);
         //                           surname=object.getString("name").toString().substring(space+1);
         //                           email=object.getString("email").toString();

         //                           if( AccessToken.getCurrentAccessToken() != null){
         //                               startActivity(new Intent(Login.this,facebookloginAct.class));
         //                               facebookloginAct.Email=email;
         //                               facebookloginAct.Name=name;
         //                               facebookloginAct.Surname=surname;
         //                               Login.this.finish();
         //                           }

         //                       } catch (JSONException e) {
         //                           e.printStackTrace();
         //                       }
         //                   }
         //               }
         //       );
         //       Bundle parameters = new Bundle();
         //       parameters.putString("fields", "id,name,email,gender,birthday");
         //       request.setParameters(parameters);
         //       request.executeAsync();
        //    }

         //   @Override
         //   public void onCancel() {
         //       Log.d(TAG, "facebook:onCancel");
                // ...
         //   }

          //  @Override
         //   public void onError(FacebookException error) {
         //       Log.d(TAG, "facebook:onError", error);
         //       // ...
         //   }
        //});



        email = Email.getText().toString();
        password =Password.getText().toString();

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //updateUI(user);
                                        //old
                                        Intent next = new Intent(Login.this, MainActivity.class);
                                        MainActivity.Email=Email.getText().toString();
                                        //new
                                        //Intent next = new Intent(Login.this, MainActivity4.class);
                                        //MainActivity4.Email=Email.getText().toString();
                                        startActivity(next);
                                        Login.this.finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                }catch (Exception e){
                    Toast.makeText(Login.this, "Login failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Login.this,SignUp.class);
                startActivity(signup);

            }
        });


        //-------------------------------------------------------------------------

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.project.kmitl57.beelife",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                /*Toast.makeText(Login.this,Base64.encodeToString(md.digest(),Base64.DEFAULT),
                        Toast.LENGTH_SHORT).show();*/
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //=========================================================================

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile"));

                            startActivity(new Intent(Login.this,facebookloginAct.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if( AccessToken.getCurrentAccessToken() != null){
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                Log.w("Email",object.getString("email").toString());
                                Log.w("Name",object.getString("name").toString());
                                Log.w("ToString",object.toString());
                                int space=object.getString("name").indexOf(" ");
                                name=object.getString("name").toString().substring(0,space);
                                surname=object.getString("name").toString().substring(space+1);
                                email=object.getString("email").toString();

                                if( AccessToken.getCurrentAccessToken() != null){
                                    startActivity(new Intent(Login.this,facebookloginAct.class));
                                    facebookloginAct.Email=email;
                                    facebookloginAct.Name=name;
                                    facebookloginAct.Surname=surname;
                                    Login.this.finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Login.this,"Can't Back Press",Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
    }
}
