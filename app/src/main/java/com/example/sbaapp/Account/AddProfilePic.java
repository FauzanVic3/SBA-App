package com.example.sbaapp.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sbaapp.R;
import com.example.sbaapp.UploadAnnouncementActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;

public class AddProfilePic extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profilePic;
    private Button uploadBtn, chooseBtn, deleteBtn;
    private Uri filePath,profilePicPath;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_pic);

        profilePic = findViewById(R.id.profilePic);
        uploadBtn = findViewById(R.id.uploadBtn);
        chooseBtn = findViewById(R.id.chooseBtn);
        deleteBtn = findViewById(R.id.deleteBtn);


        uploadBtn.setOnClickListener(this);
        chooseBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        String uid = FirebaseAuth.getInstance().getUid();

        //Checks if previous announcement exists
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("thumb_image").getValue().toString().equals("default")){
                    profilePic.setImageResource(R.drawable.ic_account);
                }else{
                    Glide.with(AddProfilePic.this)
                            .load(Uri.parse(dataSnapshot.child("thumb_image").getValue().toString()))
                            .override(300,200)
                            .into(profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.uploadBtn:
            {
                if(profilePic.getDrawable()==null){
                    Toast.makeText(this, "Choose an image first!", Toast.LENGTH_LONG).show();
                }
                else{
                    uploadFile();
                }
                break;
            }
            case R.id.chooseBtn:
            {
                showFileChooser();
                break;
            }
            case R.id.deleteBtn:
            {
                deleteAnnouncement();
                break;
            }
            default:
        }
    }

    //choose a file
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image"), PICK_IMAGE_REQUEST);
    }

    //Preview Video
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Toast.makeText(this, "Image content URI: " + data.getData(),
                    Toast.LENGTH_LONG).show();
            profilePic.setImageURI(filePath);

        }
    }

    //upload after preview
    public void uploadFile(){

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        profilePic.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) profilePic.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
        byte[] byteArray = baos.toByteArray();

        final StorageReference storeRef = FirebaseStorage.getInstance().getReference().child("images/"+uid);


        UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profilePicPath = uri;

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("thumb_image");

                        databaseReference.setValue(profilePicPath.toString());


                        Toast.makeText(AddProfilePic.this, "Upload success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProfilePic.this, AccountSetting.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProfilePic.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProfilePic.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteAnnouncement(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("thumb_image");

        databaseReference.setValue("default");

        Toast.makeText(this, "Successfully deleted the previous announcement", Toast.LENGTH_LONG).show();
    }

}
