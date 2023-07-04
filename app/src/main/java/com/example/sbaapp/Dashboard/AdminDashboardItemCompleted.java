package com.example.sbaapp.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.sbaapp.AdminFeedbackFragment.FragmentFeedbackKhat;
import com.example.sbaapp.AdminFeedbackFragment.FragmentFeedbackNurulBayan;
import com.example.sbaapp.AssessmentFragment.FragmentAssessmentKhat;
import com.example.sbaapp.AssessmentFragment.FragmentAssessmentNurulBayan;
import com.example.sbaapp.R;

public class AdminDashboardItemCompleted extends AppCompatActivity {

    private String classID, uid, email, name;
    private String classSelection, level, subClassSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_item_completed);

        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");
        uid = bundle.getString("uid");
        email = bundle.getString("email");
        name = bundle.getString("name");

//        classSelection = bundle.getString("class");
//        level = bundle.getString("level");
//        subClassSelection = bundle.getString("subClass");
//
        classSelection = get_class(classID);
        subClassSelection = getSubClass(classID);
        level = getLevel(classID);

        Bundle new_bundle = new Bundle();
        new_bundle.putString("class",classSelection);
        new_bundle.putString("subClass",subClassSelection);
        new_bundle.putString("level",level);
        new_bundle.putString("uid",uid);


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

        fragment.setArguments(new_bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.FragOutput, fragment);
        transaction.commit();
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

}
