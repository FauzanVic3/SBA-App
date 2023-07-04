package com.example.sbaapp.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class UserDashboardActivity extends AppCompatActivity {

    private Switch switchNurulBayan, switchKhat, switchKhatRiqah, switchKhatNasakh, switchKhatFarisi, switchKhatDiwani, switchKhatDiwaniJali,switchKhatThuluth;

    private String classSelection, subClassSelection, level;

    private LinearLayout mLinearLayoutNurulBayan,mLinearLayoutHeaderNurulBayan, mLinearLayoutKhat,mLinearLayoutHeaderKhat,
                         mLinearLayoutKhatRiqah, mLinearLayoutHeaderKhatRiqah, mLinearLayoutKhatNasakh, mLinearLayoutHeaderKhatNasakh,
                         mLinearLayoutKhatFarisi, mLinearLayoutHeaderKhatFarisi, mLinearLayoutKhatDiwani, mLinearLayoutHeaderKhatDiwani,
                         mLinearLayoutKhatDiwaniJali, mLinearLayoutHeaderKhatDiwaniJali, mLinearLayoutKhatThuluth, mLinearLayoutHeaderKhatThuluth;
    private TextView
            textViewNurulBayanBeginnerSubmissionStatus, textViewNurulBayanBeginnerSubmissionRate, textViewNurulBayanBeginnerSubmissionComment,
            textViewNurulBayanIntermediateSubmissionStatus, textViewNurulBayanIntermediateSubmissionRate, textViewNurulBayanIntermediateSubmissionComment,
            textViewNurulBayanAdvancedSubmissionStatus, textViewNurulBayanAdvancedSubmissionRate, textViewNurulBayanAdvancedSubmissionComment,
            textViewKhatRiqahBeginnerSubmissionStatus, textViewKhatRiqahBeginnerSubmissionRate, textViewKhatRiqahBeginnerSubmissionComment,
            textViewKhatRiqahIntermediateSubmissionStatus, textViewKhatRiqahIntermediateSubmissionRate, textViewKhatRiqahIntermediateSubmissionComment
            , textViewKhatRiqahAdvancedSubmissionStatus, textViewKhatRiqahAdvancedSubmissionRate, textViewKhatRiqahAdvancedSubmissionComment,
            textViewKhatNasakhBeginnerSubmissionStatus,textViewKhatNasakhBeginnerSubmissionRate,textViewKhatNasakhBeginnerSubmissionComment,
            textViewKhatNasakhIntermediateSubmissionStatus,textViewKhatNasakhIntermediateSubmissionRate,textViewKhatNasakhIntermediateSubmissionComment,
            textViewKhatNasakhAdvancedSubmissionStatus,textViewKhatNasakhAdvancedSubmissionRate,textViewKhatNasakhAdvancedSubmissionComment,
            textViewKhatFarisiBeginnerSubmissionStatus,textViewKhatFarisiBeginnerSubmissionRate,textViewKhatFarisiBeginnerSubmissionComment,
            textViewKhatFarisiIntermediateSubmissionStatus,textViewKhatFarisiIntermediateSubmissionRate,textViewKhatFarisiIntermediateSubmissionComment,
            textViewKhatFarisiAdvancedSubmissionStatus,textViewKhatFarisiAdvancedSubmissionRate,textViewKhatFarisiAdvancedSubmissionComment
            ,textViewKhatDiwaniBeginnerSubmissionStatus,textViewKhatDiwaniBeginnerSubmissionRate,textViewKhatDiwaniBeginnerSubmissionComment,
            textViewKhatDiwaniIntermediateSubmissionStatus,textViewKhatDiwaniIntermediateSubmissionRate,textViewKhatDiwaniIntermediateSubmissionComment,
            textViewKhatDiwaniAdvancedSubmissionStatus,textViewKhatDiwaniAdvancedSubmissionRate,textViewKhatDiwaniAdvancedSubmissionComment,
            textViewKhatDiwaniJaliBeginnerSubmissionStatus,textViewKhatDiwaniJaliBeginnerSubmissionRate,textViewKhatDiwaniJaliBeginnerSubmissionComment,
            textViewKhatDiwaniJaliIntermediateSubmissionStatus,textViewKhatDiwaniJaliIntermediateSubmissionRate,textViewKhatDiwaniJaliIntermediateSubmissionComment,
            textViewKhatDiwaniJaliAdvancedSubmissionStatus,textViewKhatDiwaniJaliAdvancedSubmissionRate,textViewKhatDiwaniJaliAdvancedSubmissionComment,
            textViewKhatThuluthBeginnerSubmissionStatus,textViewKhatThuluthBeginnerSubmissionRate,textViewKhatThuluthBeginnerSubmissionComment,
            textViewKhatThuluthIntermediateSubmissionStatus,textViewKhatThuluthIntermediateSubmissionRate,textViewKhatThuluthIntermediateSubmissionComment,
            textViewKhatThuluthAdvancedSubmissionStatus,textViewKhatThuluthAdvancedSubmissionRate,textViewKhatThuluthAdvancedSubmissionComment,
            progressTVNB, progressTVRiqah, progressTVNasakh, progressTVFarisi, progressTVDiwani, progressTVDiwaniJali, progressTVThuluth, progressTVKhat;
    private String NurulBayanBeginnerSubmissionURL, NurulBayanIntermediateSubmissionURL, NurulBayanAdvancedSubmissionURL,
            RiqahBeginnerSubmissionURL, RiqahIntermediateSubmissionURL, RiqahAdvancedSubmissionURL,
            NasakhBeginnerSubmissionURL, NasakhIntermediateSubmissionURL, NasakhAdvancedSubmissionURL,
            FarisiBeginnerSubmissionURL, FarisiIntermediateSubmissionURL, FarisiAdvancedSubmissionURL,
            DiwaniBeginnerSubmissionURL, DiwaniIntermediateSubmissionURL, DiwaniAdvancedSubmissionURL,
            DiwaniJaliBeginnerSubmissionURL,DiwaniJaliIntermediateSubmissionURL,DiwaniJaliAdvancedSubmissionURL,
            ThuluthBeginnerSubmissionURL,ThuluthIntermediateSubmissionURL,ThuluthAdvancedSubmissionURL;

    private Button openAssessmentBtnNurulBayanBeginner, openAssessmentBtnNurulBayanIntermediate, openAssessmentBtnNurulBayanAdvanced,
                    openAssessmentBtnKhatRiqahBeginner,openAssessmentBtnKhatRiqahIntermediate,openAssessmentBtnKhatRiqahAdvanced,
                    openAssessmentBtnKhatNasakhBeginner,openAssessmentBtnKhatNasakhIntermediate,openAssessmentBtnKhatNasakhAdvanced,
                    openAssessmentBtnKhatFarisiBeginner,openAssessmentBtnKhatFarisiIntermediate,openAssessmentBtnKhatFarisiAdvanced,
                    openAssessmentBtnKhatDiwaniBeginner,openAssessmentBtnKhatDiwaniIntermediate,openAssessmentBtnKhatDiwaniAdvanced,
                    openAssessmentBtnKhatDiwaniJaliBeginner,openAssessmentBtnKhatDiwaniJaliIntermediate,openAssessmentBtnKhatDiwaniJaliAdvanced,
                    openAssessmentBtnKhatThuluthBeginner,openAssessmentBtnKhatThuluthIntermediate,openAssessmentBtnKhatThuluthAdvanced;


    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;

    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //Handles animation for Nurul Bayan
        //Switch Nurul Bayan
        switchNurulBayan = (Switch) findViewById(R.id.switchNurulBayan);
        switchNurulBayan.setChecked(false);

        mLinearLayoutNurulBayan = (LinearLayout) findViewById(R.id.expandableNurulBayan);
        //set visibility to GONE
        mLinearLayoutNurulBayan.setVisibility(View.GONE);
        mLinearLayoutHeaderNurulBayan = (LinearLayout) findViewById(R.id.headerNurulBayan);
        mLinearLayoutHeaderNurulBayan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutNurulBayan.getVisibility()==View.GONE){
                    switchNurulBayan.setChecked(true);
                    expand(mLinearLayoutNurulBayan);
                    mLinearLayoutHeaderKhat.setVisibility(View.GONE);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutNurulBayan);
                    mLinearLayoutHeaderKhat.setVisibility(View.VISIBLE);
                }
            }
        });


        switchNurulBayan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutNurulBayan.getVisibility()==View.GONE){
                    expand(mLinearLayoutNurulBayan);
                    mLinearLayoutHeaderKhat.setVisibility(View.GONE);
                }else{
                    collapse(mLinearLayoutNurulBayan);
                    mLinearLayoutHeaderKhat.setVisibility(View.VISIBLE);
                }
            }
        });

        //setup views for Nurul Bayan && Khat
        setupViews();
        setupNurulBayanView();
        setupKhatView();
        extractDatabase();


        //Handles data extraction from Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mCurrentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(uid)
                .child("class_details");




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Extracting Nurul Bayan Beginner data
                DataSnapshot nurul_bayan_beginner = dataSnapshot.child("nurul_bayan_beginner").child("0");
                textViewNurulBayanBeginnerSubmissionStatus.setText(nurul_bayan_beginner.child("submission_status").getValue().toString());
                textViewNurulBayanBeginnerSubmissionRate.setText(nurul_bayan_beginner.child("submission_rating").getValue().toString());
                textViewNurulBayanBeginnerSubmissionComment.setText(nurul_bayan_beginner.child("submission_comment").getValue().toString());
                NurulBayanBeginnerSubmissionURL = nurul_bayan_beginner.child("submission_link").getValue().toString();


                DataSnapshot nurul_bayan_intermediate = dataSnapshot.child("nurul_bayan_intermediate").child("0");
                textViewNurulBayanIntermediateSubmissionStatus.setText(nurul_bayan_intermediate.child("submission_status").getValue().toString());
                textViewNurulBayanIntermediateSubmissionRate.setText(nurul_bayan_intermediate.child("submission_rating").getValue().toString());
                textViewNurulBayanIntermediateSubmissionComment.setText(nurul_bayan_intermediate.child("submission_comment").getValue().toString());
                NurulBayanIntermediateSubmissionURL = nurul_bayan_intermediate.child("submission_link").getValue().toString();

                DataSnapshot nurul_bayan_advanced = dataSnapshot.child("nurul_bayan_advanced").child("0");
                textViewNurulBayanAdvancedSubmissionStatus.setText(nurul_bayan_advanced.child("submission_status").getValue().toString());
                textViewNurulBayanAdvancedSubmissionRate.setText(nurul_bayan_advanced.child("submission_rating").getValue().toString());
                textViewNurulBayanAdvancedSubmissionComment.setText(nurul_bayan_advanced.child("submission_comment").getValue().toString());
                NurulBayanAdvancedSubmissionURL = nurul_bayan_advanced.child("submission_link").getValue().toString();

                //Extracting khat data
                //Riqah beginner
                DataSnapshot riqah_beginner = dataSnapshot.child("khat_riqah_beginner").child("0");
                textViewKhatRiqahBeginnerSubmissionStatus.setText(riqah_beginner.child("submission_status").getValue().toString());
                textViewKhatRiqahBeginnerSubmissionRate.setText(riqah_beginner.child("submission_rating").getValue().toString());
                textViewKhatRiqahBeginnerSubmissionComment.setText(riqah_beginner.child("submission_comment").getValue().toString());
                RiqahBeginnerSubmissionURL = riqah_beginner.child("submission_link").getValue().toString();

                //Riqah intermediate
                DataSnapshot riqah_intermediate = dataSnapshot.child("khat_riqah_intermediate").child("0");
                textViewKhatRiqahIntermediateSubmissionStatus.setText(riqah_intermediate.child("submission_status").getValue().toString());
                textViewKhatRiqahIntermediateSubmissionRate.setText(riqah_intermediate.child("submission_rating").getValue().toString());
                textViewKhatRiqahIntermediateSubmissionComment.setText(riqah_intermediate.child("submission_comment").getValue().toString());
                RiqahIntermediateSubmissionURL = riqah_intermediate.child("submission_link").getValue().toString();

                //Riqah advanced
                DataSnapshot riqah_advanced = dataSnapshot.child("khat_riqah_advanced").child("0");
                textViewKhatRiqahAdvancedSubmissionStatus.setText(riqah_advanced.child("submission_status").getValue().toString());
                textViewKhatRiqahAdvancedSubmissionRate.setText(riqah_advanced.child("submission_rating").getValue().toString());
                textViewKhatRiqahAdvancedSubmissionComment.setText(riqah_advanced.child("submission_comment").getValue().toString());
                RiqahAdvancedSubmissionURL = riqah_advanced.child("submission_link").getValue().toString();



                //Nasakh beginner
                DataSnapshot nasakh_beginner = dataSnapshot.child("khat_nasakh_beginner").child("0");
                textViewKhatNasakhBeginnerSubmissionStatus.setText(nasakh_beginner.child("submission_status").getValue().toString());
                textViewKhatNasakhBeginnerSubmissionRate.setText(nasakh_beginner.child("submission_rating").getValue().toString());
                textViewKhatNasakhBeginnerSubmissionComment.setText(nasakh_beginner.child("submission_comment").getValue().toString());
                NasakhBeginnerSubmissionURL = nasakh_beginner.child("submission_link").getValue().toString();

                //Nasakh intermediate
                DataSnapshot nasakh_intermediate = dataSnapshot.child("khat_nasakh_intermediate").child("0");
                textViewKhatNasakhIntermediateSubmissionStatus.setText(nasakh_intermediate.child("submission_status").getValue().toString());
                textViewKhatNasakhIntermediateSubmissionRate.setText(nasakh_intermediate.child("submission_rating").getValue().toString());
                textViewKhatNasakhIntermediateSubmissionComment.setText(nasakh_intermediate.child("submission_comment").getValue().toString());
                NasakhIntermediateSubmissionURL = nasakh_intermediate.child("submission_link").getValue().toString();

                //Nasakh advanced
                DataSnapshot nasakh_advanced = dataSnapshot.child("khat_nasakh_advanced").child("0");
                textViewKhatNasakhAdvancedSubmissionStatus.setText(nasakh_advanced.child("submission_status").getValue().toString());
                textViewKhatNasakhAdvancedSubmissionRate.setText(nasakh_advanced.child("submission_rating").getValue().toString());
                textViewKhatNasakhAdvancedSubmissionComment.setText(nasakh_advanced.child("submission_comment").getValue().toString());
                NasakhAdvancedSubmissionURL = nasakh_advanced.child("submission_link").getValue().toString();

                //Farisi beginner
                DataSnapshot farisi_beginner = dataSnapshot.child("khat_farisi_beginner").child("0");
                textViewKhatFarisiBeginnerSubmissionStatus.setText(farisi_beginner.child("submission_status").getValue().toString());
                textViewKhatFarisiBeginnerSubmissionRate.setText(farisi_beginner.child("submission_rating").getValue().toString());
                textViewKhatFarisiBeginnerSubmissionComment.setText(farisi_beginner.child("submission_comment").getValue().toString());
                FarisiBeginnerSubmissionURL = farisi_beginner.child("submission_link").getValue().toString();

                //Farisi intermediate
                DataSnapshot farisi_intermediate = dataSnapshot.child("khat_farisi_intermediate").child("0");
                textViewKhatFarisiIntermediateSubmissionStatus.setText(farisi_intermediate.child("submission_status").getValue().toString());
                textViewKhatFarisiIntermediateSubmissionRate.setText(farisi_intermediate.child("submission_rating").getValue().toString());
                textViewKhatFarisiIntermediateSubmissionComment.setText(farisi_intermediate.child("submission_comment").getValue().toString());
                FarisiIntermediateSubmissionURL = farisi_intermediate.child("submission_link").getValue().toString();

                //Farisi advanced
                DataSnapshot farisi_advanced = dataSnapshot.child("khat_farisi_advanced").child("0");
                textViewKhatFarisiAdvancedSubmissionStatus.setText(farisi_advanced.child("submission_status").getValue().toString());
                textViewKhatFarisiAdvancedSubmissionRate.setText(farisi_advanced.child("submission_rating").getValue().toString());
                textViewKhatFarisiAdvancedSubmissionComment.setText(farisi_advanced.child("submission_comment").getValue().toString());
                FarisiAdvancedSubmissionURL = farisi_advanced.child("submission_link").getValue().toString();

                //Diwani beginner
                DataSnapshot diwani_beginner = dataSnapshot.child("khat_diwani_beginner").child("0");
                textViewKhatDiwaniBeginnerSubmissionStatus.setText(diwani_beginner.child("submission_status").getValue().toString());
                textViewKhatDiwaniBeginnerSubmissionRate.setText(diwani_beginner.child("submission_rating").getValue().toString());
                textViewKhatDiwaniBeginnerSubmissionComment.setText(diwani_beginner.child("submission_comment").getValue().toString());
                DiwaniBeginnerSubmissionURL = diwani_beginner.child("submission_link").getValue().toString();

                //Diwani intermediate
                DataSnapshot diwani_intermediate = dataSnapshot.child("khat_diwani_intermediate").child("0");
                textViewKhatDiwaniIntermediateSubmissionStatus.setText(diwani_intermediate.child("submission_status").getValue().toString());
                textViewKhatDiwaniIntermediateSubmissionRate.setText(diwani_intermediate.child("submission_rating").getValue().toString());
                textViewKhatDiwaniIntermediateSubmissionComment.setText(diwani_intermediate.child("submission_comment").getValue().toString());
                DiwaniIntermediateSubmissionURL = diwani_intermediate.child("submission_link").getValue().toString();

                //Diwani advanced
                DataSnapshot diwani_advanced = dataSnapshot.child("khat_diwani_advanced").child("0");
                textViewKhatDiwaniAdvancedSubmissionStatus.setText(diwani_advanced.child("submission_status").getValue().toString());
                textViewKhatDiwaniAdvancedSubmissionRate.setText(diwani_advanced.child("submission_rating").getValue().toString());
                textViewKhatDiwaniAdvancedSubmissionComment.setText(diwani_advanced.child("submission_comment").getValue().toString());
                DiwaniAdvancedSubmissionURL = diwani_advanced.child("submission_link").getValue().toString();

                //DiwaniJali beginner
                DataSnapshot diwani_jali_beginner = dataSnapshot.child("khat_diwani_jali_beginner").child("0");
                textViewKhatDiwaniJaliBeginnerSubmissionStatus.setText(diwani_jali_beginner.child("submission_status").getValue().toString());
                textViewKhatDiwaniJaliBeginnerSubmissionRate.setText(diwani_jali_beginner.child("submission_rating").getValue().toString());
                textViewKhatDiwaniJaliBeginnerSubmissionComment.setText(diwani_jali_beginner.child("submission_comment").getValue().toString());
                DiwaniJaliBeginnerSubmissionURL = diwani_jali_beginner.child("submission_link").getValue().toString();

                //DiwaniJali intermediate
                DataSnapshot diwani_jali_intermediate = dataSnapshot.child("khat_diwani_jali_intermediate").child("0");
                textViewKhatDiwaniJaliIntermediateSubmissionStatus.setText(diwani_jali_intermediate.child("submission_status").getValue().toString());
                textViewKhatDiwaniJaliIntermediateSubmissionRate.setText(diwani_jali_intermediate.child("submission_rating").getValue().toString());
                textViewKhatDiwaniJaliIntermediateSubmissionComment.setText(diwani_jali_intermediate.child("submission_comment").getValue().toString());
                DiwaniJaliIntermediateSubmissionURL = diwani_jali_intermediate.child("submission_link").getValue().toString();

                //DiwaniJali advanced
                DataSnapshot diwani_jali_advanced = dataSnapshot.child("khat_diwani_jali_advanced").child("0");
                textViewKhatDiwaniJaliAdvancedSubmissionStatus.setText(diwani_jali_advanced.child("submission_status").getValue().toString());
                textViewKhatDiwaniJaliAdvancedSubmissionRate.setText(diwani_jali_advanced.child("submission_rating").getValue().toString());
                textViewKhatDiwaniJaliAdvancedSubmissionComment.setText(diwani_jali_advanced.child("submission_comment").getValue().toString());
                DiwaniJaliAdvancedSubmissionURL = diwani_jali_advanced.child("submission_link").getValue().toString();

                //Thuluth beginner
                DataSnapshot thuluth_beginner = dataSnapshot.child("khat_thuluth_beginner").child("0");
                textViewKhatThuluthBeginnerSubmissionStatus.setText(thuluth_beginner.child("submission_status").getValue().toString());
                textViewKhatThuluthBeginnerSubmissionRate.setText(thuluth_beginner.child("submission_rating").getValue().toString());
                textViewKhatThuluthBeginnerSubmissionComment.setText(thuluth_beginner.child("submission_comment").getValue().toString());
                ThuluthBeginnerSubmissionURL = thuluth_beginner.child("submission_link").getValue().toString();

                //Thuluth intermediate
                DataSnapshot thuluth_intermediate = dataSnapshot.child("khat_thuluth_intermediate").child("0");
                textViewKhatThuluthIntermediateSubmissionStatus.setText(thuluth_intermediate.child("submission_status").getValue().toString());
                textViewKhatThuluthIntermediateSubmissionRate.setText(thuluth_intermediate.child("submission_rating").getValue().toString());
                textViewKhatThuluthIntermediateSubmissionComment.setText(thuluth_intermediate.child("submission_comment").getValue().toString());
                ThuluthIntermediateSubmissionURL = thuluth_intermediate.child("submission_link").getValue().toString();

                //Thuluth advanced
                DataSnapshot thuluth_advanced = dataSnapshot.child("khat_thuluth_advanced").child("0");
                textViewKhatThuluthAdvancedSubmissionStatus.setText(thuluth_advanced.child("submission_status").getValue().toString());
                textViewKhatThuluthAdvancedSubmissionRate.setText(thuluth_advanced.child("submission_rating").getValue().toString());
                textViewKhatThuluthAdvancedSubmissionComment.setText(thuluth_advanced.child("submission_comment").getValue().toString());
                ThuluthAdvancedSubmissionURL = thuluth_advanced.child("submission_link").getValue().toString();



                //If user wants to see the submitted item
                openAssessmentBtnNurulBayanBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NurulBayanBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
