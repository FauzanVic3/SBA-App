package com.example.sbaapp.AdminFeedbackFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sbaapp.MainActivity;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class FragmentFeedbackKhat extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView,userSubmissionImageView;
    private RatingBar ratingBar;
    private EditText commentET;
    private Button submitBtn,toggleQuestionBtn;
    private String classID,uid,name,email;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri submissionUrl;
    private Toolbar toolbarQuestion;

    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewTaskQuestion, textViewSubmissionComment,textViewName;


    private StorageReference storageReference;
    private DatabaseReference databaseReference,submissionDatabase;
    private FirebaseUser mCurrentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_feedback_khat,container,false);

        //Bundle previous class
        classID = this.getArguments().getString("classID");
        uid = this.getArguments().getString("uid");
        name = this.getArguments().getString("name");
        email = this.getArguments().getString("email");

        //Initializing Views
        textViewTaskQuestion =view.findViewById(R.id.textViewTaskQuestion);
        textViewSubmissionStatus = (TextView) view.findViewById(R.id.textViewSubmissionStatus);
        textViewSubmissionRate = (TextView) view.findViewById(R.id.textViewSubmissionRate);
        textViewSubmissionComment = view.findViewById(R.id.textViewSubmissionComment);
        textViewName = view.findViewById(R.id.textViewName);
        commentET = view.findViewById(R.id.commentET);
        ratingBar = view.findViewById(R.id.ratingBar);
        userSubmissionImageView = (ImageView)view.findViewById(R.id.userSubmissionImageView);
        this.imageView = (ImageView)view.findViewById(R.id.previewSubmission1);

        textViewName.setText(name + " "+classIdConverter(classID));


        if(classID.contains("beginner")){
            textViewTaskQuestion.setText(R.string.question_khat_beginner);
        }
        else if(classID.contains("intermediate")){
            textViewTaskQuestion.setText(R.string.question_khat_intermediate);
        }
        else if(classID.contains("advanced")){
            textViewTaskQuestion.setText(R.string.question_khat_advanced);
        }



        //set Firebase access storage
        storageReference = FirebaseStorage.getInstance().getReference();

        String class_name = classID;


        final String currentUID = uid;


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.child("Users").child(currentUID).child("class_details").child(class_name).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                textViewSubmissionStatus.setText(dataSnapshot.child("submission_status").getValue().toString());
                textViewSubmissionRate.setText(dataSnapshot.child("submission_rating").getValue().toString());
                textViewSubmissionComment.setText(dataSnapshot.child("submission_comment").getValue().toString());
                String imageSubmittedUrl = dataSnapshot.child("submission_link").getValue().toString();
                Glide.with(FragmentFeedbackKhat.this)
                        .load(Uri.parse(imageSubmittedUrl))
                        .into(userSubmissionImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        Button photoButton = (Button) view.findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("Opening camera...");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        //submit recorded audio to firebase
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final float rated = ratingBar.getRating();
                    if(rated>=0 && !commentET.getText().toString().equals(null) ){
                        System.out.println("Rating Bar: "+rated);

//                        if(imageView.getDrawable()!=null)
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Send Feedback");
                        builder.setMessage("You are about to send this feedback. Confirm?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        uploadFile(rated, commentET.getText().toString());

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    public void uploadFile(final Float rating, final String comment){

            final String currentUID = uid;

            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
            byte[] byteArray = baos.toByteArray();

            final StorageReference storeRef = storageReference.child("feedback/"+classID+"/"+currentUID);


            UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            submissionUrl = uri;

                            String class_name;

                            class_name=classID;

                            final String class_name_ = class_name;


                            System.out.println("Rating bar is: "+rating);
                            String rate = String.format("%.1f",rating);

                            //Update Users
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            databaseReference = database.child("Users").child(currentUID).child("class_details").child(class_name).child("0");

                            databaseReference.child("submission_link_feedback").setValue(submissionUrl.toString());
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
}
