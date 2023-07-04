package com.example.sbaapp.ClassVideoPackage.ManageVideoContent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sbaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UploadVideoContent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int PICK_VIDEO_REQUEST = 1;
    private VideoView videoPreview;
    private Button buttonChoose, buttonUpload;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageReference videoRef;
    private EditText editPreviewVideoName, editPreviewVideoDescription;
    private Boolean isPlaying;
    private ImageButton playBtn;
    private Spinner spinnerLevel, spinnerClass, spinnerSubclass;
    private String khat, nb, video_admin;
    private int subclassNum;
    private Uri urlVideoPath, urlThumbnailPath;
    private Boolean hasVideo=false;
    //private EditText editPreviewVideoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video_content);

        initializeViews();



        //String for Class name
        khat = "Khat";
        nb = "Nurul Bayan";

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        //access FirebaseStorage database
        storageReference = FirebaseStorage.getInstance().getReference();
        videoRef=storageReference.child("/videos/uploaded.mp4");


        //Dropdown adapters
        ArrayAdapter<String> spinnerLevelAdapter = new ArrayAdapter<String>(UploadVideoContent.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.spinnerClassLevel));
        spinnerLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(spinnerLevelAdapter);

        ArrayAdapter<String> spinnerClassAdapter = new ArrayAdapter<String>(UploadVideoContent.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.spinnerClass));
        spinnerClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(spinnerClassAdapter);

        spinnerClass.setOnItemSelectedListener(this);



    }

    public void initializeViews(){
        //video
        videoPreview = findViewById(R.id.videoPreview);

        //buttons
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        playBtn =findViewById(R.id.playBtn);

        //EditText
        editPreviewVideoName = findViewById(R.id.editPreviewVideoName);
        editPreviewVideoDescription = findViewById(R.id.editPreviewVideoDescription);

        //Dropdown
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubclass = findViewById(R.id.spinnerSubclass);
    }

    //choose a file
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Video"), PICK_VIDEO_REQUEST);
    }

    //upload after preview
    private void uploadFile(){

        if(hasVideo==true && !TextUtils.isEmpty(editPreviewVideoName.getText().toString()) && !TextUtils.isEmpty(editPreviewVideoDescription.getText().toString())) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading... ");

            final StorageReference storeRef = storageReference.child("class_video/"+editPreviewVideoName.getText());

            storeRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                            //check user name




                            //get URL
                            storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    urlVideoPath = uri;
                                    //urlPath = uri.toString();
                                    //Toast.makeText(UploadVideoContent.this,"Dh masuk Retrieved video URL : "+urlVideoPath,Toast.LENGTH_LONG).show();


                                    //Retrieving video thumbnail as bitmap
                                    try {
                                        final StorageReference thumbnailStoreRef = storageReference.child("video_thumbnail/"+editPreviewVideoName.getText());
                                        Bitmap bitmap = retrieveVideoFrameFromVideo(urlVideoPath.toString());
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                                        byte[] data = byteArrayOutputStream.toByteArray();

                                        UploadTask uploadTask = (UploadTask) thumbnailStoreRef.putBytes(data)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        //Toast.makeText(UploadVideoContent.this,"Success upload thumbnail",Toast.LENGTH_LONG).show();
                                                        thumbnailStoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                urlThumbnailPath = uri;

                                                                //pass info into UploadVideoContentDetails.java
                                                                UploadVideoContentDetails videoDetails = new UploadVideoContentDetails(
                                                                        video_admin,
                                                                        editPreviewVideoName.getText().toString(),
                                                                        editPreviewVideoDescription.getText().toString(),
                                                                        urlVideoPath.toString(),
                                                                        urlThumbnailPath.toString()
                                                                );

//                                                                Toast.makeText(UploadVideoContent.this,"From videoDetails video URL : " + videoDetails.getVideo_url(),Toast.LENGTH_SHORT).show();
                                                                System.out.println("Success upload video :" +videoDetails.getVideo_url());
                                                                System.out.println("Success upload thumbnail :" +videoDetails.getVideo_thumbnail_url());


                                                                //Insert into Realtime Database
                                                                //access Firebase Realtime DB
                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                databaseReference = database.getReference();
                                                                //Khat
                                                                if(subclassNum == 1) {
                                                                    databaseReference
                                                                            .child("ClassList")
                                                                            .child("Class" + khat)
                                                                            .child("Subclass"+spinnerSubclass.getSelectedItem())
                                                                            .child(String.valueOf(spinnerLevel.getSelectedItem()))
                                                                            .child(String.valueOf(spinnerSubclass.getSelectedItem()).substring(0,1)+"_"
                                                                                    + String.valueOf(spinnerLevel.getSelectedItem()).substring(0,1)+"_"
                                                                                    + editPreviewVideoName.getText().toString())
                                                                            .setValue(videoDetails);

//                                                                    Toast.makeText(getApplicationContext(),"Redirecting to Admin Screen..",Toast.LENGTH_LONG).show();
//                                                                    Intent intent = new Intent(UploadVideoContent.this, AdminMainActivity.class);
//                                                                    startActivity(intent);

                                                                }
                                                                //Nurul Bayan
                                                                if(subclassNum == 2) {
                                                                    databaseReference
                                                                            .child("ClassList")
                                                                            .child("Class" + nb)
                                                                            //.child("Subclass"+spinnerSubclass.getSelectedItem())
                                                                            .child(String.valueOf(spinnerLevel.getSelectedItem()))
                                                                            .child(//String.valueOf(spinnerSubclass.getSelectedItem()).substring(0,1)
                                                                                    String.valueOf(spinnerLevel.getSelectedItem()).substring(0,1)+"_"
                                                                                            + editPreviewVideoName.getText().toString())
                                                                            .setValue(videoDetails);

//                                                                    Toast.makeText(getApplicationContext(),"Redirecting to Admin Screen..",Toast.LENGTH_SHORT).show();
//                                                                    Intent intent = new Intent(UploadVideoContent.this, AdminMainActivity.class);

                                                                }

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                System.out.println(e.getStackTrace());
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }



                                }
                            });

                            //String getUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();










                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress)+"% Uploaded...");
                }
                    });
        }else{
            if(TextUtils.isEmpty(editPreviewVideoName.getText().toString())){
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoContent.this);
                builder.setCancelable(true);
                builder.setTitle("No Video Name");
                builder.setMessage("Please fill in the video name");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"Please enter video name",Toast.LENGTH_LONG).show();

                            }
                        });
