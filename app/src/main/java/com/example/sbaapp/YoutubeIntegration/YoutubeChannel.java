package com.example.sbaapp.YoutubeIntegration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sbaapp.MainActivity;
import com.example.sbaapp.R;
import com.example.sbaapp.YoutubeIntegration.api.ApiClient;
import com.example.sbaapp.YoutubeIntegration.api.ApiInterface;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeChannel extends AppCompatActivity {

    String API_KEY = "AIzaSyAR2dvZ76wkgn3umew-1LoV7m0x4LdCyoo";

    //&maxResults=25
    String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCVNysr9qfH4jkTCvHIWAkIQ&key=AIzaSyAR2dvZ76wkgn3umew-1LoV7m0x4LdCyoo";

    String TAG = MainActivity.class.getSimpleName();
    String YouTubeAPIChannelId = "UCVNysr9qfH4jkTCvHIWAkIQ";
    String YouTubeAPIKey = "AIzaSyAR2dvZ76wkgn3umew-1LoV7m0x4LdCyoo";
    String YouTubeAPIUrl = "https://www.googleapis.com/youtube/v3/search?" +
            "part=snippet&" +
            "part=statistics&" +
            "channelId=" + YouTubeAPIChannelId + "&" +
            "&maxResults=25&" +
            "order=date&" +
            "key=" + YouTubeAPIKey;

    Context context;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int RECOVERY_REQUEST = 1;
    private int selected = 0;

    TextView txtTotalVideo;
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    VideoRecyclerAdapter adapter;
    YouTubeModel model;
    YouTubePlayer youTubePlayer;
    YouTubePlayerSupportFragment youtubePlayerFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_channel);

        try {
            context = this;
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            txtTotalVideo = (TextView) findViewById(R.id.txt_total_video);

            adapter = new VideoRecyclerAdapter(context);
            //can ignore this error
            youtubePlayerFragment = (YouTubePlayerSupportFragment) this.getSupportFragmentManager().findFragmentById(R.id.frame_fragment);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    if (!isLastPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadVideos(YouTubeAPIUrl + "&pageToken=" + model.getNextPageToken());
                            }
                        }, 200);
                    }
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClicklListener(new VideoRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selected = position;
                    if (adapter.getItems().size() > 0 && adapter.getItems().get(selected) != null && adapter.getItems().get(selected).getVideoId() != null) {
                        youTubePlayer.cueVideo(adapter.getItems().get(selected).getVideoId());
                        youTubePlayer.play();
                    }
                }
            });

            loadVideos(YouTubeAPIUrl);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void loadVideos(String url) {
        ApiInterface apiService = ApiClient.createService(ApiInterface.class);
        if (apiService == null) return;

        showProgressBar();
        Call<com.example.sbaapp.YoutubeIntegration.YouTubeModel> call = apiService.getVideos(url);
        call.enqueue(new Callback<YouTubeModel>() {
            @Override
            public void onResponse(Call<YouTubeModel> call, Response<YouTubeModel> response) {
                YouTubeModel serverResponse = response.body();
                hideProgressBar();
                loadDataAction(serverResponse);
            }

            @Override
            public void onFailure(Call<YouTubeModel> call, Throwable t) {
                Log.e(TAG, t.toString());
                hideProgressBar();
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void loadDataAction(YouTubeModel item) {
        isLoading = false;

        if (item != null && item.getVideoModels() != null) {
            model = item;
            txtTotalVideo.setText("Total Videos: " + item.getTotalViedeos());

            adapter.addAll(model.getVideoModels());
            adapter.notifyDataSetChanged();

            youtubePlayerFragment.initialize(YouTubeAPIKey, onInitializedListener);

            if (model.getNextPageToken() == null) {
                isLastPage = true;
            }
        }
    }

    YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
            if (!b) {
                youTubePlayer = player;
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                if (adapter != null && adapter.getItems().size() > 0) {
                    if (adapter.getItems().get(selected) != null && adapter.getItems().get(selected).getVideoId() != null) {
                        youTubePlayer.cueVideo(adapter.getItems().get(selected).getVideoId());
                        youTubePlayer.loadVideo(adapter.getItems().get(selected).getVideoId());
                        youTubePlayer.play();
                    }
                }

                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {

                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {
                        Log.e(TAG, "Video Started");
                    }

                    @Override
                    public void onVideoEnded() {
                        Log.e(TAG, "Video End");
                        if (selected >= adapter.getItems().size()) {
                            selected = 0;
                        } else {
                            selected++;
                        }

                        youTubePlayer.cueVideo(adapter.getItems().get(selected).getVideoId());
                        youTubePlayer.play();
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            if (youTubeInitializationResult.isUserRecoverableError()) {
                youTubeInitializationResult.getErrorDialog((Activity) context, RECOVERY_REQUEST).show();
            } else {
                Toast.makeText(context, "Error Intializing Youtube Player", Toast.LENGTH_LONG).show();
            }
        }
    };
}
