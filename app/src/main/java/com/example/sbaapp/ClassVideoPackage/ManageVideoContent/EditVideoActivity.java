package com.example.sbaapp.ClassVideoPackage.ManageVideoContent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sbaapp.AdminMainActivity;
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

public class EditVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_VIDEO_REQUEST = 1;
    private String videoID, classSelection, subClassSelection , level, video_title, video_description, video_url, prevVideoName, video_thumbnail;
    private TextView classTextView, subclassTextView, levelTextView;
    private ImageButton playBtn;
    private Button deleteBtn, updateBtn, chooseBtn;
    private EditText editPreviewVideoName, editPreviewVideoDescription;
    private VideoView classVideoView;
    private Uri videoUri, filePath, urlVideoPath, urlThumbnailPath;
    private ProgressBar bufferProgress;
    private boolean isPlaying=false;
    private boolean hasVideo=false;
    private boolean changedVideo = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);


        Bundle bundle = getIntent().getExtras();
        classSelection = bundle.getString("class");
        subClassSelection = bundle.getString("subClass");
        level = bundle.getString("level");
        video_title = bundle.getString("video_title");
        video_description = bundle.getString("video_description");
        video_url = bundle.getString("video_url");
        videoID = bundle.getString("video_id");
        video_thumbnail = bundle.getString("video_thumbnail");


        initializeViews();
        setupViews();
        setupVideo();
        deleteBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        chooseBtn.setOnClickListener(this);

    }

    public void initializeViews(){
        classTextView = findViewById(R.id.classTextView);
        subclassTextView = findViewById(R.id.subclassTextView);
        levelTextView = findViewById(R.id.levelTextView);
        editPreviewVideoDescription = findViewById(R.id.editPreviewVideoDescription);
        editPreviewVideoName =  findViewById(R.id.editPreviewVideoName);
        classVideoView = findViewById(R.id.videoPreview);
        bufferProgress = findViewById(R.id.bufferProgress);
        playBtn = findViewById(R.id.playBtn);
        updateBtn= findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        chooseBtn = findViewById(R.id.chooseBtn);


    }

    public void setupViews(){
        editPreviewVideoName.setText(video_title);
        editPreviewVideoDescription.setText(video_description);
        classTextView.setText(classSelection);
        levelTextView.setText(level);
        bufferProgress.setIndeterminate(false);

        prevVideoName = editPreviewVideoName.getText().toString();


        if(classSelection.equals("Khat")) {
            subclassTextView.setText(subClassSelection);
        }else{
            subclassTextView.setText("None");
        }

    }

    public void setupVideo(){
        videoUri = Uri.parse(video_url);

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(classVideoView);

        classVideoView.setMediaController(controller);

        classVideoView.setVideoURI(videoUri);
        classVideoView.requestFocus();


        classVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int i, int extra) {

                if(i == mp.MEDIA_INFO_BUFFERING_START){
                    bufferProgress.setVisibility(View.VISIBLE);
                }else if(i== mp.MEDIA_INFO_BUFFERING_END){
                    bufferProgress.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        classVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp) {
                bufferProgress.setVisibility(View.INVISIBLE);

            }
        });
        classVideoView.start();
        isPlaying=true;
        playBtn.setImageResource(R.drawable.ic_pause);

        //new VideoProgress().execute();

        playBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(isPlaying){
                    classVideoView.pause();
                    isPlaying=false;
                    bufferProgress.setVisibility(View.INVISIBLE);
                    playBtn.setImageResource(R.drawable.ic_play);
                }
                else{
                    classVideoView.start();
                    isPlaying=true;
                    playBtn.setImageResource(R.drawable.ic_pause);
                }
            }
        });


    }

    protected void onStop(){
        super.onStop();

        isPlaying = false;
    }

