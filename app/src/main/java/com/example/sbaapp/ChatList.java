package com.example.sbaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView personTxt;
    private Button chatBtn;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);




    }

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersdRef = rootRef.child("users");
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                String name = ds.child("name").getValue(String.class);
                Log.d("TAG", name);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

//usersdRef.addListenerForSingleValueEvent(eventListener);

}
    /*
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersdRef = rootRef.child("users");
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                String name = ds.child("name").getValue(String.class);
                Log.d("TAG", name);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

usersdRef.addListenerForSingleValueEvent(eventListener);
*/
