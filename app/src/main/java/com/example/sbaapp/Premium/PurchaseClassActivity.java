package com.example.sbaapp.Premium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PurchaseClassActivity extends AppCompatActivity {

    private String classID, classSelection, subClassSelection, level, uid;

    private TextView textViewPremiumDescription,textViewPremiumBar;

    private ImageView imageViewPremiumClassBanner;

    private RadioButton radioButtonBeginner, radioButtonIntermediate, radioButtonAdvanced, radioButtonAll;

    private RadioGroup radioGroup;

    private Button purchaseButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_class);

        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");

        classSelection = get_class(classID);
        subClassSelection = getSubClass(classID);
        level = getLevel(classID);

        initializeViews();

        personalizedViews();

        pullPurchaseStatus();

        purchasePressed();



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
        else return null;
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

    public void initializeViews(){
        textViewPremiumDescription = findViewById(R.id.textViewPremiumDescription);
        textViewPremiumBar = findViewById(R.id.textViewPremiumBar);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonBeginner = findViewById(R.id.radioButtonBeginner);
        radioButtonIntermediate = findViewById(R.id.radioButtonIntermediate);
        radioButtonAdvanced = findViewById(R.id.radioButtonAdvanced);
        radioButtonAll = findViewById(R.id.radioButtonAll);
        purchaseButton = findViewById(R.id.purchaseBtn);
        imageViewPremiumClassBanner = findViewById(R.id.imageViewPremiumClassBanner);

    }

    public void personalizedViews(){

        switch (classSelection){
            case "Nurul Bayan":{

                imageViewPremiumClassBanner.setBackgroundResource(R.drawable.classlist_quran);
                textViewPremiumBar.setText(R.string.nurul_bayan);
                textViewPremiumDescription.setText(R.string.nurul_bayan_description);

                switch(level){
                    case "Beginner":{
                        radioButtonBeginner.setChecked(true);
                        break;
                    }
                    case "Intermediate":{
                        radioButtonIntermediate.setChecked(true);
                        break;
                    }
                    case "Advanced":{
                        radioButtonAdvanced.setChecked(true);
                        break;
                    }
                }

                break;
            }
            case "Khat":{
                switch(subClassSelection){

                    case "Riqah":{
                        imageViewPremiumClassBanner.setBackgroundResource(R.drawable.banner_khat_riqah);
                        textViewPremiumBar.setText(R.string.riqah);
                        textViewPremiumDescription.setText(R.string.riqah_description);

                        switch(level){
                            case "Beginner":{
                                radioButtonBeginner.setChecked(true);
                                break;
                            }
                            case "Intermediate":{
                                radioButtonIntermediate.setChecked(true);
                                break;
                            }
                            case "Advanced":{
                                radioButtonAdvanced.setChecked(true);
                                break;
                            }
                        }
                        break;
                    }
                    case "Nasakh":{
                        imageViewPremiumClassBanner.setBackgroundResource(R.drawable.banner_khat_nasakh);
                        textViewPremiumBar.setText(R.string.nasakh);
                        textViewPremiumDescription.setText(R.string.nasakh_description);

                        switch(level){
                            case "Beginner":{
                                radioButtonBeginner.setChecked(true);
                                break;
                            }
                            case "Intermediate":{
                                radioButtonIntermediate.setChecked(true);
                                break;
                            }
                            case "Advanced":{
                                radioButtonAdvanced.setChecked(true);
                                break;
                            }
                        }
                        break;
                    }
                    case "Farisi":{
                        imageViewPremiumClassBanner.setBackgroundResource(R.drawable.banner_khat_farisi);
                        textViewPremiumBar.setText(R.string.farisi);
                        textViewPremiumDescription.setText(R.string.farisi_description);

                        switch(level){
                            case "Beginner":{
                                radioButtonBeginner.setChecked(true);
                                break;
                            }
                            case "Intermediate":{
                                radioButtonIntermediate.setChecked(true);
                                break;
                            }
                            case "Advanced":{
                                radioButtonAdvanced.setChecked(true);
                                break;
                            }
                        }
                        break;
                    }
                    case "Diwani":{
                        imageViewPremiumClassBanner.setBackgroundResource(R.drawable.banner_khat_diwani);
                        textViewPremiumBar.setText(R.string.diwani);
                        textViewPremiumDescription.setText(R.string.diwani_description);

                        switch(level){
                            case "Beginner":{
                                radioButtonBeginner.setChecked(true);
                                break;
                            }
                            case "Intermediate":{
                                radioButtonIntermediate.setChecked(true);
                                break;
                            }
                            case "Advanced":{
                                radioButtonAdvanced.setChecked(true);
                                break;
                            }
                        }
                        break;
                    }
                    case "Diwani Jali":{
                        imageViewPremiumClassBanner.setBackgroundResource(R.drawable.banner_khat_diwani_jali);
                        textViewPremiumBar.setText(R.string.diwani_jali);
                        textViewPremiumDescription.setText(R.string.diwani_jali_description);

                        switch(level){
                            case "Beginner":{
                                radioButtonBeginner.setChecked(true);
                                break;
                            }
                            case "Intermediate":{
                                radioButtonIntermediate.setChecked(true);
                                break;
                            }
                            case "Advanced":{
                                radioButtonAdvanced.setChecked(true);
                                break;
                            }
                        }
                        break;
                    }
                    case "Thuluth":{
                        imageViewPremiumClassBanner.setBackgroundResource(R.drawable.banner_khat_thuluth);
                        textViewPremiumBar.setText(R.string.thuluth);
                        textViewPremiumDescription.setText(R.string.thuluth_description);

                        switch(level){
                            case "Beginner":{
                                radioButtonBeginner.setChecked(true);
                                break;
                            }
                            case "Intermediate":{
                                radioButtonIntermediate.setChecked(true);
                                break;
                            }
                            case "Advanced":{
                                radioButtonAdvanced.setChecked(true);
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }

    }

    public void pullPurchaseStatus(){


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String classID_beginner, classID_intermediate, classID_advanced;

        classID_beginner = specialClassNameConverter(classSelection,subClassSelection,"Beginner");
        classID_intermediate = specialClassNameConverter(classSelection,subClassSelection,"Intermediate");
        classID_advanced = specialClassNameConverter(classSelection, subClassSelection, "Advanced");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("class_details").child(classID_beginner).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String purchased = dataSnapshot.child("purchase").getValue().toString();
                System.out.println("Beginner : "+purchased);

                if(purchased.equals("true") || purchased.equals("requested")|| purchased.equals("approved")){
                    radioButtonBeginner.setEnabled(false);
                    radioButtonAll.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("class_details").child(classID_intermediate).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String purchased = dataSnapshot.child("purchase").getValue().toString();
                System.out.println("Intermediate : "+purchased);
                if(purchased.equals("true") || purchased.equals("requested")|| purchased.equals("approved")){
                    radioButtonIntermediate.setEnabled(false);
                    radioButtonAll.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("class_details").child(classID_advanced).child("0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String purchased = dataSnapshot.child("purchase").getValue().toString();
                System.out.println("Advanced : "+purchased);
                if(purchased.equals("true") || purchased.equals("requested") || purchased.equals("approved")){
                    radioButtonAdvanced.setEnabled(false);
                    radioButtonAll.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String specialClassNameConverter(String classSelection, String subClassSelection, String level){
        String new_class_name;

        if(subClassSelection==null){
            new_class_name = "nurul_bayan_"+level.toLowerCase();
        }else if(subClassSelection.equals("Diwani Jali"))
            new_class_name = classSelection.toLowerCase()+"_"+"diwani_jali"+"_"+level.toLowerCase();
        else
            new_class_name = classSelection.toLowerCase()+"_"+subClassSelection.toLowerCase()+"_"+level.toLowerCase();

        return new_class_name;
    }

    public void purchasePressed(){

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseClassActivity.this, PurchaseInstructionActivity.class);;
                Bundle bundle = new Bundle();

                String classID_beginner, classID_intermediate, classID_advanced;

                classID_beginner = specialClassNameConverter(classSelection,subClassSelection,"Beginner");
                classID_intermediate = specialClassNameConverter(classSelection,subClassSelection,"Intermediate");
                classID_advanced = specialClassNameConverter(classSelection, subClassSelection, "Advanced");


                if(radioButtonBeginner.isChecked()){
                    bundle.putString("classID",classID_beginner);
                }else if(radioButtonIntermediate.isChecked()){
                    bundle.putString("classID",classID_intermediate);
                }else if(radioButtonAdvanced.isChecked()){
                    bundle.putString("classID",classID_advanced);
                }else {
                    String[] setClassID = new String[3];
                    setClassID[0]=classID_beginner;
                    setClassID[1]=classID_intermediate;
                    setClassID[2]=classID_advanced;

                    bundle.putStringArray("classIDArray",setClassID);
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
