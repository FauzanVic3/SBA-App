package com.example.sbaapp.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sbaapp.ClassVideoPackage.ClassVideo;
import com.example.sbaapp.ClassVideoPackage.Submission;
import com.example.sbaapp.ClassVideoPackage.VideoList;
import com.example.sbaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboardItemView extends AppCompatActivity {

    final int SIZE = 50;
    volatile int itemCount = 0;

    private String[] EMAIL = new String[SIZE];
    private String[] UID = new String[SIZE];
    private String[] NAMES = new String[SIZE];
    private String[] CLASS = new String[SIZE];

    private volatile String name;
    private String[] classID;

    private TextView nameTV, classTV;
    private Button openAssessmentBtn;
    private Boolean doneSetup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_item_view);
        ListView listView = findViewById(R.id.listView);


        Bundle bundle = getIntent().getExtras();
        classID = bundle.getStringArray("classID");

        //Retrieve data from database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for(int i = 0; i<classID.length;i++) {
            final int finalI = i;
            databaseReference.child("Submission").child(classID[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("In DataSnapshot :"+dataSnapshot.toString());
                    if(dataSnapshot.getChildrenCount()>1) {
                        for (DataSnapshot submissionSnapshot : dataSnapshot.getChildren()) {
                            String status = submissionSnapshot.child("submission_status").getValue(String.class);

                            if(status!=null) {
                                if (status.equals("Submitted")) {
                                    UID[itemCount] = submissionSnapshot.child("uid").getValue(String.class);
                                    EMAIL[itemCount] = submissionSnapshot.child("email").getValue(String.class);
                                    NAMES[itemCount] = submissionSnapshot.child("name").getValue(String.class);
                                    CLASS[itemCount] = classID[finalI];


                                            System.out.println("Get name in loop at "+itemCount+": "+NAMES[itemCount]);

                                    itemCount++;
                                }
                            }
                        }
                    }

                    setContentView(R.layout.activity_admin_dashboard_item_view);
                    ListView listView = findViewById(R.id.listView);
                    //if(doneSetup==false) {

                        //System.out.println("Masuk initialize setup");

                        CustomAdapter customAdapter = new CustomAdapter();
                        listView.setAdapter(customAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println("click on listView in initializeSetup");

                                Intent intent = new Intent(AdminDashboardItemView.this, AdminDashboardItemFeedback.class);

                                Bundle bundle = new Bundle();

                                bundle.putString("classID", classID[position]);
                                bundle.putString("uid", UID[position]);
                                bundle.putString("name", NAMES[position]);
                                bundle.putString("email", EMAIL[position]);

                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        });

                        BottomNavigationView navigationView = findViewById(R.id.bottomNavViewAdmin);
                        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.nav_assessment:

                                        break;
                                    case R.id.nav_done:
                                        Intent intent = new Intent(AdminDashboardItemView.this, AdminDashboardItemViewCompleted.class);

                                        Bundle bundle = new Bundle();

                                        bundle.putStringArray("classID", classID);


                                        intent.putExtras(bundle);

                                        startActivity(intent);
                                        finish();
                                        break;
                                }

                                return false;


                            }
                        });
                        doneSetup=true;
                    //}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }//Exit For Loop

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("click on listView in initializeSetup");

                Intent intent = new Intent(AdminDashboardItemView.this, AdminDashboardItemFeedback.class);

                Bundle bundle = new Bundle();

                bundle.putString("classID", classID[position]);
                bundle.putString("uid", UID[position]);
                bundle.putString("names", NAMES[position]);
                bundle.putString("email", EMAIL[position]);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {


            view = getLayoutInflater().inflate(R.layout.custom_admin_layout, null);

            nameTV =  view.findViewById(R.id.nameTV);
            classTV = view.findViewById(R.id.classTV);
            openAssessmentBtn = view.findViewById(R.id.openAssessmentBtn);

            System.out.println("Retrieved name : "+NAMES[position]);
            nameTV.setText(NAMES[position]);
            classTV.setText(classIdConverter(CLASS[position]));

            return view;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //boolean hasBackPressed = data.getBooleanExtra("hasBackPressed");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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
