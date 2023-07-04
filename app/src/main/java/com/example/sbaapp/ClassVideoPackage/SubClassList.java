package com.example.sbaapp.ClassVideoPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sbaapp.Premium.PurchaseClassActivity;
import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class SubClassList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private ImageView imageViewDemo1, imageViewDemo2;
    private ImageButton BeginnerBtn, IntermediateBtn, AdvancedBtn,DiwaniJaliBtn, ThuluthBtn, DiwaniBtn, FarisiBtn, NasakhBtn, RiqahBtn;
    private TextView TVDemo1, TVDemo2;
    private String[] demoUri = new String[2];
    private String[] demoUriThumbnail = new String[2];
    private String[] demoTitle = new String[2];
    //private Spinner spinnerSubClass;
    private String classSelection, subClassSelection;

    private int demoCount =0;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_class_list);

        //Retrieve class type from parent class (ClassList)
        Bundle bundle = getIntent().getExtras();
        classSelection = bundle.getString("class");

        //Toast.makeText(this, classSelection, Toast.LENGTH_SHORT).show();

        //Declare variables
        imageViewDemo1 = (ImageView)findViewById(R.id.imageViewDemo1);
        imageViewDemo2 = (ImageView)findViewById(R.id.imageViewDemo2);

        BeginnerBtn = (ImageButton)findViewById(R.id.BeginnerBtn);
        IntermediateBtn = (ImageButton)findViewById(R.id.IntermediateBtn);
        AdvancedBtn = (ImageButton)findViewById(R.id.AdvancedBtn);

        DiwaniJaliBtn =findViewById(R.id.DiwaniJaliBtn);
        ThuluthBtn = findViewById(R.id.ThuluthBtn);
        DiwaniBtn = findViewById(R.id.DiwaniBtn);
        FarisiBtn = findViewById(R.id.FarisiBtn);
        NasakhBtn = findViewById(R.id.NasakhBtn);
        RiqahBtn = findViewById(R.id.RiqahBtn);

        BeginnerBtn.setVisibility(View.INVISIBLE);
        IntermediateBtn.setVisibility(View.INVISIBLE);
        AdvancedBtn.setVisibility(View.INVISIBLE);

        DiwaniJaliBtn.setVisibility(View.INVISIBLE);
        ThuluthBtn.setVisibility(View.INVISIBLE);
        DiwaniBtn.setVisibility(View.INVISIBLE);
        FarisiBtn.setVisibility(View.INVISIBLE);
        NasakhBtn.setVisibility(View.INVISIBLE);
        RiqahBtn.setVisibility(View.INVISIBLE);


        TVDemo1 = (TextView)findViewById(R.id.TVDemo1);
        TVDemo2 = (TextView)findViewById(R.id.TVDemo2);


        //Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ClassList").child("Class"+classSelection).child("DemoVideo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot demoVideoSnapshot: dataSnapshot.getChildren()){

                    System.out.println("Entering for loop at demoCount : "+demoCount);
                    demoUriThumbnail[demoCount] = demoVideoSnapshot.child("Thumbnail").getValue().toString();
                    demoTitle[demoCount] = demoVideoSnapshot.child("Title").getValue().toString();
                    demoUri[demoCount] = demoVideoSnapshot.child("VideoUrl").getValue().toString();
                    demoCount++;
                }



                if(classSelection.equals("Nurul Bayan")){

                    System.out.println("Retrieving Thumbnail");
                    new DownloadImageTask((ImageView) findViewById(R.id.imageViewDemo1))
                            .execute(demoUriThumbnail[0]);
                    new DownloadImageTask((ImageView) findViewById(R.id.imageViewDemo2))
                            .execute(demoUriThumbnail[1]);

                    System.out.println("Retrieving Demo Title");

                    TVDemo1.setText(demoTitle[0]);
                    TVDemo2.setText(demoTitle[1]);

                    System.out.println("Setting Image Resources");


                    BeginnerBtn.setBackgroundResource(R.drawable.subclass_beginner_nurul_bayan);
                    IntermediateBtn.setBackgroundResource(R.drawable.subclass_intermediate_nurul_bayan);
                    AdvancedBtn.setBackgroundResource(R.drawable.subclass_advanced_nurul_bayan);

                    BeginnerBtn.setVisibility(View.VISIBLE);
                    IntermediateBtn.setVisibility(View.VISIBLE);
                    AdvancedBtn.setVisibility(View.VISIBLE);

                    //Navigate to ClassVideo for Demo classes
                    imageViewDemo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[0],demoTitle[0]);
                        }
                    });

                    TVDemo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[0],demoTitle[0]);
                        }
                    });

                    imageViewDemo2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[1],demoTitle[1]);
                        }
                    });

                    TVDemo2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[1],demoTitle[1]);
                        }
                    });

                    //Display Nurul Bayan class selections


                    //If the Class Buttons are selected, redirect to respective VideoList (Beginner, Intermediate, Advance)

                    BeginnerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            videoListNavigate(classSelection, null, "Beginner");
                        }
                    });

                    IntermediateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            videoListNavigate(classSelection, null, "Intermediate");
                        }
                    });

                    AdvancedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            videoListNavigate(classSelection, null, "Advanced");
                        }
                    });

                }

                else if(classSelection.equals("Khat")){
                    //Toast.makeText(SubClassList.this, "Entering Khat", Toast.LENGTH_LONG).show();

                    System.out.println("Retrieving Thumbnail");
                    new DownloadImageTask((ImageView) findViewById(R.id.imageViewDemo1))
                            .execute(demoUriThumbnail[0]);
                    new DownloadImageTask((ImageView) findViewById(R.id.imageViewDemo2))
                            .execute(demoUriThumbnail[1]);

                    System.out.println("Retrieving Demo Title");

                    TVDemo1.setText(demoTitle[0]);
                    TVDemo2.setText(demoTitle[1]);

                    System.out.println("Setting Image Resources");


                    BeginnerBtn.setBackgroundResource(R.drawable.subclass_beginner_khat);
                    IntermediateBtn.setBackgroundResource(R.drawable.subclass_intermediate_khat);
                    AdvancedBtn.setBackgroundResource(R.drawable.subclass_advanced_khat);

                    DiwaniJaliBtn.setBackgroundResource(R.drawable.subclass_khat_diwani_jali);
                    ThuluthBtn.setBackgroundResource(R.drawable.subclass_khat_thuluth);
                    DiwaniBtn.setBackgroundResource(R.drawable.subclass_khat_diwani);
                    FarisiBtn.setBackgroundResource(R.drawable.subclass_khat_farisi);
                    NasakhBtn.setBackgroundResource(R.drawable.subclass_khat_nasakh);
                    RiqahBtn.setBackgroundResource(R.drawable.subclass_khat_riqah);

                    DiwaniJaliBtn.setVisibility(View.VISIBLE);
                    ThuluthBtn.setVisibility(View.VISIBLE);
                    DiwaniBtn.setVisibility(View.VISIBLE);
                    FarisiBtn.setVisibility(View.VISIBLE);
                    NasakhBtn.setVisibility(View.VISIBLE);
                    RiqahBtn.setVisibility(View.VISIBLE);

                    //Navigate to ClassVideo for Demo classes
                    imageViewDemo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[0],demoTitle[0]);
                        }
                    });

                    TVDemo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[0],demoTitle[0]);                        }
                    });

                    imageViewDemo2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[1],demoTitle[1]);                        }
                    });

                    TVDemo2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoVideoClick(demoUri[1],demoTitle[1]);
                        }
                    });

                    //If the Class Buttons are selected, redirect to respective VideoList (Beginner, Intermediate, Advance)

                    DiwaniJaliBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subClassSelection="Diwani Jali";
                            subClassTrigger();
                        }
                    });

                    ThuluthBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subClassSelection="Thuluth";
                            subClassTrigger();
                        }
                    });

                    DiwaniBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subClassSelection="Diwani";
                            subClassTrigger();
                        }
                    });

                    FarisiBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subClassSelection="Farisi";
                            subClassTrigger();
                        }
                    });

                    NasakhBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subClassSelection="Nasakh";
                            subClassTrigger();
                        }
                    });

                    RiqahBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            subClassSelection="Riqah";
                            subClassTrigger();
                        }
                    });




                    BeginnerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoListNavigate(classSelection, subClassSelection, "Beginner");
                        }
                    });

                    IntermediateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoListNavigate(classSelection, subClassSelection, "Intermediate");
                        }
                    });

                    AdvancedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoListNavigate(classSelection, subClassSelection, "Advanced");
                        }
                    });
                    System.out.println("Ready to display screen!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //spinnerSubClass.setVisibility(View.INVISIBLE);
        BeginnerBtn.setVisibility(View.VISIBLE);
        IntermediateBtn.setVisibility(View.VISIBLE);
        AdvancedBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    public void demoVideoClick(String url, String title){
        Intent intent = new Intent(SubClassList.this, ClassVideo.class);

        Bundle bundle = new Bundle();

        bundle.putString("video_url", url);
        bundle.putString("video_title",title);

        intent.putExtras(bundle);

        startActivity(intent);

    }

    public void videoListNavigate(String classSelection, String subClassSelection, String level){


        checkPurchase(classSelection,subClassSelection,level,false);


    }

    public void subClassTrigger(){

        DiwaniBtn.setVisibility(View.INVISIBLE);
        FarisiBtn.setVisibility(View.INVISIBLE);
        NasakhBtn.setVisibility(View.INVISIBLE);
        RiqahBtn.setVisibility(View.INVISIBLE);
        DiwaniJaliBtn.setVisibility(View.INVISIBLE);
        ThuluthBtn.setVisibility(View.INVISIBLE);


        BeginnerBtn.setVisibility(View.VISIBLE);
        IntermediateBtn.setVisibility(View.VISIBLE);
        AdvancedBtn.setVisibility(View.VISIBLE);
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

    public void checkPurchase(final String classSelection, final String subClassSelection, final String level, Boolean have){

        String uid = FirebaseAuth.getInstance().getUid();
        String classID = specialClassNameConverter(classSelection,subClassSelection,level);
        System.out.println(classID);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("class_details").child(classID).child("0");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String purchased = dataSnapshot.child("purchase").getValue(String.class);
                    System.out.println("Purchased: " +purchased);
                    Boolean canAccess,pending;
                    if (purchased.equals("false")) {
                        //proceed[0] = false;
                        canAccess =false;
                        pending=false;
                    } else if(purchased.equals("requested")){
                        //proceed[0] = true;
                        canAccess = false;
                        pending = true;

                    } else{
                        canAccess =true;
                        pending=false;
                    }



                    if(!canAccess && !pending){

                        Toast.makeText(SubClassList.this, "Redirecting to purchase screen...", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(SubClassList.this, PurchaseClassActivity.class);

                        Bundle bundle = new Bundle();

                        String classID = specialClassNameConverter(classSelection,subClassSelection,level);
                        bundle.putString("classID", classID);

                        intent.putExtras(bundle);

                        startActivity(intent);


                    }else if(!canAccess && pending){

                        Toast.makeText(SubClassList.this, "Your payment request is pending. Please view request updates in User Notification", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Intent intent = new Intent(SubClassList.this, VideoList.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("class", classSelection);
                        bundle.putString("level", level);
                        bundle.putString("subClass", subClassSelection);


                        intent.putExtras(bundle);

                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }
}
