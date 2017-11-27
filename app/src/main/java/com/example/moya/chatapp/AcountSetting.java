package com.example.moya.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AcountSetting extends AppCompatActivity {

    private StorageReference storageReference;
    private ImageView image ;
    private FirebaseAuth mAuth;
    private static int GALLERY_PICK = 1;
    private DatabaseReference databaseReference ;
    private FirebaseUser currentUser;
    private EditText txtStatus , txtAbout;
    private TextView username ;
    private ImageButton editImage , editStatus , editAbout ;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_setting);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        txtStatus = (EditText) findViewById(R.id.acountSetting_txt_status);
        txtAbout = (EditText) findViewById(R.id.acountSetting_txt_about);
        username = (TextView) findViewById(R.id.acountSetting_displayName);
        image = (ImageView) findViewById(R.id.SingleUser_Image);
        editAbout = (ImageButton) findViewById(R.id.acountSetting_btn_editAbout);
        editImage = (ImageButton) findViewById(R.id.acountSetting_btn_editImage);
        editStatus = (ImageButton) findViewById(R.id.AcountSettings_editStatus);
        txtAbout.setMaxLines(4);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Your Profile is being uploaded!");

        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("status").setValue(txtStatus.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),"Error Occured", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);*/
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                        .start(AcountSetting.this);
            }
        });

        editAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String about = txtAbout.getText().toString();
                databaseReference.child("about").setValue(about).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),"Error Occured", Toast.LENGTH_SHORT);
                    }
                });
            }
        });


        final String current_id = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String about = dataSnapshot.child("about").getValue().toString();
                final String imageUrl = dataSnapshot.child("image").getValue().toString();

                if (!imageUrl.equals("Default"))
                    Picasso.with(getApplicationContext()).load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.default_avatar).into(image);
                        }
                    });
                txtStatus.setText(status);
                username.setText(name);
                txtAbout.setText(about);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog.show();
                final Uri resultUri = result.getUri();


                File bitmapPath = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(bitmapPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG , 100 ,baos);
                final byte[] thumb_bytes = baos.toByteArray();




                final StorageReference filePath = storageReference.child("profile_pictures/" + mAuth.getCurrentUser().getUid()+".jpg");
                final StorageReference thumbFilePath = storageReference.child("thumb_profile_pictures/" + mAuth.getCurrentUser().getUid()+ ".jpg");

                filePath.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    @SuppressWarnings("VisibleForTests") String download_url = task.getResult().getDownloadUrl().toString() ;
                                    databaseReference.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                thumbFilePath.putBytes(thumb_bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            @SuppressWarnings("VisibleForTests") String thumbDownloadUrl = task.getResult().getDownloadUrl().toString();
                                                            databaseReference.child("thumbimage").setValue(thumbDownloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        image.setImageURI(resultUri);
                                                                        Toast.makeText(getApplicationContext(),"You Changed your Profile Succesfully",Toast.LENGTH_LONG).show();
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
