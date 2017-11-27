package com.example.moya.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //
        Thread t=new Thread()
        {
            public void run()
            {
                try
                {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Thread.sleep(1000);
                    currentUser = mAuth.getCurrentUser();
                    if (currentUser == null)
                    {
                        Intent intent=new Intent(getApplicationContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(), Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();

    }
}
