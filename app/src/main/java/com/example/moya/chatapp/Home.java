package com.example.moya.chatapp;

import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

public class Home extends AppCompatActivity {
    private TextView txt;
    private FirebaseAuth mAuth ;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapter adapter;
    private TabItem tabItem1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_home);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_logout)
        {
            mAuth.signOut();
            startActivity(new Intent(this,Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_acountSettings)
            startActivity(new Intent(getApplicationContext(),AcountSetting.class));
        if (item.getItemId() == R.id.menu_allUsers)
            startActivity(new Intent(getApplicationContext(),All_Users.class));

        return true;
    }
}