//    public class VideoProgress extends AsyncTask<Void, Integer,Void> {
//        protected  Void doInBackground(Void... voids){
//
//            do{
//                if(isPlaying) {
//
//                    current = classVideoView.getCurrentPosition() / 1000;
//                    publishProgress(current);
//                }
//            }while(videoProgress.getProgress()<=100);
//            return null;
//        }
//
//        protected void onProgressUpdate(Integer... values){
//            super.onProgressUpdate(values);
//            try{
//                int currentPercent = values[0] *100/duration;
//                videoProgress.setProgress(currentPercent);
//
//                String currString = String.format("%02d:%02d", values[0]/60, values[0]%60);
//
//                currTimer.setText(currString);
//            }catch(Exception e){
//
//            }
//
//
//
//        }
//    }

    @Override
    public void onBackPressed(){
        classVideoView.stopPlayback();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateBtn:{
                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Update Video");
                builder.setMessage("You are about to update this video. Confirm?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference databaseReference;
                                String new_id;
                                switch (classSelection){

                                    case "Khat":{
                                        new_id = String.valueOf(subClassSelection).substring(0,1)+"_"
                                                + String.valueOf(level).substring(0,1)+"_"
                                                + editPreviewVideoName.getText().toString();

                                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                                .child("ClassList")
                                                .child("Class"+classSelection)
                                                .child("Subclass"+subClassSelection)
                                                .child(level)
                                                .child(new_id);

                                        if(changedVideo){
                                            uploadFile(databaseReference);
                                        }else{
                                            uploadText();
                                        }



                                        break;
                                    }
                                    case "Nurul Bayan":{
                                        new_id = String.valueOf(level).substring(0,1)+"_"
                                                + editPreviewVideoName.getText().toString();

                                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                                .child("ClassList")
                                                .child("Class"+classSelection)
                                                .child(level)
                                                .child(new_id);

                                        if(changedVideo){
                                            uploadFile(databaseReference);
                                        }else{
                                            uploadText();
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();



                break;
            }
            case R.id.deleteBtn:{

                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Delete Video");
                builder.setMessage("You are about to delete this video. This action cannot be undone. Confirm Delete?");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteVideoStorage();
                                deleteVideoThumbnail();
                                deleteVideoDatabase();

                                Toast.makeText(getApplicationContext(),"Redirecting to Manage Video Screen..",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditVideoActivity.this, ManageVideoContentActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case R.id.chooseBtn:{
                showFileChooser();
                break;
            }
        }
    }


    //choose a file
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Video"), PICK_VIDEO_REQUEST);
    }

    //upload after preview
    private void uploadFile(final DatabaseReference databaseReference){

        if(!TextUtils.isEmpty(editPreviewVideoName.getText().toString()) && !TextUtils.isEmpty(editPreviewVideoDescription.getText().toString())) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading... ");

            deleteVideoStorage();
            deleteVideoThumbnail();
            deleteVideoDatabase();

            final StorageReference storeRef = FirebaseStorage.getInstance().getReference().child("class_video/"+editPreviewVideoName.getText());

            //Store new video
            storeRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                            //get URL
                            storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    urlVideoPath = uri;

                                    //Retrieving video thumbnail as bitmap
                                    try {
                                        final StorageReference thumbnailStoreRef = FirebaseStorage.getInstance().getReference().child("video_thumbnail/"+editPreviewVideoName.getText());
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


                                                                //Insert into Realtime Database


                                                                    databaseReference.child("video_description").setValue(editPreviewVideoDescription.getText().toString());
                                                                    databaseReference.child("video_url").setValue(urlVideoPath.toString());
                                                                    databaseReference.child("video_title").setValue(editPreviewVideoName.getText().toString());
                                                                    databaseReference.child("video_thumbnail_url").setValue(urlThumbnailPath.toString());


                                                                    Toast.makeText(getApplicationContext(),"Redirecting to Manage Video Screen..",Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(EditVideoActivity.this, ManageVideoContentActivity.class);
                                                                    startActivity(intent);
                                                                    finish();


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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
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

            }
//            if(hasVideo==false){
//                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
//                builder.setCancelable(true);
//                builder.setTitle("No Video");
//                builder.setMessage("Please choose a video");
//                builder.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getApplicationContext(),"Please choose a video",Toast.LENGTH_LONG).show();
//
//                            }
//                        });
////                builder.setNegativeButton(android.R.string.cancel, null);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//            }



        }

    }

    private void uploadText(){

        if(!TextUtils.isEmpty(editPreviewVideoName.getText().toString()) && !TextUtils.isEmpty(editPreviewVideoDescription.getText().toString())) {
            if (classSelection.equals("Khat")) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("ClassList").child("Class" + classSelection).child("Subclass" + subClassSelection)
                        .child(level).child(videoID);
                databaseReference.child("video_title").setValue(editPreviewVideoName.getText().toString());
                databaseReference.child("video_description").setValue(editPreviewVideoDescription.getText().toString());
                Toast.makeText(getApplicationContext(), "Success update text. Redirecting to Manage Video screen...", Toast.LENGTH_SHORT).show();

            } else if (classSelection.equals("Nurul Bayan")) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("ClassList").child("Class" + classSelection)
                        .child(level).child(videoID);
                databaseReference.child("video_title").setValue(editPreviewVideoName.getText().toString());
                databaseReference.child("video_description").setValue(editPreviewVideoDescription.getText().toString());
                Toast.makeText(getApplicationContext(), "Success update text. Redirecting to Manage Video screen...", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(TextUtils.isEmpty(editPreviewVideoName.getText().toString())){
                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);
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

            }
        }
    }

    //Preview Video
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_VIDEO_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            hasVideo = true;
            changedVideo = true;

            //Toast.makeText(this, "Video content URI: " + data.getData(),
             //       Toast.LENGTH_LONG).show();
            classVideoView.setVideoURI(filePath);
            classVideoView.requestFocus();
            classVideoView.canPause();
            classVideoView.start();

            isPlaying=true;

            playBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    if(isPlaying){
                        classVideoView.pause();
                        isPlaying=false;
                        playBtn.setImageResource(R.drawable.ic_play);
                    }
                    else{
                        classVideoView.start();
                        isPlaying=true;
                        playBtn.setImageResource(R.drawable.ic_pause);
                    }
                }
            });
        }
    }

    public void deleteVideoStorage(){
        StorageReference delRef = FirebaseStorage.getInstance().getReference().child("class_video/"+prevVideoName);
        delRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Success delete previous video: "+ prevVideoName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed delete previous video: "+ prevVideoName);
            }
        });
    }

    public void deleteVideoThumbnail(){
        StorageReference delRef = FirebaseStorage.getInstance().getReference().child("video_thumbnail/"+prevVideoName);
        delRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Success delete previous video thumbnail: "+ prevVideoName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed delete previous video thumbnail: "+ prevVideoName);
            }
        });
    }

    public void deleteVideoDatabase(){

        if(classSelection.equals("Khat")){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("ClassList").child("Class"+classSelection).child("Subclass"+subClassSelection)
                    .child(level).child(videoID);
            databaseReference.removeValue();
        }else if(classSelection.equals("Nurul Bayan")){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("ClassList").child("Class"+classSelection)
                    .child(level).child(videoID);
            databaseReference.removeValue();
        }

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