//                        else if(textViewNurulBayanBeginnerSubmissionRate.equals("Not rated")){
//                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
//                        }
                        else{
                            classSelection = "Nurul Bayan";
                            subClassSelection = null;
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnNurulBayanIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NurulBayanIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Nurul Bayan";
                            subClassSelection = null;
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnNurulBayanAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NurulBayanAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Nurul Bayan";
                            subClassSelection = null;
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatRiqahBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(RiqahBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Riqah";
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatRiqahIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(RiqahIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Riqah";
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatRiqahAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(RiqahAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Riqah";
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });
                openAssessmentBtnKhatNasakhBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NasakhBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Nasakh";
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatNasakhIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NasakhIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Nasakh";
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatNasakhAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NasakhAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Nasakh";
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatFarisiBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(FarisiBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Farisi";
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatFarisiIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(FarisiIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Farisi";
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatFarisiAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(FarisiAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Farisi";
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatDiwaniBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DiwaniBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Diwani";
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatDiwaniIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DiwaniIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Diwani";
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatDiwaniAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DiwaniAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Diwani";
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatDiwaniJaliBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DiwaniJaliBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Diwani Jali";
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatDiwaniJaliIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DiwaniJaliIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Diwani Jali";
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatDiwaniJaliAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DiwaniJaliAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Diwani Jali";
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatThuluthBeginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ThuluthBeginnerSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Thuluth";
                            level = "Beginner";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatThuluthIntermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ThuluthIntermediateSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Thuluth";
                            level = "Intermediate";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

                openAssessmentBtnKhatThuluthAdvanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ThuluthAdvancedSubmissionURL.equals("No link")){
                            Toast.makeText(UserDashboardActivity.this, "No submission for this assessment", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            classSelection = "Khat";
                            subClassSelection = "Thuluth";
                            level = "Advanced";

                            Bundle bundle = new Bundle();
                            bundle.putString("class", classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level",level);

                            Intent intent = new Intent(UserDashboardActivity.this, UserDashboardItemView.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Khat section
        //Handles animation for Khat
        //Switch Khat
        switchKhat = (Switch) findViewById(R.id.switchKhat);
        switchKhat.setChecked(false);

        mLinearLayoutKhat = (LinearLayout) findViewById(R.id.expandableKhat);
        //set visibility to GONE
        mLinearLayoutKhat.setVisibility(View.GONE);
        mLinearLayoutHeaderKhat = (LinearLayout) findViewById(R.id.headerKhat);
        mLinearLayoutHeaderKhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhat.getVisibility()==View.GONE){
                    switchKhat.setChecked(true);
                    expand(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.GONE);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.VISIBLE);
                }
            }
        });


        switchKhat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhat.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.GONE);
                }else{
                    collapse(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.VISIBLE);
                }
            }
        });
        //END OF KHAT

        //RIQAH START
        switchKhatRiqah = (Switch) findViewById(R.id.switchKhatRiqah);
        switchKhatRiqah.setChecked(false);

        mLinearLayoutKhatRiqah = (LinearLayout) findViewById(R.id.expandableKhatRiqah);
        //set visibility to GONE
        mLinearLayoutKhatRiqah.setVisibility(View.GONE);
        mLinearLayoutHeaderKhatRiqah = (LinearLayout) findViewById(R.id.headerKhatRiqah);
        mLinearLayoutHeaderKhatRiqah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhatRiqah.getVisibility()==View.GONE){
                    switchKhatRiqah.setChecked(true);
                    expand(mLinearLayoutKhatRiqah);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhatRiqah);
                }
            }
        });


        switchKhatRiqah.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhatRiqah.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhatRiqah);
                }else{
                    collapse(mLinearLayoutKhatRiqah);
                }
            }
        });
        //RIQAH END

        //NASAKH START
        switchKhatNasakh = (Switch) findViewById(R.id.switchKhatNasakh);
        switchKhatNasakh.setChecked(false);

        mLinearLayoutKhatNasakh = (LinearLayout) findViewById(R.id.expandableKhatNasakh);
        //set visibility to GONE
        mLinearLayoutKhatNasakh.setVisibility(View.GONE);
        mLinearLayoutHeaderKhatNasakh = (LinearLayout) findViewById(R.id.headerKhatNasakh);
        mLinearLayoutHeaderKhatNasakh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhatNasakh.getVisibility()==View.GONE){
                    switchKhatNasakh.setChecked(true);
                    expand(mLinearLayoutKhatNasakh);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhatNasakh);
                }
            }
        });


        switchKhatNasakh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhatNasakh.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhatNasakh);
                }else{
                    collapse(mLinearLayoutKhatNasakh);
                }
            }
        });
        //NASAKH END

        //FARISI START
        switchKhatFarisi = (Switch) findViewById(R.id.switchKhatFarisi);
        switchKhatFarisi.setChecked(false);

        mLinearLayoutKhatFarisi = (LinearLayout) findViewById(R.id.expandableKhatFarisi);
        //set visibility to GONE
        mLinearLayoutKhatFarisi.setVisibility(View.GONE);
        mLinearLayoutHeaderKhatFarisi = (LinearLayout) findViewById(R.id.headerKhatFarisi);
        mLinearLayoutHeaderKhatFarisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhatFarisi.getVisibility()==View.GONE){
                    switchKhatFarisi.setChecked(true);
                    expand(mLinearLayoutKhatFarisi);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhatFarisi);
                }
            }
        });


        switchKhatFarisi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhatFarisi.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhatFarisi);
                }else{
                    collapse(mLinearLayoutKhatFarisi);
                }
            }
        });
        //FARISI END

        //DIWANI START
        switchKhatDiwani = (Switch) findViewById(R.id.switchKhatDiwani);
        switchKhatDiwani.setChecked(false);

        mLinearLayoutKhatDiwani = (LinearLayout) findViewById(R.id.expandableKhatDiwani);
        //set visibility to GONE
        mLinearLayoutKhatDiwani.setVisibility(View.GONE);
        mLinearLayoutHeaderKhatDiwani = (LinearLayout) findViewById(R.id.headerKhatDiwani);
        mLinearLayoutHeaderKhatDiwani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhatDiwani.getVisibility()==View.GONE){
                    switchKhatDiwani.setChecked(true);
                    expand(mLinearLayoutKhatDiwani);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhatDiwani);
                }
            }
        });


        switchKhatDiwani.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhatDiwani.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhatDiwani);
                }else{
                    collapse(mLinearLayoutKhatDiwani);
                }
            }
        });
        //DIWANI END

        //DIWANI JALI START
        switchKhatDiwaniJali = (Switch) findViewById(R.id.switchKhatDiwaniJali);
        switchKhatDiwaniJali.setChecked(false);

        mLinearLayoutKhatDiwaniJali = (LinearLayout) findViewById(R.id.expandableKhatDiwaniJali);
        //set visibility to GONE
        mLinearLayoutKhatDiwaniJali.setVisibility(View.GONE);
        mLinearLayoutHeaderKhatDiwaniJali = (LinearLayout) findViewById(R.id.headerKhatDiwaniJali);
        mLinearLayoutHeaderKhatDiwaniJali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhatDiwaniJali.getVisibility()==View.GONE){
                    switchKhatDiwaniJali.setChecked(true);
                    expand(mLinearLayoutKhatDiwaniJali);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhatDiwaniJali);
                }
            }
        });


        switchKhatDiwaniJali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhatDiwaniJali.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhatDiwaniJali);
                }else{
                    collapse(mLinearLayoutKhatDiwaniJali);
                }
            }
        });
        //DIWANI JALI END

        //THULUTH START
        switchKhatThuluth = (Switch) findViewById(R.id.switchKhatThuluth);
        switchKhatThuluth.setChecked(false);

        mLinearLayoutKhatThuluth = (LinearLayout) findViewById(R.id.expandableKhatThuluth);
        //set visibility to GONE
        mLinearLayoutKhatThuluth.setVisibility(View.GONE);
        mLinearLayoutHeaderKhatThuluth = (LinearLayout) findViewById(R.id.headerKhatThuluth);
        mLinearLayoutHeaderKhatThuluth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhatThuluth.getVisibility()==View.GONE){
                    switchKhatThuluth.setChecked(true);
                    expand(mLinearLayoutKhatThuluth);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhatThuluth);
                }
            }
        });


        switchKhatThuluth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhatThuluth.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhatThuluth);
                }else{
                    collapse(mLinearLayoutKhatThuluth);
                }
            }
        });
        //THULUTH END





    }

    public void extractDatabase(){

        final String[] key = new String[1];

        final int[] count_feedback_nb = { 0 };

        final int[] count_feedback_khat = { 0 };

        final int[] count_feedback_khat_diwani = { 0 };

        final int[] count_feedback_khat_diwani_jali = { 0 };

        final int[] count_feedback_khat_riqah = { 0 };

        final int[] count_feedback_khat_nasakh = { 0 };

        final int[] count_feedback_khat_farisi = { 0 };

        final int[] count_feedback_khat_thuluth = { 0 };

        DatabaseReference nb_b, nb_i, nb_a, k_d_b, k_d_i, k_d_a, k_dj_b, k_dj_i, k_dj_a, k_f_b, k_f_i, k_f_a, k_n_b, k_n_i, k_n_a, k_r_b, k_r_i, k_r_a, k_t_b, k_t_i, k_t_a;

        uid = FirebaseAuth.getInstance().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Submission");
        nb_b = databaseReference.child("nurul_bayan_beginner").child(uid);
        nb_i = databaseReference.child("nurul_bayan_intermediate").child(uid);
        nb_a = databaseReference.child("nurul_bayan_advanced").child(uid);
        k_d_b = databaseReference.child("khat_diwani_beginner").child(uid);
        k_d_i = databaseReference.child("khat_diwani_intermediate").child(uid);
        k_d_a = databaseReference.child("khat_diwani_advanced").child(uid);
        k_dj_b = databaseReference.child("khat_diwani_jali_beginner").child(uid);
        k_dj_i = databaseReference.child("khat_diwani_jali_intermediate").child(uid);
        k_dj_a = databaseReference.child("khat_diwani_jali_advanced").child(uid);
        k_f_b = databaseReference.child("khat_farisi_beginner").child(uid);
        k_f_i = databaseReference.child("khat_farisi_intermediate").child(uid);
        k_f_a = databaseReference.child("khat_farisi_advanced").child(uid);
        k_n_b = databaseReference.child("khat_nasakh_beginner").child(uid);
        k_n_i = databaseReference.child("khat_nasakh_intermediate").child(uid);
        k_n_a = databaseReference.child("khat_nasakh_advanced").child(uid);
        k_r_b = databaseReference.child("khat_riqah_beginner").child(uid);
        k_r_i = databaseReference.child("khat_riqah_intermediate").child(uid);
        k_r_a = databaseReference.child("khat_riqah_advanced").child(uid);
        k_t_b = databaseReference.child("khat_thuluth_beginner").child(uid);
        k_t_i = databaseReference.child("khat_thuluth_intermediate").child(uid);
        k_t_a = databaseReference.child("khat_thuluth_advanced").child(uid);

        //nb
        nb_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println(dataSnapshot.toString());
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                                count_feedback_nb[0]++;
                    }

                System.out.println("Feedback NB Beginner: "+count_feedback_nb[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        nb_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_nb[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        nb_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_nb[0]++;
                    }


                key[0] = "nurul_bayan";
                setTextViewValue(key[0],  count_feedback_nb[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        k_d_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_diwani[0]++;
                        count_feedback_khat[0]++;
                    }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_d_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_diwani[0]++;
                        count_feedback_khat[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_d_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_diwani[0]++;
                        count_feedback_khat[0]++;
                    }


                key[0] = "diwani";
                setTextViewValue(key[0], count_feedback_khat_diwani[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        k_dj_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_diwani_jali[0]++;
                        count_feedback_khat[0]++;
                    }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_dj_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_diwani_jali[0]++;
                        count_feedback_khat[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_dj_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_diwani_jali[0]++;
                        count_feedback_khat[0]++;
                    }


                key[0] = "diwani jali";
                setTextViewValue(key[0], count_feedback_khat_diwani_jali[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        k_f_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_farisi[0]++;
                        count_feedback_khat[0]++;
                    }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_f_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_farisi[0]++;
                        count_feedback_khat[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_f_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_farisi[0]++;
                        count_feedback_khat[0]++;
                    }


                key[0] = "farisi";
                setTextViewValue(key[0], count_feedback_khat_farisi[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        k_n_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_nasakh[0]++;
                        count_feedback_khat[0]++;
                    }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_n_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_nasakh[0]++;
                        count_feedback_khat[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_n_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_nasakh[0]++;
                        count_feedback_khat[0]++;
                    }


                key[0] = "nasakh";
                setTextViewValue(key[0], count_feedback_khat_nasakh[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        k_r_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_riqah[0]++;
                        count_feedback_khat[0]++;
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_r_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_riqah[0]++;
                        count_feedback_khat[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_r_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_riqah[0]++;
                        count_feedback_khat[0]++;
                    }


                key[0] = "riqah";
                setTextViewValue(key[0], count_feedback_khat_riqah[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        k_t_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_thuluth[0]++;
                        count_feedback_khat[0]++;
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_t_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_thuluth[0]++;
                        count_feedback_khat[0]++;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_t_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>1)
                    if (dataSnapshot.child("submission_status").getValue().toString().equals("Evaluated")) {

                        count_feedback_khat_thuluth[0]++;
                        count_feedback_khat[0]++;
                    }


                key[0] = "thuluth";
                setTextViewValue(key[0], count_feedback_khat_thuluth[0]);

                key[0] = "khat";
                setTextViewValue(key[0],count_feedback_khat[0]);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void setTextViewValue(String key, int feedback_count){
        switch(key){

            case "nurul_bayan":
            {

                progressTVNB.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
            case "khat":
            {

                progressTVKhat.setText(Integer.toString(feedback_count)+"/18");
                break;
            }
            case "riqah":
            {

                progressTVRiqah.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
            case "nasakh":
            {

                progressTVNasakh.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
            case "farisi":
            {

                progressTVFarisi.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
            case "diwani":
            {

                progressTVDiwani.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
            case "diwani jali":
            {

                progressTVDiwaniJali.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
            case "thuluth":
            {

                progressTVThuluth.setText(Integer.toString(feedback_count)+"/3");
                break;
            }
        }

    }

    private void expand(LinearLayout mLinearLayout) {
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight(),mLinearLayout);
        mAnimator.start();
    }

    private void collapse(final LinearLayout mLinearLayout) {
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0,mLinearLayout);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //Height=0, but it set visibility to GONE
                mLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }



    private ValueAnimator slideAnimator(int start, int end, final LinearLayout mLinearLayout) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void setupViews(){
        progressTVNB = findViewById(R.id.progressTVNB);
        progressTVKhat = findViewById(R.id.progressTVKhat);
        progressTVRiqah = findViewById(R.id.progressTVRiqah);
        progressTVNasakh = findViewById(R.id.progressTVNasakh);
        progressTVFarisi = findViewById(R.id.progressTVFarisi);
        progressTVDiwani = findViewById(R.id.progressTVDiwani);
        progressTVDiwaniJali = findViewById(R.id.progressTVDiwaniJali);
        progressTVThuluth = findViewById(R.id.progressTVThuluth);
    }

    public void setupNurulBayanView(){
        //Nurul Bayan
        //Beginner
        textViewNurulBayanBeginnerSubmissionStatus =findViewById(R.id.textViewNurulBayanBeginnerSubmissionStatus);
        textViewNurulBayanBeginnerSubmissionRate = findViewById(R.id.textViewNurulBayanBeginnerSubmissionRate);
        textViewNurulBayanBeginnerSubmissionComment = findViewById(R.id.textViewNurulBayanBeginnerSubmissionComment);
        openAssessmentBtnNurulBayanBeginner = findViewById(R.id.openAssessmentBtnNurulBayanBeginner);

        //Intermediate
        textViewNurulBayanIntermediateSubmissionStatus = findViewById(R.id.textViewNurulBayanIntermediateSubmissionStatus);
        textViewNurulBayanIntermediateSubmissionRate = findViewById(R.id.textViewNurulBayanIntermediateSubmissionRate);
        textViewNurulBayanIntermediateSubmissionComment = findViewById(R.id.textViewNurulBayanIntermediateSubmissionComment);
        openAssessmentBtnNurulBayanIntermediate = findViewById(R.id.openAssessmentBtnNurulBayanIntermediate);

        //Advanced
        textViewNurulBayanAdvancedSubmissionStatus = findViewById(R.id.textViewNurulBayanAdvancedSubmissionStatus);
        textViewNurulBayanAdvancedSubmissionRate = findViewById(R.id.textViewNurulBayanAdvancedSubmissionRate);
        textViewNurulBayanAdvancedSubmissionComment = findViewById(R.id.textViewNurulBayanAdvancedSubmissionComment);
        openAssessmentBtnNurulBayanAdvanced = findViewById(R.id.openAssessmentBtnNurulBayanAdvanced);

    }

    public void setupKhatView(){
        //KhatRiqah
        //Beginner
        textViewKhatRiqahBeginnerSubmissionStatus =findViewById(R.id.textViewKhatRiqahBeginnerSubmissionStatus);
        textViewKhatRiqahBeginnerSubmissionRate = findViewById(R.id.textViewKhatRiqahBeginnerSubmissionRate);
        textViewKhatRiqahBeginnerSubmissionComment = findViewById(R.id.textViewKhatRiqahBeginnerSubmissionComment);
        openAssessmentBtnKhatRiqahBeginner = findViewById(R.id.openAssessmentBtnKhatRiqahBeginner);

        //Intermediate
        textViewKhatRiqahIntermediateSubmissionStatus = findViewById(R.id.textViewKhatRiqahIntermediateSubmissionStatus);
        textViewKhatRiqahIntermediateSubmissionRate = findViewById(R.id.textViewKhatRiqahIntermediateSubmissionRate);
        textViewKhatRiqahIntermediateSubmissionComment = findViewById(R.id.textViewKhatRiqahIntermediateSubmissionComment);
        openAssessmentBtnKhatRiqahIntermediate = findViewById(R.id.openAssessmentBtnKhatRiqahIntermediate);

        //Advanced
        textViewKhatRiqahAdvancedSubmissionStatus = findViewById(R.id.textViewKhatRiqahAdvancedSubmissionStatus);
        textViewKhatRiqahAdvancedSubmissionRate = findViewById(R.id.textViewKhatRiqahAdvancedSubmissionRate);
        textViewKhatRiqahAdvancedSubmissionComment = findViewById(R.id.textViewKhatRiqahAdvancedSubmissionComment);
        openAssessmentBtnKhatRiqahAdvanced = findViewById(R.id.openAssessmentBtnKhatRiqahAdvanced);

        //KhatNasakh
        //Beginner
        textViewKhatNasakhBeginnerSubmissionStatus =findViewById(R.id.textViewKhatNasakhBeginnerSubmissionStatus);
        textViewKhatNasakhBeginnerSubmissionRate = findViewById(R.id.textViewKhatNasakhBeginnerSubmissionRate);
        textViewKhatNasakhBeginnerSubmissionComment = findViewById(R.id.textViewKhatNasakhBeginnerSubmissionComment);
        openAssessmentBtnKhatNasakhBeginner = findViewById(R.id.openAssessmentBtnKhatNasakhBeginner);

        //Intermediate
        textViewKhatNasakhIntermediateSubmissionStatus = findViewById(R.id.textViewKhatNasakhIntermediateSubmissionStatus);
        textViewKhatNasakhIntermediateSubmissionRate = findViewById(R.id.textViewKhatNasakhIntermediateSubmissionRate);
        textViewKhatNasakhIntermediateSubmissionComment = findViewById(R.id.textViewKhatNasakhIntermediateSubmissionComment);
        openAssessmentBtnKhatNasakhIntermediate = findViewById(R.id.openAssessmentBtnKhatNasakhIntermediate);

        //Advanced
        textViewKhatNasakhAdvancedSubmissionStatus = findViewById(R.id.textViewKhatNasakhAdvancedSubmissionStatus);
        textViewKhatNasakhAdvancedSubmissionRate = findViewById(R.id.textViewKhatNasakhAdvancedSubmissionRate);
        textViewKhatNasakhAdvancedSubmissionComment = findViewById(R.id.textViewKhatNasakhAdvancedSubmissionComment);
        openAssessmentBtnKhatNasakhAdvanced = findViewById(R.id.openAssessmentBtnKhatNasakhAdvanced);

        //KhatFarisi
        //Beginner
        textViewKhatFarisiBeginnerSubmissionStatus =findViewById(R.id.textViewKhatFarisiBeginnerSubmissionStatus);
        textViewKhatFarisiBeginnerSubmissionRate = findViewById(R.id.textViewKhatFarisiBeginnerSubmissionRate);
        textViewKhatFarisiBeginnerSubmissionComment = findViewById(R.id.textViewKhatFarisiBeginnerSubmissionComment);
        openAssessmentBtnKhatFarisiBeginner = findViewById(R.id.openAssessmentBtnKhatFarisiBeginner);

        //Intermediate
        textViewKhatFarisiIntermediateSubmissionStatus = findViewById(R.id.textViewKhatFarisiIntermediateSubmissionStatus);
        textViewKhatFarisiIntermediateSubmissionRate = findViewById(R.id.textViewKhatFarisiIntermediateSubmissionRate);
        textViewKhatFarisiIntermediateSubmissionComment = findViewById(R.id.textViewKhatFarisiIntermediateSubmissionComment);
        openAssessmentBtnKhatFarisiIntermediate = findViewById(R.id.openAssessmentBtnKhatFarisiIntermediate);

        //Advanced
        textViewKhatFarisiAdvancedSubmissionStatus = findViewById(R.id.textViewKhatFarisiAdvancedSubmissionStatus);
        textViewKhatFarisiAdvancedSubmissionRate = findViewById(R.id.textViewKhatFarisiAdvancedSubmissionRate);
        textViewKhatFarisiAdvancedSubmissionComment = findViewById(R.id.textViewKhatFarisiAdvancedSubmissionComment);
        openAssessmentBtnKhatFarisiAdvanced = findViewById(R.id.openAssessmentBtnKhatFarisiAdvanced);

        //KhatDiwani
        //Beginner
        textViewKhatDiwaniBeginnerSubmissionStatus =findViewById(R.id.textViewKhatDiwaniBeginnerSubmissionStatus);
        textViewKhatDiwaniBeginnerSubmissionRate = findViewById(R.id.textViewKhatDiwaniBeginnerSubmissionRate);
        textViewKhatDiwaniBeginnerSubmissionComment = findViewById(R.id.textViewKhatDiwaniBeginnerSubmissionComment);
        openAssessmentBtnKhatDiwaniBeginner = findViewById(R.id.openAssessmentBtnKhatDiwaniBeginner);

        //Intermediate
        textViewKhatDiwaniIntermediateSubmissionStatus = findViewById(R.id.textViewKhatDiwaniIntermediateSubmissionStatus);
        textViewKhatDiwaniIntermediateSubmissionRate = findViewById(R.id.textViewKhatDiwaniIntermediateSubmissionRate);
        textViewKhatDiwaniIntermediateSubmissionComment = findViewById(R.id.textViewKhatDiwaniIntermediateSubmissionComment);
        openAssessmentBtnKhatDiwaniIntermediate = findViewById(R.id.openAssessmentBtnKhatDiwaniIntermediate);

        //Advanced
        textViewKhatDiwaniAdvancedSubmissionStatus = findViewById(R.id.textViewKhatDiwaniAdvancedSubmissionStatus);
        textViewKhatDiwaniAdvancedSubmissionRate = findViewById(R.id.textViewKhatDiwaniAdvancedSubmissionRate);
        textViewKhatDiwaniAdvancedSubmissionComment = findViewById(R.id.textViewKhatDiwaniAdvancedSubmissionComment);
        openAssessmentBtnKhatDiwaniAdvanced = findViewById(R.id.openAssessmentBtnKhatDiwaniAdvanced);

        //KhatDiwaniJali
        //Beginner
        textViewKhatDiwaniJaliBeginnerSubmissionStatus =findViewById(R.id.textViewKhatDiwaniJaliBeginnerSubmissionStatus);
        textViewKhatDiwaniJaliBeginnerSubmissionRate = findViewById(R.id.textViewKhatDiwaniJaliBeginnerSubmissionRate);
        textViewKhatDiwaniJaliBeginnerSubmissionComment = findViewById(R.id.textViewKhatDiwaniJaliBeginnerSubmissionComment);
        openAssessmentBtnKhatDiwaniJaliBeginner = findViewById(R.id.openAssessmentBtnKhatDiwaniJaliBeginner);

        //Intermediate
        textViewKhatDiwaniJaliIntermediateSubmissionStatus = findViewById(R.id.textViewKhatDiwaniJaliIntermediateSubmissionStatus);
        textViewKhatDiwaniJaliIntermediateSubmissionRate = findViewById(R.id.textViewKhatDiwaniJaliIntermediateSubmissionRate);
        textViewKhatDiwaniJaliIntermediateSubmissionComment = findViewById(R.id.textViewKhatDiwaniJaliIntermediateSubmissionComment);
        openAssessmentBtnKhatDiwaniJaliIntermediate = findViewById(R.id.openAssessmentBtnKhatDiwaniJaliIntermediate);

        //Advanced
        textViewKhatDiwaniJaliAdvancedSubmissionStatus = findViewById(R.id.textViewKhatDiwaniJaliAdvancedSubmissionStatus);
        textViewKhatDiwaniJaliAdvancedSubmissionRate = findViewById(R.id.textViewKhatDiwaniJaliAdvancedSubmissionRate);
        textViewKhatDiwaniJaliAdvancedSubmissionComment = findViewById(R.id.textViewKhatDiwaniJaliAdvancedSubmissionComment);
        openAssessmentBtnKhatDiwaniJaliAdvanced = findViewById(R.id.openAssessmentBtnKhatDiwaniJaliAdvanced);

        //KhatThuluth
        //Beginner
        textViewKhatThuluthBeginnerSubmissionStatus =findViewById(R.id.textViewKhatThuluthBeginnerSubmissionStatus);
        textViewKhatThuluthBeginnerSubmissionRate = findViewById(R.id.textViewKhatThuluthBeginnerSubmissionRate);
        textViewKhatThuluthBeginnerSubmissionComment = findViewById(R.id.textViewKhatThuluthBeginnerSubmissionComment);
        openAssessmentBtnKhatThuluthBeginner = findViewById(R.id.openAssessmentBtnKhatThuluthBeginner);

        //Intermediate
        textViewKhatThuluthIntermediateSubmissionStatus = findViewById(R.id.textViewKhatThuluthIntermediateSubmissionStatus);
        textViewKhatThuluthIntermediateSubmissionRate = findViewById(R.id.textViewKhatThuluthIntermediateSubmissionRate);
        textViewKhatThuluthIntermediateSubmissionComment = findViewById(R.id.textViewKhatThuluthIntermediateSubmissionComment);
        openAssessmentBtnKhatThuluthIntermediate = findViewById(R.id.openAssessmentBtnKhatThuluthIntermediate);

        //Advanced
        textViewKhatThuluthAdvancedSubmissionStatus = findViewById(R.id.textViewKhatThuluthAdvancedSubmissionStatus);
        textViewKhatThuluthAdvancedSubmissionRate = findViewById(R.id.textViewKhatThuluthAdvancedSubmissionRate);
        textViewKhatThuluthAdvancedSubmissionComment = findViewById(R.id.textViewKhatThuluthAdvancedSubmissionComment);
        openAssessmentBtnKhatThuluthAdvanced = findViewById(R.id.openAssessmentBtnKhatThuluthAdvanced);
    }
}
