package com.example.moya.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private EditText username , email , password;
    private Button createAcount ;
    private DatabaseReference myref ;
    private FirebaseAuth mAuth ;
    private ProgressDialog progress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText) findViewById(R.id.signup_txt_username);
        email = (EditText) findViewById(R.id.signup_txt_email);
        password = (EditText) findViewById(R.id.signup_txt_password);
        createAcount = (Button) findViewById(R.id.signup_btn_createAcount);
        progress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        createAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrName = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (!TextUtils.isEmpty(usrName) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass))
                {
                    progress.setTitle("Registering User");
                    progress.setMessage("Please Wait while we create your acount.");
                    progress.show();
                    register_new_user(usrName,mail,pass);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please Fill in the form Correctly.",Toast.LENGTH_LONG);

            }
        });
    }

    private void register_new_user(final String usrName, String mail, String pass) {
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            final String uid = mAuth.getCurrentUser().getUid();
                            myref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String,String> hashMap = new HashMap<>();

                            hashMap.put("name", usrName);
                            hashMap.put("image", "Default");
                            hashMap.put("status","Hi I'm Using English Learner!");
                            hashMap.put("thumbimage","Default");
                            hashMap.put("about","Default");

                            myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUp.this, "You Registered Successfully!",
                                                Toast.LENGTH_LONG).show();
                                        progress.dismiss();
                                        Intent intent = new Intent(getApplicationContext(),Home.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });



                        }
                        else
                        {
                            Toast.makeText(SignUp.this, task.getException().toString(),
                                    Toast.LENGTH_LONG).show();
                            progress.hide();
                        }
                    }
                });
    }
}
