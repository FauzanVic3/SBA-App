package com.example.sbaapp.Account;

//Migration to androidx

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.legacy.app.ActionBarDrawerToggle;

import com.bumptech.glide.Glide;
import com.example.sbaapp.MainActivity;
import com.example.sbaapp.NotificationActivity;
import com.example.sbaapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSetting extends AppCompatActivity  {


    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private ImageView avatarImageView;
    private TextView mName;
    private TextView mStatus;
    private TextView mEmail;
    private Button changePWBtn;
    private Button changeUsernameBtn;

    private FirebaseAuth mAuth;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        //Navigation Drawer start
        dl=(DrawerLayout) findViewById(R.id.activity_main);
        //t=new ActionBarDrawerToggle(this,dl,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        t=new ActionBarDrawerToggle(this,dl,R.drawable.ic_hamburger_nav,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                switch (id){
                    case R.id.home:
                        startActivity(new Intent(AccountSetting.this, MainActivity.class));break;
                    case R.id.login:
                        if(mAuth.getCurrentUser()==null)
                            startActivity(new Intent(AccountSetting.this, Login.class));
                        else{
                            Toast.makeText(AccountSetting.this, "You have signed in. Logout to change account",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.register:
                        startActivity(new Intent(AccountSetting.this, Register.class));break;
                    case R.id.settings:
                        startActivity(new Intent(AccountSetting.this,AccountSetting.class));
                        break;
                    case R.id.notification:
                        startActivity(new Intent(AccountSetting.this, NotificationActivity.class));
                        break;
                    case R.id.logout:
                        Toast.makeText(AccountSetting.this,"User signed out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();break;

                    default:
                        return true;
                }

                return true;
            }
        });
        //Navigation Drawer end

        avatarImageView = (ImageView) findViewById(R.id.avatarImgView);
        mName = (TextView) findViewById(R.id.usernameTextView);
        mStatus = (TextView) findViewById(R.id.statusTextView);
        mEmail = findViewById(R.id.emailTextView);
        changeUsernameBtn =findViewById(R.id.changeUsernameBtn);

        changeUsernameBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent reset = new Intent(AccountSetting.this, ResetUsername.class);
                startActivity(reset);
            }
        });


        changePWBtn = (Button) findViewById(R.id.changePWBtn);

        changePWBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent reset = new Intent(AccountSetting.this, ResetPasswordActivity.class);
                startActivity(reset);
            }
        });

        avatarImageView = findViewById(R.id.avatarImgView);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSetting.this, AddProfilePic.class);
                startActivity(intent);
            }
        });


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mEmail.setText(mCurrentUser.getEmail());


            String currentUID = mCurrentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);

            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                    mName.setText(name);
                    mStatus.setText(status);

                    if(thumb_image.equals("default")){
                        avatarImageView.setImageResource(R.drawable.ic_account);
                    }else{
                        Glide.with(AccountSetting.this)
                                .load(Uri.parse(thumb_image))
                                .override(300,200)
                                .into(avatarImageView);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
}
