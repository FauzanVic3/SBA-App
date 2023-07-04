package com.example.sbaapp.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.sbaapp.AssessmentFragment.FragmentAssessmentKhat;
import com.example.sbaapp.AssessmentFragment.FragmentAssessmentKhatAdvanced;
import com.example.sbaapp.AssessmentFragment.FragmentAssessmentNurulBayan;
import com.example.sbaapp.R;
import com.example.sbaapp.SubmissionFragment.FragmentKhatAdvanced;
import com.example.sbaapp.SubmissionFragment.FragmentKhatBeginner;
import com.example.sbaapp.SubmissionFragment.FragmentKhatIntermediate;
import com.example.sbaapp.SubmissionFragment.FragmentNurulBayanAdvanced;
import com.example.sbaapp.SubmissionFragment.FragmentNurulBayanBeginner;
import com.example.sbaapp.SubmissionFragment.FragmentNurulBayanIntermediate;

public class UserDashboardItemView extends AppCompatActivity {

    private String classSelection, level, subClassSelection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_item_view);

        Bundle bundle = getIntent().getExtras();
        classSelection = bundle.getString("class");
        level = bundle.getString("level");
        subClassSelection = bundle.getString("subClass");


        Fragment fragment = null;
        if (classSelection.equals("Khat")&& level.equals("Beginner")){
            fragment = new FragmentAssessmentKhat();
        }else if(classSelection.equals("Khat")&& level.equals("Intermediate")){
            fragment = new FragmentAssessmentKhat();
        }else if(classSelection.equals("Khat")&& level.equals("Advanced")) {
            //fragment = new FragmentAssessmentKhatAdvanced();
            fragment = new FragmentAssessmentKhat();
        }else if(classSelection.equals("Nurul Bayan")) {
            fragment = new FragmentAssessmentNurulBayan();
        }
        else {
            System.out.println("Failed access fragment");
        }



        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.FragOutput, fragment);
        transaction.commit();

    }
}
