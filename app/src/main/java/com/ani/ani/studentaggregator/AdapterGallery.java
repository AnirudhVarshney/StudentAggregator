package com.devionlabs.ray.studentaggregator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devionlabs.ray.studentaggregator.activity.ImageGallery;
import com.devionlabs.ray.studentaggregator.model.Event;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 16-07-2016.
 */
public class AdapterGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private LayoutInflater minflater;
    ArrayList<Event> mResults;
    int count;
    String[] arrayImageUrl;
    Context con;
    public AdapterGallery(Context context, ArrayList<Event> Results,int c) {
        minflater = LayoutInflater.from(context);
        mResults = Results;
        con = context;
        count=c;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.row_gallery, parent, false);
        return new GalleryHol(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GalleryHol galleryHol = (GalleryHol) holder;
        Event event = mResults.get(position);
        galleryHol.textViewHeading.setText(event.getTitle());
        int noOfImages = Integer.parseInt(event.getImages());
        arrayImageUrl = new String[noOfImages];

        for (int i = 0; i < noOfImages; i++) {
            arrayImageUrl[i] = "http://studentaggregator.org/events/" + event.getId() + "_" + (i+1) + ".png";
        }
        Glide.with(con).load(arrayImageUrl[position]).centerCrop().listener((new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
               // eventHolder.spinner.setVisibility(View.GONE);
                return false;
            }
        })).crossFade().into(galleryHol.image);
        galleryHol.image.setOnClickListener(new OnImageClickListener(position));
    }

    @Override
    public int getItemCount() {
        return count;
    }
    private class GalleryHol extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewHeading;
        TextView textViewDescription;
        ImageView image;

        public GalleryHol(View itemView) {
            super(itemView);
            textViewHeading = (TextView) itemView.findViewById(R.id.galTextTitle);

            image = (ImageView) itemView.findViewById(R.id.galImage);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
        class OnImageClickListener implements View.OnClickListener {

            int _postion;
            String _id;

            // constructor
            public OnImageClickListener(int position) {
                this._postion = position;
            }

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Event event = mResults.get(_postion);
                int noOfImages = Integer.parseInt(event.getImages());
                arrayImageUrl = new String[noOfImages];

                for (int i = 0; i < noOfImages; i++) {
                    arrayImageUrl[i] = "http://studentaggregator.org/events/" + event.getId() + "_" + (i+1) + ".png";
                }
                Intent i = new Intent(con, ImageGallery.class);
                i.putExtra("urls", arrayImageUrl);
                i.putExtra("title",event.getTitle());
                i.putExtra("desc",event.getDescription());
                con.startActivity(i);
            }
        }
}
