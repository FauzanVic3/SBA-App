package com.example.sbaapp.ClassVideoPackage.ManageVideoContent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sbaapp.ClassVideoPackage.ClassVideo;
import com.example.sbaapp.ClassVideoPackage.Submission;
import com.example.sbaapp.ClassVideoPackage.VideoList;
import com.example.sbaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditVideoList extends AppCompatActivity {

    final int SIZE = 20;
    volatile int videoCount = 0;

    private String[] VIDEOID = new String[SIZE];
    private String[] NAMES = new String[SIZE];
    private String[] DESCRIPTIONS = new String[SIZE];
    private String[] URL = new String[SIZE];
    private String[] THUMBNAIL = new String[SIZE];
    private String classSelection, subClassSelection, level;

    private TextView textView_name, textView_description;
    private ImageView thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        classSelection = bundle.getString("class");
        level = bundle.getString("level");
        if(bundle.getString("subClass")!=null)
            subClassSelection = bundle.getString("subClass");

        //Retrieve data from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if(classSelection.equals("Khat")) {
            databaseReference.child("ClassList")
                    .child("Class" + classSelection)
                    .child("Subclass" + subClassSelection)
                    .child(level).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot videoDetailsSnapshot : dataSnapshot.getChildren()) {
                        VIDEOID[videoCount] = videoDetailsSnapshot.getKey();
                        NAMES[videoCount] = videoDetailsSnapshot.child("video_title").getValue().toString();
                        DESCRIPTIONS[videoCount] = videoDetailsSnapshot.child("video_description").getValue().toString();
                        URL[videoCount] = videoDetailsSnapshot.child("video_url").getValue().toString();
                        THUMBNAIL[videoCount] = videoDetailsSnapshot.child("video_thumbnail_url").getValue().toString();
                        videoCount++;
                    }

                    setContentView(R.layout.activity_edit_video_list);

                    ListView listView = findViewById(R.id.listView);

                    CustomAdapter customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(EditVideoList.this, EditVideoActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("class",classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level", level);
                            bundle.putString("video_title", NAMES[position]);
                            bundle.putString("video_description", DESCRIPTIONS[position]);
                            bundle.putString("video_url", URL[position]);
                            bundle.putString("video_id",VIDEOID[position]);

                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    });



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }



            });
        }
        else if(classSelection.equals("Nurul Bayan")) {
            databaseReference.child("ClassList")
                    .child("Class" + classSelection)
                    .child(level).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot videoDetailsSnapshot : dataSnapshot.getChildren()) {

                        VIDEOID[videoCount] = videoDetailsSnapshot.getKey();
                        NAMES[videoCount] = videoDetailsSnapshot.child("video_title").getValue().toString();
                        DESCRIPTIONS[videoCount] = videoDetailsSnapshot.child("video_description").getValue().toString();
                        URL[videoCount] = videoDetailsSnapshot.child("video_url").getValue().toString();
                        THUMBNAIL[videoCount] = videoDetailsSnapshot.child("video_thumbnail_url").getValue().toString();
                        videoCount++;
                    }

                    setContentView(R.layout.activity_edit_video_list);

                    final ListView listView = findViewById(R.id.listView);

                    final CustomAdapter customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(EditVideoList.this, EditVideoActivity.class);

                            Bundle bundle = new Bundle();

                            bundle.putString("class",classSelection);
                            bundle.putString("subClass", subClassSelection);
                            bundle.putString("level", level);
                            bundle.putString("video_title", NAMES[position]);
                            bundle.putString("video_description", DESCRIPTIONS[position]);
                            bundle.putString("video_url", URL[position]);
                            bundle.putString("video_id",VIDEOID[position]);
                            bundle.putString("video_thumbnail", THUMBNAIL[position]);

                            intent.putExtras(bundle);
                            //startActivityForResult(intent,1);
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }


    }



    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return videoCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {


            view = getLayoutInflater().inflate(R.layout.customlayout, null);

            thumbnail =  view.findViewById(R.id.imageViewThumbnail);
            textView_name = view.findViewById(R.id.textView_name);
            textView_description = view.findViewById(R.id.textView_description);

            textView_name.setText(NAMES[position]);
            textView_description.setText(DESCRIPTIONS[position]);

            Glide.with(EditVideoList.this)
                    .load(Uri.parse(THUMBNAIL[position]))
                    .override(300,200)
                    .into(thumbnail);


            return view;

        }
    }
}
