package com.example.sbaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.legacy.app.ActionBarDrawerToggle;

import com.bumptech.glide.Glide;
import com.example.sbaapp.Account.AccountSetting;
import com.example.sbaapp.Account.Login;
import com.example.sbaapp.Account.Register;
import com.example.sbaapp.ClassVideoPackage.ClassList;


import com.example.sbaapp.Dashboard.UserDashboardActivity;
import com.example.sbaapp.Quran.MainQuranActivity;
import com.example.sbaapp.YoutubeIntegration.YoutubeChannel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{
    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private TextView welcomeText;
    private ImageView announcementImageView, logoImageView;
    private volatile Boolean adminCheckBoolean =false;
    private int itemCount = 0;
    private String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set username textview
        welcomeText = (TextView) findViewById(R.id.welcomeText);

        //check if user exists (logged in)
        mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth.getCurrentUser());

        //To check if user is admin or not
        if(mAuth.getCurrentUser()!=null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                    String adminCheck = dataSnapshot.child("admin").getValue().toString();

                    welcomeText.setText("Welcome "+name+"!");
                    adminCheckBoolean = Boolean.parseBoolean(adminCheck);
                    //Toast.makeText(MainActivity.this, "adminCheck: "+adminCheck,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //To check if there are announcements
        logoImageView = findViewById(R.id.logoImageView);
        announcementImageView = findViewById(R.id.announcementImageView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcement");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gotAnnouncement;

                gotAnnouncement = dataSnapshot.child("announcement_link").getValue().toString();

                if(gotAnnouncement.equals("default")){
                    logoImageView.setVisibility(View.VISIBLE);
                    announcementImageView.setVisibility(View.INVISIBLE);
                    //announcementImageView.setImageResource(R.drawable.logo);
                }
                else{
                    logoImageView.setVisibility(View.INVISIBLE);
                    announcementImageView.setVisibility(View.VISIBLE);
                    Glide.with(MainActivity.this)
                            .load(Uri.parse(gotAnnouncement))
                            .override(600,500)
                            .into(announcementImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(mAuth.getCurrentUser()!=null){
            String uid = mAuth.getCurrentUser().getUid();

            getNotification(uid);
        }


        //Navigation Drawer start
        dl=(DrawerLayout) findViewById(R.id.activity_main);
        t=new ActionBarDrawerToggle(MainActivity.this,dl,R.drawable.ic_hamburger_nav,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        //t.setHomeAsUpIndicator(R.drawable.ic_hamburger_nav);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_nav);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = (NavigationView) findViewById(R.id.nv);
        if(mAuth.getCurrentUser()==null){
            nv.inflateMenu(R.menu.navigation_menu);
        }else{
            nv.inflateMenu(R.menu.navigation_menu_logged_in);
        }
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                switch (id){
                    case R.id.home:
                        break;
                    case R.id.login:
                         if(mAuth.getCurrentUser()==null)
                            startActivity(new Intent(MainActivity.this, Login.class));
                         else{
                             Toast.makeText(MainActivity.this, "You have signed in. Logout to change account",Toast.LENGTH_LONG).show();
                         }
                             break;
                    case R.id.register:
                         startActivity(new Intent(MainActivity.this, Register.class));break;
                    case R.id.settings:
                         if(mAuth.getCurrentUser()!=null)
                            startActivity(new Intent(MainActivity.this, AccountSetting.class));
                         else{
                             Toast.makeText(MainActivity.this, "You have not signed in.",Toast.LENGTH_LONG).show();
                         }
                         break;
                    case R.id.notification:
                        startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                         break;
                    case R.id.chat:{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("You are about to exit the app");
                        builder.setMessage("Are you sure you want to redirect to Whatsapp?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String number = "601128870677";
                                        String text = "Hi, I have questions regarding Shaikh Bakry Ayoub app:\n\n ";
                                        String url = "https://api.whatsapp.com/send?phone="+number+"&text="+text;
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    }
                    case R.id.admin:
                        if(adminCheckBoolean==false){
                            Toast.makeText(MainActivity.this, "Access for Admins only",Toast.LENGTH_LONG).show();
                        }else
                        startActivity(new Intent (MainActivity.this, AdminMainActivity.class));
                        break;

                    case R.id.logout:
                        Toast.makeText(MainActivity.this,"User signed out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                        break;

                    default:
                        return true;
                }

                return true;
            }
        });
        //Navigation Drawer end


        mTextMessage = findViewById(R.id.message);
        welcomeText = findViewById(R.id.welcomeText);
        ImageButton openQuranBtn = findViewById(R.id.openQuranBtn);
        ImageButton openClassBtn = findViewById(R.id.openClassBtn);
        ImageButton openAssessmentBtn = findViewById(R.id.openAssessmentBtn);
        ImageView openYoutubeBtn = findViewById(R.id.openYoutubeBtn);
        /*
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            Toast.makeText(MainActivity.this,"User has not signed in",Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            //welcomeText.setText(currentUser.getDisplayName());
            Toast.makeText(MainActivity.this,"User has signed in",Toast.LENGTH_SHORT).show();
        */
        //App Functions
        openQuranBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, MainQuranActivity.class));
            }
        });

        openClassBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(mAuth.getCurrentUser()!=null){
                startActivity(new Intent(MainActivity.this, ClassList.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "Please login to view the classes",Toast.LENGTH_LONG).show();
                }
            }
        });

        openYoutubeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, YoutubeChannel.class));
            }
        });

        openAssessmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "Please login to view your assessment progress",Toast.LENGTH_LONG).show();
                }
            }
        });


        /*Temporarily comment for testing
        openClassBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, ClassVideo.class));
            }
        });
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void getNotification(final String uid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(uid)){
                    System.out.println("In notification "+ dataSnapshot.child(uid).getValue().toString());

                    for (DataSnapshot messageSnapshot : dataSnapshot.child(uid).getChildren()) {

                        System.out.println("MessageSnapShot: "+messageSnapshot.getValue());
                        itemCount++;
                    }
                    System.out.println("Itemcount: "+itemCount);

                    if(itemCount>0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("You have " + itemCount + " notifications!");
                        builder.setMessage("Click View to view notifications");
                        builder.setPositiveButton("View",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        itemCount=0;
                                        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                                        startActivity(intent);

                                        //finish();

                                    }
                                });
                        builder.setNegativeButton("Ignore", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            finish();
        }
        else
            Toast.makeText(MainActivity.this,"User has signed in",Toast.LENGTH_SHORT).show();
    }
    */



}
