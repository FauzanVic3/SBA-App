package com.example.sbaapp.ClassVideoPackage.ManageVideoContent;


import android.widget.Toast;

public class UploadVideoContentDetails {

//    public String TAG ="Testing";
    public String video_url;
    public String video_thumbnail_url;
    public String video_admin;
    public String video_title;
    public String video_description;


    public UploadVideoContentDetails(){

    }

    public UploadVideoContentDetails(String video_admin, String video_title, String video_description, String video_url, String video_thumbnail_url) {
        this.video_admin = video_admin;
        this.video_title = video_title;
        this.video_description = video_description;
        this.video_url = video_url;
        this.video_thumbnail_url = video_thumbnail_url;
    }

    public String getVideo_url() {
        System.out.println("Called video url from object : "+video_url);
        return video_url;
    }

    public String getVideo_thumbnail_url() {
        return video_thumbnail_url;
    }
}
