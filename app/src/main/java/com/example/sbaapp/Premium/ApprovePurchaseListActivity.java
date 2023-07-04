package com.example.sbaapp.Premium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sbaapp.Dashboard.AdminDashboardItemFeedback;
import com.example.sbaapp.Dashboard.AdminDashboardItemView;
import com.example.sbaapp.Dashboard.AdminDashboardItemViewCompleted;
import com.example.sbaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApprovePurchaseListActivity extends AppCompatActivity {


    final int SIZE = 50;
    volatile int itemCount = 0;

    private String[] EMAIL = new String[SIZE];
    private String[] UID = new String[SIZE];
    private String[] NAMES = new String[SIZE];
    private String[] CLASS = new String[SIZE];
    private String[] PURCHASE_LINK = new String[SIZE];
    private String[] PURCHASE_MESSAGE = new String[SIZE];
    private String[] PURCHASE_STATUS = new String[SIZE];

    private volatile String name;
    private String[] classID;

    private TextView nameTV, classTV,emailTV, messageTV;
    private Button openAssessmentBtn;
    private Boolean doneSetup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_approve_purchase_list);
//        ListView listView = findViewById(R.id.listView);


//        Bundle bundle = getIntent().getExtras();
//        classID = bundle.getStringArray("classID");

        //Retrieve data from database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



            databaseReference.child("Purchase").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("In DataSnapshot :"+dataSnapshot.toString());

                    for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                        for(DataSnapshot userSnapshot : classSnapshot.getChildren()){
                            String status = userSnapshot.child("purchase_status").getValue(String.class);
                            if (status.equals("requested")) {
                                UID[itemCount] = userSnapshot.child("uid").getValue(String.class);
                                EMAIL[itemCount] = userSnapshot.child("email").getValue(String.class);
                                NAMES[itemCount] = userSnapshot.child("name").getValue(String.class);
                                CLASS[itemCount] = userSnapshot.child("classID").getValue(String.class);
                                PURCHASE_LINK[itemCount] = userSnapshot.child("purchase_link").getValue(String.class);
                                PURCHASE_MESSAGE[itemCount] = userSnapshot.child("purchase_message").getValue(String.class);
                                PURCHASE_STATUS[itemCount] = userSnapshot.child("purchase_status").getValue(String.class);

                                System.out.println("Get name in loop at "+itemCount+": "+NAMES[itemCount]);

                                itemCount++;
                            }

                        }

                    }


                    setContentView(R.layout.activity_approve_purchase_list);
                    ListView listView = findViewById(R.id.listView);
                    //if(doneSetup==false) {

                    //System.out.println("Masuk initialize setup");

                    CustomAdapter customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            Intent intent = new Intent(ApprovePurchaseListActivity.this, ApprovePurchaseActivity.class);

                            Bundle bundle = new Bundle();

                            bundle.putString("classID", CLASS[position]);
                            bundle.putString("uid", UID[position]);
                            bundle.putString("name", NAMES[position]);
                            bundle.putString("email", EMAIL[position]);
                            bundle.putString("purchase_message", PURCHASE_MESSAGE[position]);
                            bundle.putString("purchase_link", PURCHASE_LINK[position]);
                            bundle.putString("purchase_status", PURCHASE_STATUS[position]);


                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    });

                    BottomNavigationView navigationView = findViewById(R.id.bottomNavViewApproval);
                    navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.nav_requested:

                                    break;
                                case R.id.nav_done:
                                    Intent intent = new Intent(ApprovePurchaseListActivity.this, AdminDashboardItemViewCompleted.class);

                                    Bundle bundle = new Bundle();


                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                    break;
                            }

                            return false;


                        }
                    });
                    doneSetup=true;


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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


            view = getLayoutInflater().inflate(R.layout.custom_approval_layout, null);

            nameTV =  view.findViewById(R.id.nameTV);
            classTV = view.findViewById(R.id.classTV);
            emailTV = view.findViewById(R.id.emailTV);
            messageTV = view.findViewById(R.id.messageTV);
            openAssessmentBtn = view.findViewById(R.id.openAssessmentBtn);


            nameTV.setText(NAMES[position]);
            classTV.setText(classIdConverter(CLASS[position]));
            emailTV.setText(EMAIL[position]);
            messageTV.setText(PURCHASE_MESSAGE[position]);
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
