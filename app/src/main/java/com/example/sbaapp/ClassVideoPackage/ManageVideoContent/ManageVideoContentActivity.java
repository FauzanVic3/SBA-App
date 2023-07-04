package com.example.sbaapp.ClassVideoPackage.ManageVideoContent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sbaapp.R;

public class ManageVideoContentActivity extends AppCompatActivity {

    private Button uploadBtn, editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_video_content);

        editBtn = findViewById(R.id.editBtn);
        uploadBtn = findViewById(R.id.uploadBtn);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editVideo();
            }
        });

    }

    public void editVideo(){
        Intent intent = new Intent(ManageVideoContentActivity.this,EditVideoCategory.class );
        startActivity(intent);
    }

    public void uploadVideo(){
        Intent intent = new Intent(ManageVideoContentActivity.this,UploadVideoContent.class );
        startActivity(intent);
    }
}
