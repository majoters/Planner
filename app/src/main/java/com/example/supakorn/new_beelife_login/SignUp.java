package com.example.supakorn.new_beelife_login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.kmitl57.beelife.R;

import java.io.IOException;

public class SignUp extends AppCompatActivity {
    FirebaseAuth auth;
    public static final String TAG = "SignUp ";
    private ImageButton imgbtn;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        auth = FirebaseAuth.getInstance();

        imgbtn = (ImageButton) findViewById(R.id.imageAvatar);
        final EditText Username = (EditText) findViewById(R.id.Username);
        final EditText Email = (EditText) findViewById(R.id.Email);
        final EditText Password = (EditText) findViewById(R.id.password);
        final EditText Confirm = (EditText) findViewById(R.id.confirm);
        final Button SignUp = (Button) findViewById(R.id.sign_up_button);


        final String username = Username.getText().toString();
        final String email = Email.getText().toString();
        final String password = Password.getText().toString();
        final String confirm = Confirm.getText().toString();

        imgbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                chooseImage();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Register2.class);
                intent.putExtra("filePath",filePath.toString());
                startActivity(intent);
                Register2.Email=Email.getText().toString();
                Register2.Password=Password.getText().toString();
                Register2.Username=Username.getText().toString();
            }
        });


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.supakorn.new_beelife_login.SignUp.this,
                        Login.class));
                finish();
            }
        });

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgbtn.setImageBitmap(bitmap);
                Log.d("Bitmap filepath", String.valueOf(filePath));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
