package com.example.sbaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UploadAnnouncementActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView previewAnnouncementImage;
    private Button uploadBtn, chooseBtn, deleteBtn;
    private Uri filePath,urlAnnouncementPath;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_announcement);

        previewAnnouncementImage = findViewById(R.id.previewAnnouncementImage);
        uploadBtn = findViewById(R.id.uploadBtn);
        chooseBtn = findViewById(R.id.chooseBtn);
        deleteBtn = findViewById(R.id.deleteBtn);


        uploadBtn.setOnClickListener(this);
        chooseBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        //Checks if previous announcement exists
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcement");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String announcementExist = dataSnapshot.child("announcement_link").getValue().toString();
                if(announcementExist.equals("default")){
                    previewAnnouncementImage.setImageResource(R.drawable.image_preview);
                }else{
                    Glide.with(UploadAnnouncementActivity.this)
                            .load(Uri.parse(announcementExist))
                            .override(300,200)
                            .into(previewAnnouncementImage);
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
                if(previewAnnouncementImage==null){
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadAnnouncementActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Delete?");
                builder.setMessage("Are you sure you want to delete?");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAnnouncement();

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

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
            previewAnnouncementImage.setImageURI(filePath);

        }
    }

    //upload after preview
    public void uploadFile(){

            previewAnnouncementImage.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) previewAnnouncementImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
            byte[] byteArray = baos.toByteArray();

            final StorageReference storeRef = FirebaseStorage.getInstance().getReference().child("announcement/news");


            UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlAnnouncementPath = uri;

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcement");

                            databaseReference.child("announcement_link").setValue(urlAnnouncementPath.toString());


                            Toast.makeText(UploadAnnouncementActivity.this, "Upload success!", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadAnnouncementActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadAnnouncementActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    public void deleteAnnouncement(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcement");

        databaseReference.child("announcement_link").setValue("default");

        Toast.makeText(this, "Successfully deleted the previous announcement", Toast.LENGTH_LONG).show();
    }


}
