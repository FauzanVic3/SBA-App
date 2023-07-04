package com.example.sbaapp.ClassVideoPackage.ManageVideoContent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sbaapp.R;

public class EditVideoCategory extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Spinner spinnerLevel, spinnerClass, spinnerSubclass;
    private String khat, nb;
    private int subclassNum;
    private Button proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video_category);

        initializeViews();

        proceedBtn.setOnClickListener(this);

        //Dropdown adapters
        ArrayAdapter<String> spinnerLevelAdapter = new ArrayAdapter<String>(EditVideoCategory.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.spinnerClassLevel));
        spinnerLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(spinnerLevelAdapter);

        ArrayAdapter<String> spinnerClassAdapter = new ArrayAdapter<String>(EditVideoCategory.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.spinnerClass));
        spinnerClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(spinnerClassAdapter);

        spinnerClass.setOnItemSelectedListener(this);
    }

    public void initializeViews(){
        //Dropdown
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubclass = findViewById(R.id.spinnerSubclass);

        proceedBtn = findViewById(R.id.proceedBtn);

        //String for Class name
        khat = "Khat";
        nb = "Nurul Bayan";
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.proceedBtn){
            Bundle bundle = new Bundle();

            if(subclassNum==1) {
                bundle.putString("class", spinnerClass.getSelectedItem().toString());
                bundle.putString("subClass", spinnerSubclass.getSelectedItem().toString());
                bundle.putString("level", spinnerLevel.getSelectedItem().toString());
            }
            else if(subclassNum==2){
                bundle.putString("class", spinnerClass.getSelectedItem().toString());
                bundle.putString("subClass", null);
                bundle.putString("level", spinnerLevel.getSelectedItem().toString());
            }
            Intent intent = new Intent(EditVideoCategory.this, EditVideoList.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String spinnerClassText= String.valueOf(spinnerClass.getSelectedItem());
        Toast.makeText(this, spinnerClassText, Toast.LENGTH_SHORT).show();
        if(spinnerClassText.contentEquals(khat)) {
            spinnerSubclass.setVisibility(View.VISIBLE);
            ArrayAdapter<String> spinnerSubclassAdapter = new ArrayAdapter<String>(EditVideoCategory.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerSubclassKhat));
            spinnerSubclassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubclass.setAdapter(spinnerSubclassAdapter);
            subclassNum = 1;
        }
        if(spinnerClassText.contentEquals(nb)) {
            //ArrayAdapter<String> spinnerSubclassAdapter = new ArrayAdapter<String>(UploadVideoContent.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerSubclassNB));
            //spinnerSubclassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spinnerSubclass.setAdapter(spinnerSubclassAdapter);
            spinnerSubclass.setVisibility(View.INVISIBLE);
            subclassNum = 2;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
