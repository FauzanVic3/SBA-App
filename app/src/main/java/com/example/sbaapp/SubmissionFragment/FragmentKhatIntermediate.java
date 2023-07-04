package com.example.sbaapp.SubmissionFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class FragmentKhatIntermediate extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private Button submitBtn;
    private String classSelection, subClassSelection, level;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri submissionUrl;

    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewTaskQuestion;


    private StorageReference storageReference;
    private DatabaseReference databaseReference,submissionDatabase;
    private FirebaseUser mCurrentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_khat_beginner,container,false);

        //Bundle from Submission class
        classSelection = this.getArguments().getString("class");
        subClassSelection = this.getArguments().getString("subClass");
        level = this.getArguments().getString("level");

        textViewTaskQuestion =view.findViewById(R.id.textViewTaskQuestion);
        textViewTaskQuestion.setText(R.string.question_khat_intermediate);
        //set Firebase access storage
        storageReference = FirebaseStorage.getInstance().getReference();

        String class_name = specialClassNameConverter(classSelection,subClassSelection,level);

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




        this.imageView = (ImageView)view.findViewById(R.id.previewSubmission1);
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
                                uploadFile();

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

    public void uploadFile(){
        if(imageView!=null){

            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            final String currentUID = mCurrentUser.getUid();


            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
            byte[] byteArray = baos.toByteArray();

            final StorageReference storeRef = storageReference.child("submission/"+classSelection+"/"+subClassSelection+"/"+level+"/"+currentUID);


            UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            submissionUrl = uri;


                            String class_name;

                            class_name=specialClassNameConverter(classSelection,subClassSelection,level);
                            System.out.println(class_name);

                            final String class_name_=class_name;


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
