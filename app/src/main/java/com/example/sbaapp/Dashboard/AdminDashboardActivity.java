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

import com.example.sbaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView submissionStatusTVNB, feedbackTVNB, submissionStatusTVKhat, feedbackTVKhat, submissionStatusTVRiqah, feedbackTVRiqah,
                     submissionStatusTVNasakh, feedbackTVNasakh, submissionStatusTVFarisi, feedbackTVFarisi, submissionStatusTVDiwani, feedbackTVDiwani,
                     submissionStatusTVDiwaniJali, feedbackTVDiwaniJali, submissionStatusTVThuluth, feedbackTVThuluth;
    private Button openEvaluationBtnNB, openEvaluationBtnKhat, openEvaluationBtnRiqah, openEvaluationBtnNasakh, openEvaluationBtnFarisi, openEvaluationBtnDiwani, openEvaluationBtnDiwaniJali, openEvaluationBtnThuluth;
    private Switch switchNurulBayan,switchKhat;
    private LinearLayout mLinearLayoutNurulBayan, mLinearLayoutHeaderNurulBayan, mLinearLayoutHeaderKhat, mLinearLayoutKhat;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initializeViews();
        setupExpandables();

        extractDatabase();



    }


    public void setupExpandables(){

        mLinearLayoutHeaderNurulBayan = (LinearLayout) findViewById(R.id.headerNurulBayan);
        mLinearLayoutNurulBayan = (LinearLayout) findViewById(R.id.expandableNurulBayan);
        mLinearLayoutHeaderKhat = (LinearLayout) findViewById(R.id.headerKhat);
        mLinearLayoutKhat = (LinearLayout) findViewById(R.id.expandableKhat);
        //Handles animation for Nurul Bayan
        //Switch Nurul Bayan
//        switchNurulBayan = (Switch) findViewById(R.id.switchNurulBayan);
//        switchNurulBayan.setChecked(false);
//
//
//        //set visibility to GONE
//        mLinearLayoutNurulBayan.setVisibility(View.GONE);
//
//        mLinearLayoutHeaderNurulBayan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mLinearLayoutNurulBayan.getVisibility()==View.GONE){
//                    switchNurulBayan.setChecked(true);
//                    expand(mLinearLayoutNurulBayan);
//                    mLinearLayoutHeaderKhat.setVisibility(View.GONE);
//
//                }else{
//                    switchNurulBayan.setChecked(false);
//                    collapse(mLinearLayoutNurulBayan);
//                    mLinearLayoutHeaderKhat.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        //Switch Khat
        switchKhat = (Switch) findViewById(R.id.switchKhat);
        switchKhat.setChecked(false);


        //set visibility to GONE
        mLinearLayoutKhat.setVisibility(View.GONE);

        mLinearLayoutHeaderKhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutKhat.getVisibility()==View.GONE){
                    switchKhat.setChecked(true);
                    expand(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.GONE);
                    mLinearLayoutNurulBayan.setVisibility(View.GONE);

                }else{
                    switchNurulBayan.setChecked(false);
                    collapse(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.VISIBLE);
                    mLinearLayoutNurulBayan.setVisibility(View.VISIBLE);
                }
            }
        });


        switchKhat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && mLinearLayoutKhat.getVisibility()==View.GONE){
                    expand(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.GONE);
                    mLinearLayoutNurulBayan.setVisibility(View.GONE);
                }else{
                    collapse(mLinearLayoutKhat);
                    mLinearLayoutHeaderNurulBayan.setVisibility(View.VISIBLE);
                    mLinearLayoutNurulBayan.setVisibility(View.VISIBLE);
                }
            }
        });


