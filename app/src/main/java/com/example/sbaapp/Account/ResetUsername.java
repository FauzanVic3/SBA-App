package com.example.sbaapp.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sbaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ResetUsername extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private EditText editUsername;
    private Button resetBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_username);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUser.getUid();
        editUsername = findViewById(R.id.editUsername);
        resetBtn = findViewById(R.id.resetBtn);

        final String currentUID = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);




            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);




        resetBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                mUserDatabase.child("name").setValue(editUsername.getText().toString());
                Intent reset = new Intent(ResetUsername.this, AccountSetting.class);
                startActivity(reset);
            }
        });

    }
}
