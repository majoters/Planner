package com.example.supakorn.new_beelife_login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.kmitl57.beelife.MainActivity;
import com.project.kmitl57.beelife.R;

import java.util.ArrayList;
import java.util.Date;

public class Register2 extends AppCompatActivity {
    public static String Email;
    public static String Password;
    public static String Username;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    EditText name;
    EditText surname;
    EditText nickname;
    EditText Province;
    EditText Hobby;
    static ArrayList<Integer> All_ID;
    public static int ID;
    public Uri filePath; //Uri.parse(extras.getString("uri"));  getIntent().getData();
    public Uri downloadUri;
    public static String UrlImg;
    public String Usermail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Bundle extras = getIntent().getExtras();
        filePath= Uri.parse(extras.getString("filePath")); //Uri.parse(extras.getString("filePath"));
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        All_ID=new ArrayList<>();
        name=(EditText)findViewById(R.id.name);
        surname=(EditText)findViewById(R.id.editText3);
        nickname=(EditText)findViewById(R.id.editText4);
        Province=(EditText)findViewById(R.id.editText5);
        Hobby=(EditText)findViewById(R.id.editText6);
        Button signup=(Button)findViewById(R.id.button3);

        DatabaseReference getAllID=firebaseDatabase.getReference("Profiles/");
        getAllID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    //Get Each ID To Array
                    int id_start=ds.getValue().toString().indexOf("ID")+3;
                    Log.d("id_start:", String.valueOf(id_start));
                    int id_stop=ds.getValue().toString().indexOf(",",id_start);
                    Log.d("id_stop:", String.valueOf(id_stop));
                    //java.lang.StringIndexOutOfBoundsException: length=15; regionStart=2; regionLength=-3
                    String id=ds.getValue().toString().substring(id_start,id_stop);
                    Log.d("String id:", id);
                    int ID=Integer.parseInt(id);
                    All_ID.add(ID);
                }
                ID=GenerateID();
                Log.d("Generated ID: ", String.valueOf(ID));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Log.d("ID: ", String.valueOf(ID));
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.createUserWithEmailAndPassword(Email,Password)
                        .addOnCompleteListener(Register2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SignUp", "createUserWithEmail:success");
                                    //FirebaseUser user = auth.getCurrentUser();
                                    //updateUI(user);
                                    Toast.makeText(Register2.this, "Sign up Success.",
                                            Toast.LENGTH_SHORT).show();
                                    Usermail=Email.substring(0,Email.indexOf("."));
                                    MainActivity.Email = Usermail;
                                    databaseReference=firebaseDatabase.getReference("Profiles/");
                                    databaseReference.child(Usermail).child("Name").setValue(name.getText().toString());
                                    databaseReference.child(Usermail).child("Surname").setValue(surname.getText().toString());
                                    databaseReference.child(Usermail).child("Nickname").setValue(nickname.getText().toString());
                                    databaseReference.child(Usermail).child("Username").setValue(Username);
                                    databaseReference.child(Usermail).child("Email").setValue(Email);
                                    databaseReference.child(Usermail).child("Province").setValue(Province.getText().toString());
                                    databaseReference.child(Usermail).child("Hobby").setValue(Hobby.getText().toString());
                                    Log.d("ID: ", String.valueOf(ID));
                                    uploadImage(ID);
                                    databaseReference.child(Usermail).child("ID").setValue(ID);
                                    //Log.d("downloadUri: ", UrlImg);
                                    //databaseReference.child(Usermail).child("Avatar").setValue(UrlImg);
                                    Log.d("Avatar: ","Save to firebase");


                                    auth.signInWithEmailAndPassword(Email,Password);
                                    Intent login = new Intent(Register2.this,MainActivity.class);
                                    startActivity(login);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register2.this, "Sign up failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }

    public static int GenerateID(){
        Date date = new Date();
        int hour=date.getHours();
        int minute=date.getMinutes();
        int ID=hour*100+minute;
        boolean complete=false;
        while (!complete){
            int c=0;
            for(int i=0;i<All_ID.size();i++){
                if(All_ID.get(i)==ID){
                    c++;
                }
            }
            if(c==0){
                complete=true;
            }else{
                if(ID>10000)
                    ID=ID-10000;
                else
                    ID+=1;
            }
        }
        return ID;
    }
    private void uploadImage(int ID) {

        if(filePath != null)
        {
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("Uploading...");
            //progressDialog.show();

            // Clear the last download, if any
            downloadUri = null;
            StorageReference ref = storageReference.child("images/"+ID); //child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            //Toast.makeText(Register2.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            UrlImg = downloadUri.toString();
                            Log.d("downloadUri: ", String.valueOf(downloadUri));
                            Toast.makeText(Register2.this, UrlImg, Toast.LENGTH_SHORT).show();
                            databaseReference.child(Usermail).child("Avatar").setValue(UrlImg);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            //Toast.makeText(UploadImageActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            //.getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