//        switchNurulBayan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked && mLinearLayoutNurulBayan.getVisibility()==View.GONE){
//                    expand(mLinearLayoutNurulBayan);
//                    mLinearLayoutHeaderKhat.setVisibility(View.GONE);
//                }else{
//                    collapse(mLinearLayoutNurulBayan);
//                    mLinearLayoutHeaderKhat.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    public void initializeViews(){


        feedbackTVDiwani = findViewById(R.id.feedbackTVDiwani);
        feedbackTVDiwaniJali = findViewById(R.id.feedbackTVDiwaniJali);
        feedbackTVFarisi = findViewById(R.id.feedbackTVFarisi);
        feedbackTVKhat = findViewById(R.id.feedbackTVKhat);
        feedbackTVNasakh = findViewById(R.id.feedbackTVNasakh);
        feedbackTVNB = findViewById(R.id.feedbackTVNB);
        feedbackTVRiqah = findViewById(R.id.feedbackTVRiqah);
        feedbackTVThuluth = findViewById(R.id.feedbackTVThuluth);

        openEvaluationBtnNB = findViewById(R.id.openEvaluationBtnNB);
        openEvaluationBtnKhat = findViewById(R.id.openEvaluationBtnKhat);
        openEvaluationBtnDiwani = findViewById(R.id.openEvaluationBtnDiwani);
        openEvaluationBtnDiwaniJali = findViewById(R.id.openEvaluationBtnDiwaniJali);
        openEvaluationBtnFarisi = findViewById(R.id.openEvaluationBtnFarisi);
        openEvaluationBtnNasakh = findViewById(R.id.openEvaluationBtnNasakh);
        openEvaluationBtnRiqah = findViewById(R.id.openEvaluationBtnRiqah);
        openEvaluationBtnThuluth = findViewById(R.id.openEvaluationBtnThuluth);

        openEvaluationBtnNB.setOnClickListener(this);
        openEvaluationBtnKhat.setOnClickListener(this);
        openEvaluationBtnRiqah.setOnClickListener(this);
        openEvaluationBtnNasakh.setOnClickListener(this);
        openEvaluationBtnFarisi.setOnClickListener(this);
        openEvaluationBtnDiwani.setOnClickListener(this);
        openEvaluationBtnDiwaniJali.setOnClickListener(this);
        openEvaluationBtnThuluth.setOnClickListener(this);
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Submission");
        nb_b = databaseReference.child("nurul_bayan_beginner");
        nb_i = databaseReference.child("nurul_bayan_intermediate");
        nb_a = databaseReference.child("nurul_bayan_advanced");
        k_d_b = databaseReference.child("khat_diwani_beginner");
        k_d_i = databaseReference.child("khat_diwani_intermediate");
        k_d_a = databaseReference.child("khat_diwani_advanced");
        k_dj_b = databaseReference.child("khat_diwani_jali_beginner");
        k_dj_i = databaseReference.child("khat_diwani_jali_intermediate");
        k_dj_a = databaseReference.child("khat_diwani_jali_advanced");
        k_f_b = databaseReference.child("khat_farisi_beginner");
        k_f_i = databaseReference.child("khat_farisi_intermediate");
        k_f_a = databaseReference.child("khat_farisi_advanced");
        k_n_b = databaseReference.child("khat_nasakh_beginner");
        k_n_i = databaseReference.child("khat_nasakh_intermediate");
        k_n_a = databaseReference.child("khat_nasakh_advanced");
        k_r_b = databaseReference.child("khat_riqah_beginner");
        k_r_i = databaseReference.child("khat_riqah_intermediate");
        k_r_a = databaseReference.child("khat_riqah_advanced");
        k_t_b = databaseReference.child("khat_thuluth_beginner");
        k_t_i = databaseReference.child("khat_thuluth_intermediate");
        k_t_a = databaseReference.child("khat_thuluth_advanced");

        //nb
        nb_b.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_nb[0]++;
                        }

                    }

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
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_nb[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        nb_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_nb[0]++;
                        }

                    }

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

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_diwani[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_d_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_diwani[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_d_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_diwani[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

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

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_diwani_jali[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_dj_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_diwani_jali[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_dj_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_diwani_jali[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

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

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_farisi[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_f_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_farisi[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_f_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_farisi[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

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

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_nasakh[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_n_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_nasakh[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_n_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_nasakh[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

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

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_riqah[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_r_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_riqah[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_r_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_riqah[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

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

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_thuluth[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_t_i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_thuluth[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        k_t_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    if(childSnapshot.getChildrenCount()>1) {
                        if (childSnapshot.child("submission_status").getValue().toString().equals("Submitted")
                                && childSnapshot.child("submission_rating").getValue().toString().equals("Not rated")) {

                            count_feedback_khat_thuluth[0]++;
                            count_feedback_khat[0]++;
                        }

                    }

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

                feedbackTVNB.setText(Integer.toString(feedback_count));
                break;
            }
            case "khat":
            {

                feedbackTVKhat.setText(Integer.toString(feedback_count));
                break;
            }
            case "riqah":
            {

                feedbackTVRiqah.setText(Integer.toString(feedback_count));
                break;
            }
            case "nasakh":
            {

                feedbackTVNasakh.setText(Integer.toString(feedback_count));
                break;
            }
            case "farisi":
            {

                feedbackTVFarisi.setText(Integer.toString(feedback_count));
                break;
            }
            case "diwani":
            {

                feedbackTVDiwani.setText(Integer.toString(feedback_count));
                break;
            }
            case "diwani jali":
            {

                feedbackTVDiwaniJali.setText(Integer.toString(feedback_count));
                break;
            }
            case "thuluth":
            {

                feedbackTVThuluth.setText(Integer.toString(feedback_count));
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

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()){
            case R.id.openEvaluationBtnNB:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"nurul_bayan_beginner","nurul_bayan_intermediate","nurul_bayan_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case  R.id.openEvaluationBtnKhat:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_riqah_beginner","khat_riqah_intermediate","khat_riqah_advanced",
                        "khat_nasakh_beginner","khat_nasakh_intermediate","khat_nasakh_advanced",
                        "khat_farisi_beginner","khat_farisi_intermediate","khat_farisi_advanced",
                        "khat_diwani_beginner","khat_diwani_intermediate","khat_diwani_advanced",
                        "khat_diwani_jali_beginner","khat_diwani_jali_intermediate","khat_diwani_jali_advanced",
                        "khat_thuluth_beginner","khat_thuluth_intermediate","khat_thuluth_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.openEvaluationBtnRiqah:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_riqah_beginner","khat_riqah_intermediate","khat_riqah_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            }
            case R.id.openEvaluationBtnNasakh:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_nasakh_beginner","khat_nasakh_intermediate","khat_nasakh_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            }
            case R.id.openEvaluationBtnFarisi:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_farisi_beginner","khat_farisi_intermediate","khat_farisi_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.openEvaluationBtnDiwani:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_diwani_beginner","khat_diwani_intermediate","khat_diwani_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.openEvaluationBtnDiwaniJali:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_diwani_jali_beginner","khat_diwani_jali_intermediate","khat_diwani_jali_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.openEvaluationBtnThuluth:
            {
                intent = new Intent(AdminDashboardActivity.this, AdminDashboardItemView.class);
                bundle = new Bundle();
                bundle.putStringArray("classID", new String[]{"khat_thuluth_beginner","khat_thuluth_intermediate","khat_thuluth_advanced"});
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }
}
