package com.example.sbaapp.YoutubeIntegration;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoModel implements Serializable {
    @SerializedName("id")
    Id Id;

    @SerializedName("snippet")
    Snippet Snippet;

    public class Id {
        @SerializedName("videoId")
        String VideoId;
    }

    public class Snippet {
        @SerializedName("title")
        String Title;

        @SerializedName("description")
        String Description;

        @SerializedName("publishedAt")
        String Date;

        @SerializedName("thumbnails")
        Thumbnails Thumbnails;
    }

    public class Thumbnails {
        @SerializedName("default")
        Default Default;

        class Default {
            @SerializedName("url")
            String Url;
        }
    }

    public String getVideoId() {
        return Id.VideoId;
    }

    public String getTitle() {
        if (Snippet.Title == null) {
            return "";
        }

        return Snippet.Title;
    }

    public String getDescription() {
        return Snippet.Description;
    }

    public String getDate() {
        return Snippet.Date;
    }

    public String getDefaultImage() {
        return Snippet.Thumbnails.Default.Url;
    }
}
