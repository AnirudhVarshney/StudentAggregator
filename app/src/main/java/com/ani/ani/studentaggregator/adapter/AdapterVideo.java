package com.devionlabs.ray.studentaggregator.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.RecyclerListner;
import com.devionlabs.ray.studentaggregator.activity.VideoPlayer;
import com.devionlabs.ray.studentaggregator.helper.FileDownloader;
import com.devionlabs.ray.studentaggregator.helper.ServerUtilities;
import com.devionlabs.ray.studentaggregator.helper.Video;
import com.devionlabs.ray.studentaggregator.model.Fee;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Created by RAY on 12-06-2016.

public class AdapterVideo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "mylog";
    private LayoutInflater layoutInflater;
    private ArrayList<Video> videoList;
    private Context con;

    public AdapterVideo(Context context, ArrayList<Video> videoList) {
        layoutInflater = LayoutInflater.from(context);
        this.videoList = videoList;
        con = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_single_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VideoHolder) {
            final VideoHolder videoHolder = (VideoHolder) holder;
            final Video video = videoList.get(position);

            videoHolder.textViewTitle.setText(video.getTitle());
            videoHolder.textViewDescription.setText(video.getDescription());

            final String id = video.getId();
            String imageurl = "http://img.youtube.com/vi/" + id + "/0.jpg";

            Glide.with(con).load(imageurl).centerCrop().listener((new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    videoHolder.spinner.setVisibility(View.GONE);
                    return false;
                }
            })).crossFade().into(videoHolder.imageViewVideo);


            videoHolder.linearLayoutVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(con, VideoPlayer.class);
                    i.putExtra("id", id);
                    con.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayoutVideo;
        ImageView imageViewVideo;
        TextView textViewTitle;
        TextView textViewDescription;
        ProgressBar spinner;

        public VideoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            linearLayoutVideo = (LinearLayout) itemView.findViewById(R.id.linearLayoutVideo);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            imageViewVideo = (ImageView) itemView.findViewById(R.id.imageViewVideo);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBarLoadImage);
        }

        @Override
        public void onClick(View v) {
            Log.d("mylog", "position clicked");
        }
    }
}
