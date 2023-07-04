package com.example.sbaapp.AssessmentFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sbaapp.ClassVideoPackage.VideoList;
import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FragmentAssessmentKhat extends Fragment {

    private ImageView imageView, feedbackImageView;
    private String classSelection, subClassSelection, level;
    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewSubmissionComment,textViewTaskQuestion;
    private Toolbar toolbarQuestion;
    private Button toggleQuestionBtn;
    private RatingBar ratingBar;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_assessment_khat,container,false);



        textViewSubmissionStatus = (TextView) view.findViewById(R.id.textViewSubmissionStatus);
        textViewSubmissionRate = (TextView) view.findViewById(R.id.textViewSubmissionRate);
        textViewSubmissionComment = (TextView) view.findViewById(R.id.textViewSubmissionComment);
        textViewTaskQuestion =view.findViewById(R.id.textViewTaskQuestion);
        imageView = (ImageView) view.findViewById(R.id.previewSubmission1);
        feedbackImageView = (ImageView) view.findViewById(R.id.feedbackImageView);
        ratingBar =view.findViewById(R.id.ratingBar);

        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Bundle from Submission class
        classSelection = this.getArguments().getString("class");
        subClassSelection = this.getArguments().getString("subClass");
        level = this.getArguments().getString("level");
        if(this.getArguments().containsKey("uid")){
            currentUID = this.getArguments().getString("uid");
        }



        if(level.equals("Intermediate"))
            textViewTaskQuestion.setText(R.string.question_khat_intermediate);
        else if(level.equals("Beginner"))
            textViewTaskQuestion.setText(R.string.question_khat_beginner);
        else
            textViewTaskQuestion.setText(R.string.question_khat_advanced);

        //set Firebase access storage
        storageReference = FirebaseStorage.getInstance().getReference();

        String class_name = specialClassNameConverter(classSelection,subClassSelection,level);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewSubmissionStatus.setText(dataSnapshot.child("submission_status").getValue().toString());
                textViewSubmissionRate.setText(dataSnapshot.child("submission_rating").getValue().toString());
                textViewSubmissionComment.setText(dataSnapshot.child("submission_comment").getValue().toString());


                Glide.with(FragmentAssessmentKhat.this)
                        .load(Uri.parse(dataSnapshot.child("submission_link").getValue().toString()))
                        .override(300,200)
                        .into(imageView);

                if(!dataSnapshot.child("submission_rating").getValue().toString().equals("Not rated")){
                    ratingBar.setRating(Float.parseFloat(dataSnapshot.child("submission_rating").getValue().toString()));

                    if(dataSnapshot.hasChild("submission_link_feedback")){
                        if(!dataSnapshot.child("submission_link_feedback").getValue().toString().equals(null)) {
                            feedbackImageView.setVisibility(View.VISIBLE);
                            Glide.with(FragmentAssessmentKhat.this)
                                    .load(Uri.parse(dataSnapshot.child("submission_link_feedback").getValue().toString()))
                                    .override(300, 200)
                                    .into(feedbackImageView);
                        }
                    }else{
                        feedbackImageView.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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






    public String specialClassNameConverter(String classSelection, String subClassSelection, String level){
        String new_class_name;

        if(subClassSelection.equals("Diwani Jali")){
            new_class_name = classSelection.toLowerCase()+"_"+"diwani_jali"+"_"+level.toLowerCase();
        }else{
            new_class_name = classSelection.toLowerCase()+"_"+subClassSelection.toLowerCase()+"_"+level.toLowerCase();
        }
        return new_class_name;
    }

}
