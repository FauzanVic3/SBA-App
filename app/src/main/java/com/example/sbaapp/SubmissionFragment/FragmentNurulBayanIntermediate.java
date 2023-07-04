package com.example.sbaapp.SubmissionFragment;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

public class FragmentNurulBayanIntermediate extends Fragment {

    private Button submitBtn;
    private String classSelection, level;

    private Uri submissionUrl;

    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewTaskQuestion;


    private StorageReference storageReference;
    private DatabaseReference databaseReference,submissionDatabase;
    private FirebaseUser mCurrentUser;

    //record audio declaration
    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    //private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    private Boolean haveRecord=false;

    private int RECORD_AUDIO_REQUEST_CODE =123 ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_nurul_bayan_intermediate,container,false);

        //Bundle from Submission class
        classSelection = this.getArguments().getString("class");
        level = this.getArguments().getString("level");

        textViewTaskQuestion =view.findViewById(R.id.textViewTaskQuestion);
        textViewTaskQuestion.setText(R.string.question_nurul_bayan_intermediate);

        //set Firebase access storage
        storageReference = FirebaseStorage.getInstance().getReference();

        //set special class_name converter
        String class_name = specialClassNameConverter(classSelection,level);

        //video recording declaration
        linearLayoutRecorder = (LinearLayout) view.findViewById(R.id.linearLayoutRecorder);
        chronometer = (Chronometer) view.findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = (ImageView) view.findViewById(R.id.imageViewRecord);
        imageViewStop = (ImageView) view.findViewById(R.id.imageViewStop);
        imageViewPlay = (ImageView) view.findViewById(R.id.imageViewPlay);
        linearLayoutPlay = (LinearLayout) view.findViewById(R.id.linearLayoutPlay);
        //seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getPermissionToRecordAudio();
        }

        imageViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareforRecording();
                startRecording();

            }
        });
        imageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareforStop();
                stopRecording();
            }
        });
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

        //get user's data
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUID = mCurrentUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");
        databaseReference.child("submission_status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewSubmissionStatus = (TextView) view.findViewById(R.id.textViewSubmissionStatus);
                textViewSubmissionStatus.setText(dataSnapshot.getValue().toString());
                System.out.println(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("submission_rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewSubmissionRate = (TextView) view.findViewById(R.id.textViewSubmissionRate);
                textViewSubmissionRate.setText(dataSnapshot.getValue().toString());

                System.out.println(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //submit recorded audio to firebase
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Submit Assessment");
                builder.setMessage("You are about to submit this assessment. Confirm?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if(haveRecord==true)
                                    uploadFile();
                                    else
                                        errorMessage();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

                //
            }
        });





        return view;
    }




    public void uploadFile() throws FileNotFoundException {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUID = mCurrentUser.getUid();


        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/ShaikhBakryAyoub/"+"/Audios/");
        if (!file.exists()) {
            file.mkdirs();
        }


        fileName =  root.getAbsolutePath() + "/ShaikhBakryAyoub/"+"/Audios/"+classSelection+"_"+level+"_" + currentUID+".mp3";
        Log.d("filename",fileName);

        Uri filePath = Uri.fromFile(new File(fileName));


        if(fileName!=null){
//            InputStream stream = new FileInputStream(new File(fileName));
//            System.out.println("Is the file exist?: "+fileName);

            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("audio/mpeg").build();

            final StorageReference storeRef = storageReference.child("submission/"+classSelection+"/"+level+"/"+currentUID);

            //.child(filePath.getLastPathSegment())
            UploadTask uploadTask = storeRef.putFile(filePath,metadata);
            //uploadTask = storeRef.child("audio/"+file.getLas)

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            submissionUrl = uri;


                            String class_name;

                            class_name=specialClassNameConverter(classSelection,level);
                            System.out.println(class_name);

                            final String class_name_ = class_name;


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");

                            databaseReference.child("submission_link").setValue(submissionUrl.toString());
                            databaseReference.child("submission_status").setValue("Submitted");

                            //Update to Submission child in Database
                            DatabaseReference nameRef = database.getReference().child("Users").child(currentUID).child("name");
                            nameRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.getValue(String.class);
                                    System.out.println(name);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String email = mCurrentUser.getEmail();

                                    submissionDatabase = database.getReference().child("Submission").child(class_name_).child(currentUID);
                                    submissionDatabase.child("submission_status").setValue("Submitted");
                                    submissionDatabase.child("submission_rating").setValue("Not rated");
                                    submissionDatabase.child("submission_comment").setValue("No comment");
                                    submissionDatabase.child("submission_link").setValue(submissionUrl.toString());
                                    submissionDatabase.child("uid").setValue(currentUID);
                                    submissionDatabase.child("email").setValue(email);
                                    submissionDatabase.child("name").setValue(name);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

    public String specialClassNameConverter(String classSelection, String level){
        String new_class_name;

        new_class_name = "nurul_bayan_"+level.toLowerCase();

        return new_class_name;
    }

    //Not here
    private void prepareforRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);
    }

    private void startRecording() {

        //get user id
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUID = mCurrentUser.getUid();
        haveRecord=true;

        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/ShaikhBakryAyoub/"+classSelection+"/"+level+"/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }



        fileName =  root.getAbsolutePath() + "/ShaikhBakryAyoub/"+"/Audios/"+classSelection+"_"+level+"_" + currentUID+".mp3";
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        //seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    //Not here
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

    //Not here
    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlay.setVisibility(View.VISIBLE);
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
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        Toast.makeText(getActivity(), "Recording saved successfully.", Toast.LENGTH_SHORT).show();
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

        /** moving the track as per the seekBar's position**/
        //seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        //           @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if( mPlayer!=null && fromUser ){
//                    //here the track's progress is being changed as per the progress bar
//                    mPlayer.seekTo(progress);
//                    //timer is being updated as per the progress of the seekbar
//                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
//                    lastProgress = progress;
//                }
//            }

//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            seekUpdation();
//        }
//    };

//    private void seekUpdation() {
//        if(mPlayer != null){
//            int mCurrentPosition = mPlayer.getCurrentPosition() ;
//            seekBar.setProgress(mCurrentPosition);
//            lastProgress = mCurrentPosition;
//        }
//        mHandler.postDelayed(runnable, 100);
//    }

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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
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
}
