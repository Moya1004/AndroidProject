package com.example.moya.chatapp;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class Profile extends AppCompatActivity {


    TextView name, status, totalFriends ;
    ImageView image;
    ProgressDialog progressDialog;
    Button sendRequest,acceptRequest;
    String mCurrentState ="not_friends";
    DatabaseReference friendsDbReference , requestsDBReferenece , databaseReference;
    String user_id , mCurrentUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Loading Profile");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        user_id = getIntent().getStringExtra("user_id");
        name = (TextView) findViewById(R.id.profile_name);
        status = (TextView) findViewById(R.id.profile_status);
        totalFriends = (TextView) findViewById(R.id.profile_totalFriends);
        sendRequest = (Button) findViewById(R.id.profile_sendRequest);
        image = (ImageView) findViewById(R.id.profile_image);
        requestsDBReferenece = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        requestsDBReferenece.child(mCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id))
                {
                    mCurrentState = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                }
                    friendsDbReference.child(mCurrentUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(user_id))
                            {
                                mCurrentState = "friend";
                            }
                            if (mCurrentState.equals("Sent"))
                                sendRequest.setText("Cancel Friend Request");
                            else if (mCurrentState.equals("Recieved"))
                            {
                                sendRequest.setText("Ignore Friend Request");
                                acceptRequest = (Button)findViewById(R.id.profile_acceptRequest);
                                acceptRequest.setEnabled(true);
                                acceptRequest.setVisibility(View.VISIBLE);
                            }
                            else if (mCurrentState.equals("friend"))
                            {
                                sendRequest.setText("Unfriend");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        friendsDbReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState.equals("not_friends"))
                    requestsDBReferenece.child(mCurrentUser).child(user_id).child("request_type").setValue("Sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                requestsDBReferenece.child(user_id).child(mCurrentUser).child("request_type").setValue("Recieved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),"Friend Request Sent",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Couldn't Sent a Friend Request",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                else if (mCurrentState.equals("Sent") || mCurrentState.equals("Recieved") || mCurrentState.equals("friend"))
                {
                    requestsDBReferenece.child(mCurrentUser).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                requestsDBReferenece.child(user_id).child(mCurrentUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            friendsDbReference.child(mCurrentUser).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                        friendsDbReference.child(user_id).child(mCurrentUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
                                                                        Toast.makeText(getApplicationContext(),"You cancelled the friend Request",Toast.LENGTH_LONG).show();
                                                                        sendRequest.setText("Send Friend Request");
                                                                        mCurrentState = "not_friend";
                                                                        acceptRequest = (Button) findViewById(R.id.profile_acceptRequest);
                                                                        acceptRequest.setVisibility(View.INVISIBLE);
                                                                        acceptRequest.setEnabled(false);
                                                                        finish();
                                                                        startActivity(getIntent());
                                                                    }
                                                            }
                                                        });
                                                }
                                            });

                                        }
                                    }
                                });

                            }
                            else
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String statuss = dataSnapshot.child("status").getValue().toString();
                String imagePath = dataSnapshot.child("image").getValue().toString();

                name.setText(displayName);
                status.setText(statuss);
                if(!imagePath.equals("Default"))
                    Picasso.with(getApplicationContext()).load(imagePath).placeholder(R.drawable.default_avatar).into(image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onError() {

                        }
                    });
                else
                    progressDialog.dismiss();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void acceptFriendRequest(View v)
    {
        Calendar c = Calendar.getInstance();
        final String currentTime = c.getTime().toString();
        friendsDbReference.child(mCurrentUser).child(user_id).setValue(currentTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    friendsDbReference.child(user_id).child(mCurrentUser).setValue(currentTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                requestsDBReferenece.child(mCurrentUser).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            requestsDBReferenece.child(user_id).child(mCurrentUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        sendRequest.setText("Unfriend");
                                                        acceptRequest.setVisibility(View.INVISIBLE);
                                                        acceptRequest.setEnabled(false);
                                                        Toast.makeText(getApplicationContext(),"User Succefully added to the friend list" , Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }
                                        else
                                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                            else
                                Toast.makeText(getApplicationContext(),"Error Occured!" , Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(),"Error Occured!" , Toast.LENGTH_LONG).show();
            }
        });
    }
}
