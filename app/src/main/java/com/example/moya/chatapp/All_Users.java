package com.example.moya.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_Users extends Activity {


    private DatabaseReference databaseReference ;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__users);

        recyclerView = (RecyclerView) findViewById(R.id.allUsersView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class , R.layout.single_user_layout , UsersViewHolder.class , databaseReference) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, final int position) {
                viewHolder.setName(model.getName());
                viewHolder.setImage(model.getThumbimage());
                viewHolder.setStatus(model.getStatus());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user_id = getRef(position).getKey().toString();
                        Intent intent = new Intent(getApplicationContext() , Profile.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
