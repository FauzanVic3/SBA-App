package com.example.sbaapp.Account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sbaapp.MainActivity;
import com.example.sbaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Register extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button createAccBtn;
    private EditText regUsername;
    private EditText regEmail;
    private EditText regPassword;
    private EditText confirmPassword;

    private String username,email,password,cPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mDeepDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mAuth = FirebaseAuth.getInstance();


        regUsername= findViewById(R.id.regUsername);
        regEmail= findViewById(R.id.loginEmail);
        regPassword= findViewById(R.id.loginPassword);
        confirmPassword= findViewById(R.id.confirmPassword);
        createAccBtn =  findViewById(R.id.createAccBtn);

        createAccBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                username = regUsername.getEditableText().toString();
                email = regEmail.getEditableText().toString();
                password = regPassword.getEditableText().toString();
                cPassword = confirmPassword.getEditableText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword)){
                    Toast.makeText(Register.this, "Please complete the registration form", Toast.LENGTH_LONG).show();
                }
                else if(!cPassword.equals(password)){
                    Toast.makeText(Register.this,"Confirm Password mismatch!",Toast.LENGTH_SHORT).show();
                }
                else
                register_user(username,email,password,cPassword);
            }
        });




    }

    public void register_user(final String username, final String email, final String password, String cPassword){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            //User personal details
                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("name",username);
                            userMap.put("status","Demo Access");
                            userMap.put("admin","false");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            userMap.put("class_purchased","false");

                            mDatabase.setValue(userMap);

                            mDeepDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("class_details");

                            HashMap<String,String> classDetailHashMap = new HashMap<>();
                            classDetailHashMap.put("purchase", "false");
                            classDetailHashMap.put("submission_status","No submission");
                            classDetailHashMap.put("submission_link","No link");
                            classDetailHashMap.put("certification_status","false");
                            classDetailHashMap.put("submission_rating","Not rated");
                            classDetailHashMap.put("submission_comment","No comment");

                            List<HashMap<String,String>> hashMapList = new ArrayList<>();
                            hashMapList.add(classDetailHashMap);

                            HashMap<String, String> classDetailHashMapSpecialAdvanced = new HashMap<>();
                            classDetailHashMapSpecialAdvanced.put("purchase", "false");
                            classDetailHashMapSpecialAdvanced.put("submission_status","No submission");
                            classDetailHashMapSpecialAdvanced.put("submission_link","No link");
                            classDetailHashMapSpecialAdvanced.put("certification_status","false");
                            classDetailHashMapSpecialAdvanced.put("submission_rating","Not rated");
                            classDetailHashMapSpecialAdvanced.put("submission_comment","No comment");
                            classDetailHashMapSpecialAdvanced.put("submission_status2","No submission");
                            classDetailHashMapSpecialAdvanced.put("submission_link2","No link");
                            classDetailHashMapSpecialAdvanced.put("certification_status2","false");
                            classDetailHashMapSpecialAdvanced.put("submission_rating2","Not rated");
                            classDetailHashMapSpecialAdvanced.put("submission_comment2","No comment");
                            classDetailHashMapSpecialAdvanced.put("submission_status3","No submission");
                            classDetailHashMapSpecialAdvanced.put("submission_link3","No link");
                            classDetailHashMapSpecialAdvanced.put("certification_status3","false");
                            classDetailHashMapSpecialAdvanced.put("submission_rating3","Not rated");
                            classDetailHashMapSpecialAdvanced.put("submission_comment3","No comment");

                            List<HashMap<String,String>> hashMapListSpecialAdvanced = new ArrayList<>();
                            hashMapListSpecialAdvanced.add(classDetailHashMapSpecialAdvanced);

                            HashMap<String, List<HashMap<String,String>>> classMap = new HashMap<>();
                            classMap.put("nurul_bayan_beginner",hashMapList);
                            classMap.put("nurul_bayan_intermediate",hashMapList);
                            classMap.put("nurul_bayan_advanced",hashMapList);
                            classMap.put("khat_diwani_beginner",hashMapList);
                            classMap.put("khat_diwani_intermediate",hashMapList);
                            classMap.put("khat_diwani_advanced",hashMapList);
                            classMap.put("khat_diwani_jali_beginner",hashMapList);
                            classMap.put("khat_diwani_jali_intermediate",hashMapList);
                            classMap.put("khat_diwani_jali_advanced",hashMapList);
                            classMap.put("khat_thuluth_beginner",hashMapList);
                            classMap.put("khat_thuluth_intermediate",hashMapList);

                            classMap.put("khat_farisi_beginner",hashMapList);
                            classMap.put("khat_farisi_intermediate",hashMapList);
                            classMap.put("khat_farisi_advanced",hashMapList);
                            classMap.put("khat_riqah_beginner",hashMapList);
                            classMap.put("khat_riqah_intermediate",hashMapList);
                            classMap.put("khat_riqah_advanced",hashMapList);
                            classMap.put("khat_nasakh_beginner",hashMapList);
                            classMap.put("khat_nasakh_intermediate",hashMapList);



                            classMap.put("khat_nasakh_advanced",hashMapListSpecialAdvanced);
                            classMap.put("khat_thuluth_advanced",hashMapListSpecialAdvanced);

                            mDeepDatabase.setValue(classMap);

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            //Intent mainIntent = new Intent(Register.this, MainActivity.class);

                            login_user(email,password);

                            //updateUI(user);

                            //startActivity(mainIntent);
                            //finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }
                    }
                });


    }

    public void login_user(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(Register.this, "You are now signed in.",
                                    Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();

                            Intent mainIntent = new Intent(Register.this, MainActivity.class);
                            startActivity(mainIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

//    public void sendEmail(){
//    final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener() {
//        @Override
//        public void onComplete(@NonNull Task task) {
//
//
//            if (task.isSuccessful()) {
//                Toast.makeText(Register.this,
//                        "Verification email sent to " + user.getEmail(),
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e(TAG, "sendEmailVerification", task.getException());
//                Toast.makeText(Register.this,
//                        "Failed to send verification email.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    });
//    }

}
