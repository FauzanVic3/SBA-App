package com.example.sbaapp.Premium;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sbaapp.AdminFeedbackFragment.FragmentFeedbackKhat;
import com.example.sbaapp.AdminMainActivity;
import com.example.sbaapp.NotificationActivity;
import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApprovePurchaseActivity extends AppCompatActivity {

    private TextView nameTV,classTV, priceTV, emailTV;
    private ImageView proofImage;
    private Button rejectBtn, approveBtn;

    private String classID, uid, name, email, reply_message, link, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_purchase);

        initializeViews();

        Bundle bundle = getIntent().getExtras();
        getBundle(bundle);

        setViews();
        setClick();
    }

    public void setClick(){

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovePurchaseActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Reject Request");
                builder.setMessage("Are you sure you want to reject this request?");
                builder.setPositiveButton("Reject",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                databaseReference.child("Purchase").child(classID).child(uid).child("purchase_status").setValue("rejected");
                                databaseReference.child("Purchase").child(classID).child(uid).child("purchase_message").setValue("We are sorry to announce that your request for class "+classIdConverter(classID)+" has been rejected due to false proof of payment.\nYou may try again or contact our admin in the purchase screen");
                                databaseReference.child("Purchase").child(classID).child(uid).child("read_status").setValue("false");
                                databaseReference.child("Users").child(uid).child("class_details").child(classID).child("0").child("purchase").setValue("false");
                                databaseReference.child("Notification").child(uid).child(ts).child("classID").setValue(classID);
                                databaseReference.child("Notification").child(uid).child(ts).child("message").setValue("We are sorry to announce that your request for class "+classIdConverter(classID)+" has been rejected due to false proof of payment.\nYou may try again or contact our admin in the purchase screen");


                                Toast.makeText(ApprovePurchaseActivity.this, "Your response has been sent to "+name,Toast.LENGTH_SHORT);
                                Intent intent = new Intent(ApprovePurchaseActivity.this, ApprovePurchaseListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovePurchaseActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Approve Request");
                builder.setMessage("Are you sure you want to approve this request?");
                builder.setPositiveButton("Approve",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Purchase").child(classID).child(uid).child("purchase_status").setValue("approved");
                                databaseReference.child("Purchase").child(classID).child(uid).child("purchase_message").setValue("We are happy to announce that your request for class "+classIdConverter(classID)+" has been approved! \nHappy learning! ");
                                databaseReference.child("Purchase").child(classID).child(uid).child("read_status").setValue("false");
                                databaseReference.child("Users").child(uid).child("class_details").child(classID).child("0").child("purchase").setValue("approved");
                                databaseReference.child("Notification").child(uid).child(ts).child("classID").setValue(classID);
                                databaseReference.child("Notification").child(uid).child(ts).child("message").setValue("We are happy to announce that your request for class "+classIdConverter(classID)+" has been approved! \nHappy learning! ");

                                Toast.makeText(ApprovePurchaseActivity.this, "Your response has been sent to "+name,Toast.LENGTH_SHORT);
                                Intent intent = new Intent(ApprovePurchaseActivity.this, AdminMainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });
    }
    public void getBundle(Bundle bundle){

        classID = bundle.getString("classID");
        uid = bundle.getString("uid");
        name = bundle.getString("name");
        email = bundle.getString("email");
        link = bundle.getString("purchase_link");


    }
    public void initializeViews(){
        nameTV = findViewById(R.id.nameTV);
        classTV = findViewById(R.id.classTV);
        priceTV = findViewById(R.id.priceTV);
        emailTV = findViewById(R.id.emailTV);
        proofImage = findViewById(R.id.proofImage);
        rejectBtn = findViewById(R.id.rejectBtn);
        approveBtn = findViewById(R.id.approveBtn);


    }
    public void setViews(){
        nameTV.setText(name);
        classTV.setText(classIdConverter(classID));
        emailTV.setText(email);

        String level = getLevel(classID);
        String price;
        switch(level){
            case "Beginner":{
                price = "RM 30";
                priceTV.setText(price);
                break;
            }case "Intermediate":{
                price = "RM 50";
                priceTV.setText(price);
                break;
            }case  "Advanced":{
                price = "RM 80";
                priceTV.setText(price);
                break;
            }
        }
        Glide.with(ApprovePurchaseActivity.this)
                .load(Uri.parse(link))
                .into(proofImage);



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
}
