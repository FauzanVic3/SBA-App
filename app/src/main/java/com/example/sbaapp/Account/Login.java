package com.example.sbaapp.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sbaapp.MainActivity;
import com.example.sbaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText loginEmail;
    private  EditText loginPassword;
    private TextView regNavText,forgotPasswordTextView;
    private Button loginBtn;
    private Spinner spinnerUserType;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginBtn = (Button) findViewById(R.id.LoginBtn);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        regNavText = (TextView) findViewById(R.id.regNavText);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);


        regNavText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getEditableText().toString();
                String password = loginPassword.getEditableText().toString();
                if(email.isEmpty())
                    Toast.makeText(Login.this,"E-mail field is empty",Toast.LENGTH_LONG).show();
                else if(password.isEmpty())
                    Toast.makeText(Login.this,"Password field is empty",Toast.LENGTH_LONG).show();
                else
                login_user(email,password);
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
                            Toast.makeText(Login.this, "You are now signed in.",
                                    Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();

                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            startActivity(mainIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }




}
