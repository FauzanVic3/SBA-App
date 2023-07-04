package com.example.sbaapp.ClassVideoPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sbaapp.R;

public class ClassList extends AppCompatActivity {

    private ImageButton KhatBtn, NBBtn;
    private String classSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        KhatBtn = (ImageButton)findViewById(R.id.KhatBtn);
        NBBtn = (ImageButton)findViewById(R.id.NBBtn);

        KhatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassList.this, SubClassList.class);
                classSelection = "Khat";

                Bundle bundle = new Bundle();

                bundle.putString("class", classSelection);

                intent.putExtras(bundle);

                startActivity(intent);
                //finish();
            }
        });

        NBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassList.this, SubClassList.class);
                classSelection = "Nurul Bayan";

                Bundle bundle = new Bundle();

                bundle.putString("class", classSelection);

                intent.putExtras(bundle);

                startActivity(intent);
                //finish();
            }
        });
    }
}
