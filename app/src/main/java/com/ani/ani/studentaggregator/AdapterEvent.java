package com.devionlabs.ray.studentaggregator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.model.Event;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 18-05-2016.
 */
public class AdapterEvent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater minflater;
    RecyclerListner mListner;
    ArrayList<Event> mResults;
    Context con;
    DBHelper dbHelper;
    String im;
    //final String url1="http://www.dpsgrnoida.com/dpsgrnoida/UserSpace/UserName/dpsnadmin/DynamicFolder/HomePage_New/events/image/annual-j2.jpg";

    //final String url1="http://silverlineprestigeschool.com/wp-content/uploads/2014/02/DSC_0149-copy.jpg";
    String url1_4 = "http://wecanschool.org/wp-content/uploads/2013/02/annual-day-fb-page.jpg";
    String url2_4 = "https://i.ytimg.com/vi/FJsZn7FKR_s/maxresdefault.jpg";
    String url3_4 = "http://www.tribuneindia.com/2004/20040219/ldh9.jpg";
    String url4_4 = "http://dpsgrnoida.com/dpsgrnoida/UserSpace/UserName/dpsnadmin/DynamicFolder/HomePage_New/events/image/annual1-2-12-13.JPG";
    // String url1_1="http://studentaggregator.org/events/1_1.png";
    String url1_1 = "https://i.ytimg.com/vi/DdaH70Kd75k/maxresdefault.jpg";
    String url2_1 = "http://studentaggregator.org/events/1_2.png";
    String url3_1 = "http://studentaggregator.org/events/1_3.png";
    String url4_1 = "http://studentaggregator.org/events/1_4.png";
    String url5_1 = "http://studentaggregator.org/events/1_5.png";
    String[] urls = {url1_1, url2_1, url3_1, url4_1, url5_1};
    String[] urls1 = {url1_4, url2_4, url3_4, url4_4};
    View view;
    String Id;


    public AdapterEvent(Context context, String id) {
        minflater = LayoutInflater.from(context);
        con = context;
        Id = id;
        //pb = bar;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = minflater.inflate(R.layout.row_event_text, parent, false);
//        pb.setVisibility(View.VISIBLE);
        return new EventHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            final EventHolder eventHolder = (EventHolder) holder;


            if (Id.equals("1")) {
                Log.d("!", "hii" + Id);
                Glide.with(con).load(urls[position]).centerCrop().listener((new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //pb.setVisibility(View.GONE);
                        eventHolder.spinner.setVisibility(View.GONE);
                        return false;
                    }
                })).crossFade().into(eventHolder.mImageView);
                eventHolder.mImageView.setOnClickListener(new OnImageClickListener(Id,position));

            } else if (Id.equals("4")) {
                Log.d("!", "hii in 4" + Id);
                Glide.with(con).load(urls1[position]).listener((new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //pb.setVisibility(View.GONE);
                        eventHolder.spinner.setVisibility(View.GONE);
                        return false;
                    }
                })).centerCrop().into(eventHolder.mImageView);
                eventHolder.mImageView.setOnClickListener(new OnImageClickListener(Id,position));
            }


        }

    }


    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int j = 0;
        if (Id.equals("1")) {
            j = urls.length;
        } else
            j = urls1.length;
        return j;
    }

    public void setRecyclerListner(RecyclerListner mRecyclerListner) {
        mListner = mRecyclerListner;

    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;
        TextView mTextView;
        boolean isImageFitToScreen;
        ProgressBar spinner;


        public EventHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.event_image);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            // mImageView.setOnClickListener(this);//
            //mImageView.setOnClickListener(new OnImageClickListener(mImageView.get));

        }


        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
        }

    }

    class OnImageClickListener implements View.OnClickListener {

        int _postion;
        String _id;

        // constructor
        public OnImageClickListener(String id,int position) {
            this._postion = position;
            this._id=id;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Log.d("@@", "hii" + _postion);
            Intent i = new Intent(con, FullScreenViewActivity.class);
             i.putExtra("position", _postion);
            i.putExtra("Id",_id);
            con.startActivity(i);
        }
    }
}
