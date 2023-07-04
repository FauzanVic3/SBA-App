package com.example.sbaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.widget.Toast;

import com.example.sbaapp.Dashboard.AdminDashboardItemViewCompleted;
import com.example.sbaapp.Premium.ApprovePurchaseActivity;
import com.example.sbaapp.Premium.ApprovePurchaseListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {

    final int SIZE = 50;
    volatile int itemCount = 0;



    private String[] CLASS = new String[SIZE];
    private String[] MESSAGE = new String[SIZE];
    private String[] TIMESTAMP = new String[SIZE];


    private volatile String name;
    private String[] classID;
    private ImageView deleteBtn;

    private TextView nameTV, classTV,emailTV, messageTV;
    private Button openAssessmentBtn;
    private Boolean doneSetup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String uid = FirebaseAuth.getInstance().getUid();
        //Retrieve data from database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();




        databaseReference.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("uid")){

                    for (DataSnapshot messageSnapshot : dataSnapshot.child(uid).getChildren()) {
                            TIMESTAMP[itemCount] = messageSnapshot.getKey();
                            System.out.println("Timestamp: "+TIMESTAMP[itemCount]);
                            CLASS[itemCount] = messageSnapshot.child("classID").getValue(String.class);
                            MESSAGE[itemCount] = messageSnapshot.child("message").getValue(String.class);

                            itemCount++;
                    }
                }else{
                    Toast.makeText(NotificationActivity.this, "You have no notification...", Toast.LENGTH_LONG).show();
                }


                setContentView(R.layout.activity_notification);
                final ListView listView = findViewById(R.id.listView);

                final CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Delete?");
                        builder.setMessage("Are you sure you want to delete?");
                        builder.setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        listView.removeViewInLayout(view);
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notification").child(uid);
                                        databaseReference.child(TIMESTAMP[position]).removeValue();

                                        customAdapter.notifyDataSetChanged();
                                        Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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


            view = getLayoutInflater().inflate(R.layout.custom_notification_layout, null);


            classTV = view.findViewById(R.id.classTV);

            messageTV = view.findViewById(R.id.messageTV);
            deleteBtn = view.findViewById(R.id.deleteBtn);

            classTV.setText(classIdConverter(CLASS[position]));
            messageTV.setText(MESSAGE[position]);
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

    public void callAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Title");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