//                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
            if(TextUtils.isEmpty(editPreviewVideoDescription.getText().toString())){
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoContent.this);
                builder.setCancelable(true);
                builder.setTitle("No Video Description");
                builder.setMessage("Please fill in the video description");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"Please enter video description",Toast.LENGTH_LONG).show();

                            }
                        });
//                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }if(hasVideo==false){
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoContent.this);
                builder.setCancelable(true);
                builder.setTitle("No Video");
                builder.setMessage("Please choose a video");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"Please choose a video",Toast.LENGTH_LONG).show();

                            }
                        });
//                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }



        }

    }

    //Preview Video
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_VIDEO_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            hasVideo = true;

            Toast.makeText(this, "Video content URI: " + data.getData(),
                    Toast.LENGTH_LONG).show();
            videoPreview.setVideoURI(filePath);
            videoPreview.requestFocus();
            videoPreview.canPause();
            videoPreview.start();

            isPlaying=true;

            playBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    if(isPlaying){
                        videoPreview.pause();
                        isPlaying=false;
                        playBtn.setImageResource(R.drawable.ic_play);
                    }
                    else{
                        videoPreview.start();
                        isPlaying=true;
                        playBtn.setImageResource(R.drawable.ic_pause);
                    }
                }
            });



            /*try {

                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }catch(IOException e){
                e.printStackTrace();
            }*/
        }
    }


    @Override
    public void onClick(View view){
        if(view == buttonChoose){
            showFileChooser();
        }else if(view == buttonUpload){

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoContent.this);
            builder.setCancelable(true);
            builder.setTitle("Upload Video");
            builder.setMessage("You are about to upload this video. Confirm?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uploadFile();

                        }
                    });
                builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String spinnerClassText= String.valueOf(spinnerClass.getSelectedItem());
        Toast.makeText(this, spinnerClassText, Toast.LENGTH_SHORT).show();
        if(spinnerClassText.contentEquals(khat)) {
            spinnerSubclass.setVisibility(View.VISIBLE);
            ArrayAdapter<String> spinnerSubclassAdapter = new ArrayAdapter<String>(UploadVideoContent.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerSubclassKhat));
            spinnerSubclassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubclass.setAdapter(spinnerSubclassAdapter);
            subclassNum = 1;
        }
        if(spinnerClassText.contentEquals(nb)) {
            //ArrayAdapter<String> spinnerSubclassAdapter = new ArrayAdapter<String>(UploadVideoContent.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerSubclassNB));
            //spinnerSubclassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spinnerSubclass.setAdapter(spinnerSubclassAdapter);
            spinnerSubclass.setVisibility(View.INVISIBLE);
            subclassNum = 2;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Get Video Thumbnail
    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 17)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retrieveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
