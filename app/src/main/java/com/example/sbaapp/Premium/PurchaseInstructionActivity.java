package com.example.sbaapp.Premium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sbaapp.ClassVideoPackage.SubClassList;
import com.example.sbaapp.NotificationActivity;
import com.example.sbaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PurchaseInstructionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Toolbar toolbarInstruction;
    private TextView classNameTV;
    private ImageView preview;
    private Button uploadBtn, chooseBtn, photoBtn,toggleBtn,whatsappBtn;
    private Uri filePath,photoPath,submissionUrl;
    private DatabaseReference databaseReference;
    private  StorageReference storageReference;

    private String[] setClassID = new String[3];
    private String classID;

    private String[] adminUID = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_instruction);

        adminUID[0] = "eUEVafOyOAa8mkso1EgYO10FDcu2";
        adminUID[1] = "nZOdEsCeYrcJyqTp1maCKyzGtq52";
        adminUID[2] = "Q2BIRtJQ7obYmQFmSMn78ILSDLy2";

        initializeViews();
        Intent intent = getIntent();
        if(intent.hasExtra("classID")){
            Bundle bundle = getIntent().getExtras();
            retrieveClassIDBundle(bundle);

        }else if(intent.hasExtra("classIDArray")){
            Bundle bundle = getIntent().getExtras();
            retrieveClassIDArrayBundle(bundle);
        }






        toggleInstruction();

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Opening camera...");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preview.getDrawable().getConstantState() !=getResources().getDrawable(R.drawable.image_preview).getConstantState()){
                    if(classID!=null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseInstructionActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Sending Request");
                        builder.setMessage("You are about to SEND REQUEST. Proceed?");
                        builder.setPositiveButton("Proceed",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        uploadFile();
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, null);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else
                        uploadArrayFile();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseInstructionActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("No payment proof chosen");
                    builder.setMessage("Please press CHOOSE IMAGE or OPEN CAMERA to upload payment proof");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    //builder.setNegativeButton(android.R.string.cancel, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Toast.makeText(PurchaseInstructionActivity.this, "Upload proof of payment first!",Toast.LENGTH_LONG).show();
                }
            }
        });

        if(preview.getDrawable()!=null){
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseInstructionActivity.this);
                builder.setCancelable(true);
                builder.setTitle("You are about to exit the app");
                builder.setMessage("Are you sure you want to redirect to Whatsapp?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String number = "601128870677";
                                String new_class =  get_class(classID)+" "+getSubClass(classID)+" "+getLevel(classID);
                                String text = "I am interested in purchasing your class "+new_class+ " in Shaikh Bakry Ayoub app.\n\n I have a question to ask: \n";
                                String url = "https://api.whatsapp.com/send?phone="+number+"&text="+text;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    public void initializeViews(){
        uploadBtn = findViewById(R.id.uploadBtn);
        whatsappBtn = findViewById(R.id.whatsappBtn);
        chooseBtn = findViewById(R.id.chooseBtn);
        photoBtn = findViewById(R.id.photoBtn);
        toggleBtn = findViewById(R.id.toggleBtn);
        preview = findViewById(R.id.preview);
        classNameTV = findViewById(R.id.classNameTV);
        toolbarInstruction = findViewById(R.id.toolbarInstructions);

    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image"), PICK_IMAGE_REQUEST);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            preview.setImageBitmap(photo);
        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();


            preview.setImageURI(filePath);

        }
    }


    public void toggleInstruction(){

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toolbarInstruction.getVisibility() ==View.GONE){
                    toolbarInstruction.setVisibility(View.VISIBLE);
                    toggleBtn.setText("HIDE PAYMENT INSTRUCTIONS");
                }
                else{
                    toolbarInstruction.setVisibility(View.GONE);
                    toggleBtn.setText("VIEW PAYMENT INSTRUCTIONS");
                }

            }
        });
    }

    public void retrieveClassIDBundle(Bundle bundle){
        classID = bundle.getString("classID");
        System.out.println("ClassID: "+classID);
        setClassName(classID);
    }

    public void retrieveClassIDArrayBundle(Bundle bundle){
        setClassID = bundle.getStringArray("classIDArray");

        setClassName(setClassID[0]);
    }

    public void setClassName(String classID){
        String new_class =  get_class(classID)+" "+getSubClass(classID)+" "+getLevel(classID);
        classNameTV.setText(new_class);
    }

    public String get_class(String classID){
        if(classID.contains("khat")){
            return "Khat";
        }
        else if(classID.contains("nurul")){
            return "Nurul Bayan";
        }
        else
            return null;
    }

    public String getSubClass(String classID){
        if(classID.contains("riqah")){
            return "Riqah";
        }
        else if(classID.contains("nasakh")){
            return "Nasakh";
        }
        else if(classID.contains("farisi")){
            return "Farisi";
        }
        else if(classID.contains("diwani")){
            return "Diwani";
        }
        else if(classID.contains("jali")){
            return "Diwani Jali";
        }
        else if(classID.contains("thuluth")){
            return "Thuluth";
        }
        else return "";
    }

    public String getLevel(String classID){
        if(classID.contains("beginner")){
            return "Beginner";
        }
        else if(classID.contains("intermediate")){
            return "Intermediate";
        }
        else if(classID.contains("advanced")){
            return "Advanced";
        }
        else return null;
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


    public void uploadFile(){

        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference storeRef = storageReference.child("payment_receipt/"+classID+"/"+currentUID);


        preview.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
        byte[] byteArray = baos.toByteArray();

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


                        //Update Users
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(currentUID).child("class_details").child(classID).child("0").child("purchase").setValue("requested");
                        databaseReference.child("Users").child(currentUID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("name").getValue().toString();
                                //String email = dataSnapshot.child("email").getValue().toString();
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Purchase").child(classID).child(currentUID);

                                databaseReference.child("uid").setValue(currentUID);
                                databaseReference.child("classID").setValue(classID);
                                databaseReference.child("name").setValue(name);
                                databaseReference.child("email").setValue(email);
                                databaseReference.child("purchase_link").setValue(submissionUrl.toString());
                                databaseReference.child("purchase_message").setValue(name+" is requesting to purchase "+classIdConverter(classID));
                                databaseReference.child("purchase_status").setValue("requested");


                                Intent intent = new Intent(PurchaseInstructionActivity.this, SubClassList.class);
                                String classSelection = get_class(classID);
//                                String subClassSelection = getSubClass(classID);
//                                String level = getLevel(classID);

                                Bundle new_bundle = new Bundle();
                                new_bundle.putString("class", classSelection);
//                                new_bundle.putString("subClass", subClassSelection);
//                                new_bundle.putString("level", level);

                                DatabaseReference databaseReferenceNotification = FirebaseDatabase.getInstance().getReference();
                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();
                                for(int i=0; i< adminUID.length;i++) {


                                    databaseReferenceNotification.child("Notification").child(adminUID[i]).child(ts).child("classID").setValue(classID);
                                    databaseReferenceNotification.child("Notification").child(adminUID[i]).child(ts).child("message").setValue(name+" is requesting to purchase "+classIdConverter(classID));
                                }

                                intent.putExtras(new_bundle);
                                startActivity(intent);
                                finish();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                        Toast.makeText(PurchaseInstructionActivity.this, "Upload success!", Toast.LENGTH_SHORT).show();

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

    public void uploadArrayFile(){
        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        for(int i = 0; i<setClassID.length;i++){
            final StorageReference storeRef = storageReference.child("payment_receipt/"+setClassID[i]+"/"+currentUID);


            preview.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bitmap is required image which have to send  in Bitmap form
            byte[] byteArray = baos.toByteArray();

            final int finalI = i;
            UploadTask uploadTask = (UploadTask) storeRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            submissionUrl = uri;

                            String class_name;

                            class_name=setClassID[finalI];

                            final String class_name_ = class_name;


                            //Update Users
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Users").child(currentUID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Purchase").child(setClassID[finalI]).child(currentUID);

                                    databaseReference.child("uid").setValue(currentUID);
                                    databaseReference.child("classID").setValue(setClassID[finalI]);
                                    databaseReference.child("name").setValue(name);
                                    databaseReference.child("email").setValue(email);
                                    databaseReference.child("purchase_link").setValue(submissionUrl.toString());
                                    databaseReference.child("purchase_message").setValue(name+" is requesting to purchase "+classIdConverter(setClassID[finalI]));
                                    databaseReference.child("purchase_status").setValue("requested");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                            Toast.makeText(PurchaseInstructionActivity.this, "Upload success!", Toast.LENGTH_SHORT).show();

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
