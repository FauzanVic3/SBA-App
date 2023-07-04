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
import android.widget.LinearLayout;
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

public class FragmentKhatAdvanced extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView,imageView2,imageView3;
    private Button submitBtn,submitBtn2,submitBtn3;
    private String classSelection, subClassSelection, level;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri submissionUrl;

    private int getImageViewId;

    private TextView textViewSubmissionStatus,textViewSubmissionRate,textViewSubmissionStatus2,textViewSubmissionRate2,textViewSubmissionStatus3,textViewSubmissionRate3,textViewTaskQuestion,khat_advanced_textview2,khat_advanced_textview3;
    private LinearLayout khat_advanced_linearlayout2_1,khat_advanced_linearlayout2_2,khat_advanced_linearlayout2_3,khat_advanced_linearlayout3_1,khat_advanced_linearlayout3_2,khat_advanced_linearlayout3_3;


    private StorageReference storageReference;
    private DatabaseReference databaseReference,submissionDatabase;
    private FirebaseUser mCurrentUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_khat_advanced,container,false);

        //Bundle from Submission class
        classSelection = this.getArguments().getString("class");
        subClassSelection = this.getArguments().getString("subClass");
        level = this.getArguments().getString("level");




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
                getImageViewId =1;
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
                                uploadFile("1");

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

                //
            }
        });

        //If Thuluth or Nasakh
        textViewTaskQuestion = (TextView)view.findViewById(R.id.textViewTaskQuestion);
        khat_advanced_textview2 = view.findViewById(R.id.khat_advanced_textview2);
        khat_advanced_textview3 = view.findViewById(R.id.khat_advanced_textview3);
        khat_advanced_linearlayout2_1 = view.findViewById(R.id.khat_advanced_linearlayout2_1);
        khat_advanced_linearlayout2_2 = view.findViewById(R.id.khat_advanced_linearlayout2_2);
        khat_advanced_linearlayout2_3 = view.findViewById(R.id.khat_advanced_linearlayout2_3);
        khat_advanced_linearlayout3_1 = view.findViewById(R.id.khat_advanced_linearlayout3_1);
        khat_advanced_linearlayout3_2 = view.findViewById(R.id.khat_advanced_linearlayout3_2);
        khat_advanced_linearlayout3_3 = view.findViewById(R.id.khat_advanced_linearlayout3_3);
        imageView2 = view.findViewById(R.id.previewSubmission2);
        imageView3 = view.findViewById(R.id.previewSubmission3);


        if(subClassSelection.equals("Thuluth") || subClassSelection.equals("Nasakh")){

            textViewTaskQuestion.setText(R.string.question_khat_advanced_special);


            khat_advanced_linearlayout2_1.setVisibility(View.VISIBLE);
            khat_advanced_linearlayout2_2.setVisibility(View.VISIBLE);
            khat_advanced_linearlayout2_3.setVisibility(View.VISIBLE);
            khat_advanced_linearlayout3_1.setVisibility(View.VISIBLE);
            khat_advanced_linearlayout3_2.setVisibility(View.VISIBLE);
            khat_advanced_linearlayout3_3.setVisibility(View.VISIBLE);
            khat_advanced_textview2.setVisibility(View.VISIBLE);
            khat_advanced_textview3.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.VISIBLE);


            databaseReference.child("submission_status2").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    textViewSubmissionStatus2 = (TextView) view.findViewById(R.id.textViewSubmissionStatus2);
                    textViewSubmissionStatus2.setText(dataSnapshot.getValue().toString());
                    System.out.println(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child("submission_rating2").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    textViewSubmissionRate2 = (TextView) view.findViewById(R.id.textViewSubmissionRate2);
                    textViewSubmissionRate2.setText(dataSnapshot.getValue().toString());

                    System.out.println(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("submission_status3").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    textViewSubmissionStatus3 = (TextView) view.findViewById(R.id.textViewSubmissionStatus3);
                    textViewSubmissionStatus3.setText(dataSnapshot.getValue().toString());
                    System.out.println(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child("submission_rating3").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    textViewSubmissionRate3 = (TextView) view.findViewById(R.id.textViewSubmissionRate3);
                    textViewSubmissionRate3.setText(dataSnapshot.getValue().toString());

                    System.out.println(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            this.imageView2 = (ImageView)view.findViewById(R.id.previewSubmission2);
            Button photoButton2 = (Button) view.findViewById(R.id.photoButton2);
            photoButton2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    System.out.println("Opening camera...");
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getImageViewId = 2;
                    cameraIntent.putExtra("imageView_id",2);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            });

            submitBtn2 = (Button) view.findViewById(R.id.submitBtn2);
            submitBtn2.setOnClickListener(new View.OnClickListener() {
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
                                    uploadFile("2");

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //
                }
            });

            this.imageView3 = (ImageView)view.findViewById(R.id.previewSubmission3);
            Button photoButton3 = (Button) view.findViewById(R.id.photoButton3);
            photoButton3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    System.out.println("Opening camera...");
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //cameraIntent.putExtra("imageView_id",3);
                    getImageViewId = 3;
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            });

            submitBtn3 = (Button) view.findViewById(R.id.submitBtn3);
            submitBtn3.setOnClickListener(new View.OnClickListener() {
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
                                    uploadFile("3");

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //
                }
            });



        }else{
            textViewTaskQuestion.setText(R.string.question_khat_advanced);
            khat_advanced_linearlayout2_1.setVisibility(View.INVISIBLE);
            khat_advanced_linearlayout2_2.setVisibility(View.INVISIBLE);
            khat_advanced_linearlayout2_3.setVisibility(View.INVISIBLE);
            khat_advanced_linearlayout3_1.setVisibility(View.INVISIBLE);
            khat_advanced_linearlayout3_2.setVisibility(View.INVISIBLE);
            khat_advanced_linearlayout3_3.setVisibility(View.INVISIBLE);
            khat_advanced_textview2.setVisibility(View.INVISIBLE);
            khat_advanced_textview3.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
        }





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

            if(getImageViewId==1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
            else if(getImageViewId==2) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView2.setImageBitmap(photo);
            }
            else if(getImageViewId==3) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView3.setImageBitmap(photo);
            }
        }
    }

    public void uploadFile(String taskID) {

        if (taskID.equals("1")) {


            if (imageView != null) {

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String currentUID = mCurrentUser.getUid();


                imageView.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
                byte[] byteArray = baos.toByteArray();

                final StorageReference storeRef = storageReference.child("submission/" + classSelection + "/" + subClassSelection + "/" + level + "/" + currentUID + "_" + taskID);


                UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                submissionUrl = uri;


                                String class_name;

                                class_name = specialClassNameConverter(classSelection, subClassSelection, level);
                                System.out.println(class_name);


                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");

                                databaseReference.child("submission_link").setValue(submissionUrl.toString());
                                databaseReference.child("submission_status").setValue("Submitted");

                                //Update to Submission child in Database
                                String email = mCurrentUser.getEmail();
                                submissionDatabase = database.getReference().child("Submission").child(class_name).child(currentUID);
                                submissionDatabase.child("submission_status").setValue("Submitted");
                                submissionDatabase.child("submission_rating").setValue("Not rated");
                                submissionDatabase.child("submission_comment").setValue("No comment");
                                submissionDatabase.child("submission_link").setValue(submissionUrl.toString());
                                submissionDatabase.child("uid").setValue(currentUID);
                                submissionDatabase.child("email").setValue(email);
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
        }else if(taskID.equals("2")){
            if (imageView2 != null) {

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String currentUID = mCurrentUser.getUid();


                imageView2.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) imageView2.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
                byte[] byteArray = baos.toByteArray();

                final StorageReference storeRef = storageReference.child("submission/" + classSelection + "/" + subClassSelection + "/" + level + "/" + currentUID + "_" + taskID);


                UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                submissionUrl = uri;


                                String class_name;

                                class_name = specialClassNameConverter(classSelection, subClassSelection, level);
                                System.out.println(class_name);


                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");

                                databaseReference.child("submission_link2").setValue(submissionUrl.toString());
                                databaseReference.child("submission_status2").setValue("Submitted");

                                //Update to Submission child in Database
                                String email = mCurrentUser.getEmail();
                                submissionDatabase = database.getReference().child("Submission").child(class_name).child(currentUID);
                                submissionDatabase.child("submission_status").setValue("Submitted");
                                submissionDatabase.child("submission_rating").setValue("Not rated");
                                submissionDatabase.child("submission_comment").setValue("No comment");
                                submissionDatabase.child("submission_link").setValue(submissionUrl.toString());
                                submissionDatabase.child("uid").setValue(currentUID);
                                submissionDatabase.child("email").setValue(email);
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
        else if(taskID.equals("3")){
            if (imageView3 != null) {

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String currentUID = mCurrentUser.getUid();


                imageView3.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) imageView3.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
                byte[] byteArray = baos.toByteArray();

                final StorageReference storeRef = storageReference.child("submission/" + classSelection + "/" + subClassSelection + "/" + level + "/" + currentUID + "_" + taskID);


                UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                submissionUrl = uri;


                                String class_name;

                                class_name = specialClassNameConverter(classSelection, subClassSelection, level);
                                System.out.println(class_name);
                                final String class_name_ = class_name;


                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                databaseReference = database.getReference().child("Users").child(currentUID).child("class_details").child(class_name).child("0");

                                databaseReference.child("submission_link3").setValue(submissionUrl.toString());
                                databaseReference.child("submission_status3").setValue("Submitted");

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
