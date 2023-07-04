package com.example.sbaapp.AssessmentFragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
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
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class FragmentAssessmentNurulBayan extends Fragment {

    private String classSelection, level;

    private Uri submissionUrl;

    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewSubmissionComment,textViewTaskQuestion;

    private Toolbar toolbarQuestion;
    private Button toggleQuestionBtn;
    private RatingBar ratingBar;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;

    //record audio declaration
    private Chronometer chronometer,chronometerFeedback;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop,imageViewPlayFeedback;
    //private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay, linearLayoutPlayFeedback;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null, fileNameFeedback=null;
    private static String mediaUrl;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_assessment_nurul_bayan,container,false);


        linearLayoutPlayFeedback = view.findViewById(R.id.linearLayoutPlayFeedback);

        ratingBar = view.findViewById(R.id.ratingBar);

        chronometer = (Chronometer) view.findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometerFeedback = (Chronometer) view.findViewById(R.id.chronometerTimer);
        chronometerFeedback.setBase(SystemClock.elapsedRealtime());

        textViewSubmissionStatus = view.findViewById(R.id.textViewSubmissionStatus);
        textViewSubmissionRate = view.findViewById(R.id.textViewSubmissionRate);
        textViewSubmissionComment = view.findViewById(R.id.textViewSubmissionComment);
        textViewTaskQuestion = view.findViewById(R.id.textViewTaskQuestion);
        imageViewPlay = view.findViewById(R.id.imageViewPlay);
        imageViewPlayFeedback = view.findViewById(R.id.imageViewPlayFeedback);
        imageViewStop = view.findViewById(R.id.imageViewStop);

        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        classSelection = this.getArguments().getString("class");
        level = this.getArguments().getString("level");
        if(this.getArguments().containsKey("uid")){
            currentUID = this.getArguments().getString("uid");
        }


        if(level.equals("Beginner"))
            textViewTaskQuestion.setText(R.string.question_nurul_bayan_beginner);
        else if(level.equals("Intermediate"))
            textViewTaskQuestion.setText(R.string.question_nurul_bayan_intermediate);
        else if(level.equals("Advanced"))
            textViewTaskQuestion.setText(R.string.question_nurul_bayan_advanced);



        String class_name;

        class_name=specialClassNameConverter(classSelection,level);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fileName = dataSnapshot.child("submission_link").getValue().toString();
                textViewSubmissionStatus.setText(dataSnapshot.child("submission_status").getValue().toString());
                textViewSubmissionRate.setText(dataSnapshot.child("submission_rating").getValue().toString());
                textViewSubmissionComment.setText(dataSnapshot.child("submission_comment").getValue().toString());


                if(!dataSnapshot.child("submission_rating").getValue().toString().equals("Not rated")){
                    ratingBar.setRating(Float.parseFloat(dataSnapshot.child("submission_rating").getValue().toString()));

                    if(dataSnapshot.hasChild("submission_link_feedback")){
                        if(!dataSnapshot.child("submission_link_feedback").getValue().toString().equals(null)) {
                            linearLayoutPlayFeedback.setVisibility(View.VISIBLE);
                            fileNameFeedback = dataSnapshot.child("submission_link_feedback").getValue().toString();
                        }
                    }else{
                        linearLayoutPlayFeedback.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        imageViewPlayFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !isPlaying && fileNameFeedback != null ){
                    isPlaying = true;
                    startPlayingFeedback();
                }else{
                    isPlaying = false;
                    stopPlayingFeedback();
                }
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

    private void startPlayingFeedback() {
        mPlayer = new MediaPlayer();
        try {
            //fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileNameFeedback);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlayFeedback.setImageResource(R.drawable.ic_pause);

        //seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        //seekBar.setMax(mPlayer.getDuration());
        //seekUpdation();
        chronometerFeedback.setBase(SystemClock.elapsedRealtime());
        chronometerFeedback.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlayFeedback.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometerFeedback.stop();
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

    private void stopPlayingFeedback() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlayFeedback.setImageResource(R.drawable.ic_play);
        chronometerFeedback.stop();
    }

    public String specialClassNameConverter(String classSelection, String level){
        String new_class_name;

        new_class_name = "nurul_bayan_"+level.toLowerCase();

        return new_class_name;
    }

}
