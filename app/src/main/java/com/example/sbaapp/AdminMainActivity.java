package com.example.sbaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.sbaapp.ClassVideoPackage.ManageVideoContent.ManageVideoContentActivity;
import com.example.sbaapp.ClassVideoPackage.ManageVideoContent.UploadVideoContent;
import com.example.sbaapp.Dashboard.AdminDashboardActivity;
import com.example.sbaapp.Premium.ApprovePurchaseListActivity;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton uploadClassBtn, uploadNewsBtn, openAssessmentBtn ;
    private ImageView openApprovalBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        uploadClassBtn = findViewById(R.id.uploadClassBtn);
        uploadNewsBtn = findViewById(R.id.uploadNewsBtn);
        openAssessmentBtn = findViewById(R.id.openAssessmentBtn);
        openApprovalBtn = findViewById(R.id.openApprovalBtn);

        uploadClassBtn.setOnClickListener(this);
        uploadNewsBtn.setOnClickListener(this);
        openAssessmentBtn.setOnClickListener(this);
        openApprovalBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.uploadClassBtn:
                intent = new Intent(this, ManageVideoContentActivity.class);
                startActivity(intent);
                break;

            case R.id.uploadNewsBtn:
                intent = new Intent(this, UploadAnnouncementActivity.class);
                startActivity(intent);

                break;
            case R.id.openAssessmentBtn:
                intent = new Intent(this, AdminDashboardActivity.class);
                startActivity(intent);
                break;

            case R.id.openApprovalBtn:
                intent = new Intent(this, ApprovePurchaseListActivity.class);
                startActivity(intent);
                break;

                default:

        }
    }
}
