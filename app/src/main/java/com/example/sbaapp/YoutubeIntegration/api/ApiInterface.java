package com.example.sbaapp.YoutubeIntegration.api;

import com.example.sbaapp.YoutubeIntegration.YouTubeModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET
    Call<YouTubeModel> getVideos(@Url String url);
}
