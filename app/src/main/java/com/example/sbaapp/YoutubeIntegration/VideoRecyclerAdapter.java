package com.example.sbaapp.YoutubeIntegration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sbaapp.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.RecyclerGalleryView> {
    String TAG = VideoRecyclerAdapter.class.getSimpleName();
    private List<VideoModel> Items = new ArrayList<>();
    private Context mContext;

    OnItemClickListener mItemClickListener;

    public VideoRecyclerAdapter(Context context) {
        this.Items = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public RecyclerGalleryView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_video, null);
        return new RecyclerGalleryView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerGalleryView holder, int i) {
        final VideoModel model = Items.get(i);


        holder.txtTitle.setText(model.getTitle());
        holder.txtDate.setText(toDate(model.getDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy"));
        Picasso.get()
                .load(model.getDefaultImage())
                .placeholder(R.drawable.ic_play)
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return (null != Items ? Items.size() : 0);
    }

    public List<VideoModel> getItems() {
        if (Items == null) {
            return new ArrayList<>();
        }

        return Items;
    }

    public class RecyclerGalleryView extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitle, txtDate;
        ImageView imgItem;

        public RecyclerGalleryView(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            imgItem = (ImageView) view.findViewById(R.id.img_adapter);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClicklListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void add(VideoModel videoModel) {
        Items.add(videoModel);
        //notifyItemInserted(Items.size() - 1);
        notifyDataSetChanged();
    }

    public void addAll(List<VideoModel> videoModelList) {
        for (VideoModel videoModel : videoModelList) {
            add(videoModel);
        }
    }

    public void remove(VideoModel videoModel) {
        int position = Items.indexOf(videoModel);
        if (position > -1) {
            Items.remove(position);
            //notifyItemRemoved(position);
        }

        notifyDataSetChanged();
    }

    public String toDate(String dateString, String oldFormat, String newFormat) {
        String returnString = dateString;
        try {
            DateFormat inputFormat = new SimpleDateFormat(oldFormat);
            DateFormat outputFormat = new SimpleDateFormat(newFormat);
            Date date = inputFormat.parse(dateString);
            returnString = outputFormat.format(date);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        return returnString;
    }
}