package com.example.sbaapp.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.sbaapp.AdminFeedbackFragment.FragmentFeedbackKhat;
import com.example.sbaapp.AdminFeedbackFragment.FragmentFeedbackNurulBayan;
import com.example.sbaapp.R;

public class AdminDashboardItemFeedback extends AppCompatActivity {

    private String classID, uid, email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_item_feedback);

        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");
        uid = bundle.getString("uid");
        email = bundle.getString("email");
        name = bundle.getString("name");

        System.out.println(classID);
        System.out.println(uid);
        System.out.println(email);
        System.out.println(name);

        Fragment fragment = null;
        if(classID.contains("nurul_bayan")){
            fragment = new FragmentFeedbackNurulBayan();
        }else if(classID.contains("khat")){
            fragment = new FragmentFeedbackKhat();
        }else{
            System.out.println("Failed getting fragment");
        }

        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.FragOutput, fragment);
        transaction.commit();


    }
}
