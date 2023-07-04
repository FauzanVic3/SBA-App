package com.example.sbaapp.AdminFeedbackFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import com.example.sbaapp.R;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FragmentFeedbackNurulBayan extends Fragment {

    private String classID,uid,name,email;

    private Uri submissionUrl;

    private Toolbar toolbarQuestion;
    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewSubmissionComment,textViewTaskQuestion,textViewName;

    private RatingBar ratingBar;
    private EditText commentET;
    private Button submitBtn,toggleQuestionBtn;

    private StorageReference storageReference;
    private DatabaseReference databaseReference,submissionDatabase;
    private FirebaseUser mCurrentUser;

    //record audio declaration
    private Chronometer chronometer,chronometerRecorder;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop,imageViewPlayRecorder;
    //private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay, linearLayoutPlayRecorder;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null, fileNameRecorder =null;
    private static String mediaUrl;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    private int RECORD_AUDIO_REQUEST_CODE =123 ;
    private Boolean haveRecord =false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feedback_nurul_bayan,container,false);

        textViewSubmissionStatus = view.findViewById(R.id.textViewSubmissionStatus);
        textViewSubmissionRate = view.findViewById(R.id.textViewSubmissionRate);
        textViewSubmissionComment = view.findViewById(R.id.textViewSubmissionComment);
        textViewTaskQuestion = view.findViewById(R.id.textViewTaskQuestion);
        textViewName = view.findViewById(R.id.textViewName);
        commentET = view.findViewById(R.id.commentET);


        //User Submission mediaplayer
        chronometer = (Chronometer) view.findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewPlay = view.findViewById(R.id.imageViewPlay);
        imageViewStop = view.findViewById(R.id.imageViewStop);


        //Admin Feedback mediaplayer
        chronometerRecorder = (Chronometer) view.findViewById(R.id.chronometerTimerRecorder);
        chronometerRecorder.setBase(SystemClock.elapsedRealtime());
        imageViewPlayRecorder =view.findViewById(R.id.imageViewPlayRecorder);
        imageViewRecord = view.findViewById(R.id.imageViewRecord);
        imageViewStop = (ImageView) view.findViewById(R.id.imageViewStop);
        linearLayoutRecorder = view.findViewById(R.id.linearLayoutRecorder);
        linearLayoutPlayRecorder = view.findViewById(R.id.linearLayoutPlayRecorder);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getPermissionToRecordAudio();
        }



        classID = this.getArguments().getString("classID");
        uid = this.getArguments().getString("uid");
        name = this.getArguments().getString("name");
        email = this.getArguments().getString("email");

        textViewName.setText(name +" "+classIdConverter(classID));

        //Handles Rating Stars
        final RatingBar ratingBar = view.findViewById(R.id.ratingBar);


        if(classID.equals("nurul_bayan_beginner"))
            textViewTaskQuestion.setText(R.string.question_nurul_bayan_beginner);
        else if(classID.equals("nurul_bayan_intermediate"))
            textViewTaskQuestion.setText(R.string.question_nurul_bayan_intermediate);
        else if(classID.equals("nurul_bayan_advanced"))
            textViewTaskQuestion.setText(R.string.question_nurul_bayan_advanced);

        String class_name = classID;
        String currentUID = uid;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fileName = dataSnapshot.child("submission_link").getValue().toString();
                textViewSubmissionStatus.setText(dataSnapshot.child("submission_status").getValue().toString());
                textViewSubmissionRate.setText(dataSnapshot.child("submission_rating").getValue().toString());
                textViewSubmissionComment.setText(dataSnapshot.child("submission_comment").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //User
        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !isPlaying && fileName != null ){
                    isPlaying = true;
                    startPlaying();
                }else{
                    isPlaying = false;
                    stopPlaying();
                }
            }
        });

        //Admin Feedback
        imageViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareforRecording();
                startRecording();

            }
        });
        imageViewPlayRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !isPlaying && fileNameRecorder != null ){
                    isPlaying = true;
                    startPlayingRecorder();
                }else{
                    isPlaying = false;
                    stopPlayingRecorder();
                }
            }
        });
        imageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareforStop();
                stopRecording();
            }
        });

        //submit recorded audio to firebase
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final float rated = ratingBar.getRating();
                    if(rated>=0 && !commentET.getText().toString().equals(null) && haveRecord==true){
                        System.out.println("Rating Bar: "+rated);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Send Feedback");
                        builder.setMessage("You are about to send this feedback. Confirm?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            uploadFile(rated, commentET.getText().toString());
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                    else if(commentET.getText().toString().equals(null) || rated==0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Empty fields");
                        builder.setMessage("Please rate the submission and comment");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(),"Please rate the submission and comment",Toast.LENGTH_LONG).show();

                                    }
                                });
                        //builder.setNegativeButton(android.R.string.cancel, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else if(haveRecord==false){
                        errorMessage();
                    }


                //
            }
        });
        toggleQuestionBtn = view.findViewById(R.id.toggleQuestionBtn);
        toolbarQuestion = view.findViewById(R.id.toolbarQuestion);
        toggleQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toolbarQuestion.getVisibility() ==View.GONE){
                    toolbarQuestion.setVisibility(View.VISIBLE);
                    toggleQuestionBtn.setText("HIDE ASSESSMENT QUESTION");
                }
                else{
                    toolbarQuestion.setVisibility(View.GONE);
                    toggleQuestionBtn.setText("VIEW ASSESSMENT QUESTION");
                }

            }
        });





        return view;
    }



    public String classIdConverter(String classID){

        String new_class=null;

        switch(classID){
            case "nurul_bayan_beginner":{
                new_class = "Nurul Bayan | Beginner";
                break;
            }case "nurul_bayan_intermediate":{
                new_class = "Nurul Bayan | Intermediate";
                break;
            }case  "nurul_bayan_advanced":{
                new_class = "Nurul Bayan | Advanced";
                break;
            }case "khat_riqah_beginner":{
                new_class="Khat | Riqah | Beginner";
                break;
            }
            case "khat_riqah_intermediate":{
                new_class="Khat | Riqah | Intermediate";
                break;
            }
            case "khat_riqah_advanced":{
                new_class="Khat | Riqah | Advanced";
                break;
            }
            case "khat_nasakh_beginner":{
                new_class="Khat | Nasakh | Beginner";
                break;
            }
            case "khat_nasakh_intermediate":{
                new_class="Khat | Nasakh | Intermediate";
                break;
            }
            case "khat_nasakh_advanced":{
                new_class="Khat | Nasakh | Advanced";
                break;
            }
            case "khat_farisi_beginner":{
                new_class="Khat | Farisi | Beginner";
                break;
            }
            case "khat_farisi_intermediate":{
                new_class="Khat | Farisi | Intermediate";
                break;
            }
            case "khat_farisi_advanced":{
                new_class="Khat | Farisi | Advanced";
                break;
            }
            case "khat_diwani_beginner":{
                new_class="Khat | Diwani | Beginner";
                break;
            }
            case "khat_diwani_intermediate":{
                new_class="Khat | Diwani | Intermediate";
                break;
            }
            case "khat_diwani_advanced":{
                new_class="Khat | Diwani | Advanced";
                break;
            }
            case "khat_diwani_jali_beginner":{
                new_class="Khat | Diwani Jali | Beginner";
                break;
            }
            case "khat_diwani_jali_intermediate":{
                new_class="Khat | Diwani Jali | Intermediate";
                break;
            }
            case "khat_diwani_jali_advanced":{
                new_class="Khat | Diwani Jali | Advanced";
                break;
            }
            case "khat_thuluth_beginner":{
                new_class="Khat | Thuluth | Beginner";
                break;
            }
            case "khat_thuluth_intermediate":{
                new_class="Khat | Thuluth | Intermediate";
                break;
            }
            case "khat_thuluth_advanced":{
                new_class="Khat | Thuluth | Advanced";
                break;
            }

        }

        return new_class;
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            //fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        //seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        //seekBar.setMax(mPlayer.getDuration());
        //seekUpdation();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
            }
        });

    }

    private void startPlayingRecorder() {
        mPlayer = new MediaPlayer();
        try {
            //fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileNameRecorder);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlayRecorder.setImageResource(R.drawable.ic_pause);

        //seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        //seekBar.setMax(mPlayer.getDuration());
        //seekUpdation();
        chronometerRecorder.setBase(SystemClock.elapsedRealtime());
        chronometerRecorder.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlayRecorder.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometerRecorder.stop();
            }
        });

    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();
    }
    private void stopPlayingRecorder() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlayRecorder.setImageResource(R.drawable.ic_play);
        chronometerRecorder.stop();
    }

    private void prepareforRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlayRecorder.setVisibility(View.GONE);
    }

    private void startRecording() {

        //get user id

        final String currentUID = uid;

        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        haveRecord=true;
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/ShaikhBakryAyoub/Audios/Feedback/"+classID);
        if (!file.exists()) {
            file.mkdirs();
        }

        fileNameRecorder =  root.getAbsolutePath() + "/ShaikhBakryAyoub/"+"Audios/Feedback/"+classID+"_" + currentUID+".mp3";
        Log.d("filename",fileNameRecorder);
        mRecorder.setOutputFile(fileNameRecorder);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        //seekBar.setProgress(0);
        stopPlayingRecorder();
        //starting the chronometer
        chronometerRecorder.setBase(SystemClock.elapsedRealtime());
        chronometerRecorder.start();
    }

    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlayRecorder.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
            haveRecord=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometerRecorder.stop();
        chronometerRecorder.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        Toast.makeText(getActivity(), "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }
    public void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("No recording");
        builder.setMessage("Please press the Microphone icon to record your voice");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                //finishAffinity();
            }
        }

    }

    public String specialClassNameConverter(String classSelection, String level){
        String new_class_name;

        new_class_name = "nurul_bayan_"+level.toLowerCase();

        return new_class_name;
    }

    public void uploadFile(final Float rating, final String comment) throws FileNotFoundException {



        final String currentUID = uid;


        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/ShaikhBakryAyoub/Audios/Feedback/"+classID);
        if (!file.exists()) {
            file.mkdirs();
        }


        fileNameRecorder =  root.getAbsolutePath() + "/ShaikhBakryAyoub/"+"Audios/Feedback/"+classID+"_" + currentUID+".mp3";
        Log.d("filename",fileNameRecorder);

        Uri filePath = Uri.fromFile(new File(fileNameRecorder));


        if(fileNameRecorder!=null){

            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("audio/mpeg").build();

            storageReference= FirebaseStorage.getInstance().getReference();
            final StorageReference storeRef = storageReference.child("feedback/"+classID+"/"+currentUID);

            UploadTask uploadTask = storeRef.putFile(filePath,metadata);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            submissionUrl = uri;


                            String class_name = classID;

                            final String class_name_= class_name;


                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                            //Store in Users node
                            databaseReference = database.child("Users").child(currentUID).child("class_details").child(class_name).child("0");

                            databaseReference.child("submission_link_feedback").setValue(submissionUrl.toString());


//                            String rating = Float.toString(ratingBar.getRating());
                            System.out.println("Rating bar is: "+rating);
                            String rate = String.format("%.1f",rating);


                            databaseReference.child("submission_rating").setValue(rate);
                            databaseReference.child("submission_status").setValue("Evaluated");
                            databaseReference.child("submission_comment").setValue(comment);

                            //Update to Submission child in Database

                            submissionDatabase = database.child("Submission").child(class_name_).child(currentUID);
                            submissionDatabase.child("submission_status").setValue("Evaluated");
                            submissionDatabase.child("submission_rating").setValue(rate);
                            submissionDatabase.child("submission_comment").setValue(comment);
                            submissionDatabase.child("submission_link_feedback").setValue(submissionUrl.toString());

                            //End Submission child in Database

                            Toast.makeText(getActivity(), "Upload success!", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Failed upload");
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

}
