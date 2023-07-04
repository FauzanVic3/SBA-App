package com.example.sbaapp.YoutubeIntegration;

import android.graphics.pdf.PdfDocument;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class YouTubeModel implements Serializable {
    @SerializedName("nextPageToken")
    String NextPageToken;

    @SerializedName("pageInfo")
    PageInfo PageInfo;

    @SerializedName("items")
    List<VideoModel> VideoModels;

    class PageInfo{
        @SerializedName("totalResults")
        int TotalResult;
    }

    int getTotalViedeos(){
        return PageInfo.TotalResult;
    }

    String getNextPageToken() {
        return NextPageToken;
    }

    List<VideoModel> getVideoModels() {
        return VideoModels;
    }

}
