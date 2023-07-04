package com.example.sbaapp.ClassVideoPackage;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sbaapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ClassVideo extends AppCompatActivity {

    private StorageReference mStorageRef;
    private VideoView classVideoView;
    private ImageButton playBtn;
    private TextView currTimer, durationTimer, textViewTitle, textViewDescription;

    private ProgressBar videoProgress;
    private ProgressBar bufferProgress;

    private String videoUrl;

    private boolean isPlaying=false;

    private int current =0;
    private int duration = 0;

    private Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_video);

        //Get Bundle URL from VideoList & SubClassList(Demo)
        Bundle bundle = getIntent().getExtras();
        videoUrl = bundle.getString("video_url");
        //Setting Title and Description
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewTitle.setText(bundle.getString("video_title"));
        textViewDescription.setText(bundle.getString("video_description"));


        //Database
        mStorageRef = FirebaseStorage.getInstance().getReference();

        classVideoView = (VideoView) findViewById(R.id.classVideoView);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        videoProgress = (ProgressBar) findViewById(R.id.videoProgress);
        videoProgress.setMax(100);
        currTimer = (TextView) findViewById(R.id.currTimer);

        durationTimer = (TextView) findViewById(R.id.durationTimer);
        bufferProgress =(ProgressBar) findViewById(R.id.bufferProgress);

        bufferProgress.setIndeterminate(false);

        videoUri = Uri.parse(videoUrl);

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
                duration=mp.getDuration()/1000;
                String durationString = String.format("%02d:%02d",duration/60,duration%60);
                durationTimer.setText(durationString);
            }
        });
        classVideoView.start();
        isPlaying=true;
        playBtn.setImageResource(R.drawable.ic_pause);

        new VideoProgress().execute();

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

    public class VideoProgress extends AsyncTask<Void, Integer,Void>{
        protected  Void doInBackground(Void... voids){

            do{
                if(isPlaying) {

                    current = classVideoView.getCurrentPosition() / 1000;
                    publishProgress(current);
                }
            }while(videoProgress.getProgress()<=100);
            return null;
        }

        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            try{
                int currentPercent = values[0] *100/duration;
                videoProgress.setProgress(currentPercent);

                String currString = String.format("%02d:%02d", values[0]/60, values[0]%60);

                currTimer.setText(currString);
            }catch(Exception e){

            }



        }
    }

    @Override
    public void onBackPressed(){
        classVideoView.stopPlayback();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
