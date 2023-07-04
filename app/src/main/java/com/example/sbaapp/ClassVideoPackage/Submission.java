package com.example.sbaapp.ClassVideoPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sbaapp.R;
import com.example.sbaapp.SubmissionFragment.FragmentKhatAdvanced;
import com.example.sbaapp.SubmissionFragment.FragmentKhatBeginner;
import com.example.sbaapp.SubmissionFragment.FragmentKhatIntermediate;
import com.example.sbaapp.SubmissionFragment.FragmentNurulBayanAdvanced;
import com.example.sbaapp.SubmissionFragment.FragmentNurulBayanBeginner;
import com.example.sbaapp.SubmissionFragment.FragmentNurulBayanIntermediate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Submission extends AppCompatActivity{


    private String classSelection, subClassSelection, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        Bundle bundle = getIntent().getExtras();
        classSelection = bundle.getString("class");
        level = bundle.getString("level");
        subClassSelection = bundle.getString("subClass");


                Fragment fragment = null;
                if (classSelection.equals("Khat")&& level.equals("Beginner")){
                    fragment = new FragmentKhatBeginner();
                }else if(classSelection.equals("Khat")&& level.equals("Intermediate")){
                    fragment = new FragmentKhatIntermediate();
                }else if(classSelection.equals("Khat")&& level.equals("Advanced")) {
                    fragment = new FragmentKhatAdvanced();
                }else if(classSelection.equals("Nurul Bayan")&& level.equals("Beginner")) {
                    fragment = new FragmentNurulBayanBeginner();
                }else if(classSelection.equals("Nurul Bayan")&& level.equals("Intermediate")) {
                    fragment = new FragmentNurulBayanIntermediate();
                }else if(classSelection.equals("Nurul Bayan")&& level.equals("Advanced")) {
                    fragment = new FragmentNurulBayanAdvanced();
                }
                else {
                    System.out.println("Failed access fragment");
                }

                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.FragOutput, fragment);
                transaction.commit();







        BottomNavigationView navigationView = findViewById(R.id.bottomNavViewVideoList);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_videoList:
                        Intent intent = new Intent(Submission.this, VideoList.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("class", classSelection);
                        bundle.putString("level", level);
                        bundle.putString("subClass", subClassSelection);


                        intent.putExtras(bundle);

                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_submission:

                        break;
                }

                return false;
            }
        });

    }



}
