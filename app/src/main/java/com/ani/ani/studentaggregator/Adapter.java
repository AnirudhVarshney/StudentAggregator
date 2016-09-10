package com.devionlabs.ray.studentaggregator;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devionlabs.ray.studentaggregator.model.Event;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 20-05-2016.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater minflater;
    ArrayList<Event> mResults;
    RecyclerListner mListner;
    Context con;


    public Adapter(Context context, ArrayList<Event> Results) {

        minflater = LayoutInflater.from(context);
        mResults = Results;
        con = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.row_event, parent, false);
        return new EventHol(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProgressBar progressBar;
        if (holder instanceof EventHol) {
            EventHol eventHolder = (EventHol) holder;
            Log.d("anirudh1", "in eventbind");
            Event event = mResults.get(position);
            Log.d("ani", "h" + event.getImages());
            if (event.getImages().isEmpty()) {
                eventHolder.mTextView.setText(event.getDatetime());
                eventHolder.Title.setText(event.getTitle());
                eventHolder.Description.setText(event.getDescription());
                eventHolder.mRelative.setVisibility(View.GONE);
                eventHolder.BelowRecyclerView.setVisibility(View.GONE);
            } else if (event.getId().equals("4")) {
                eventHolder.mTextView.setVisibility(View.GONE);
                eventHolder.Title.setVisibility(View.GONE);
                eventHolder.Description.setVisibility(View.GONE);
                eventHolder.BelowEvemtdesc.setVisibility(View.GONE);
                eventHolder.mREcyclerTextView.setText(event.getDatetime());
                eventHolder.RecyclerTitle.setText(event.getTitle());
                eventHolder.RecyclerDescription.setText(event.getDescription());
                AdapterEvent mAdapter = new AdapterEvent(con, event.getId());
                eventHolder.recyclerViewchild.setAdapter(mAdapter);
                LinearLayoutManager manager = new LinearLayoutManager(minflater.getContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                eventHolder.recyclerViewchild.setLayoutManager(manager);
                Log.d("@@", "hi in 4" + event.getImages());
            } else if (event.getId().equals("1")) {
                eventHolder.mTextView.setVisibility(View.GONE);
                eventHolder.Title.setVisibility(View.GONE);
                eventHolder.Description.setVisibility(View.GONE);
                eventHolder.BelowEvemtdesc.setVisibility(View.GONE);
                eventHolder.mREcyclerTextView.setText(event.getDatetime());
                eventHolder.RecyclerTitle.setText(event.getTitle());
                eventHolder.RecyclerDescription.setText(event.getDescription());
                AdapterEvent mAdapter = new AdapterEvent(con, event.getId());
                eventHolder.recyclerViewchild.setAdapter(mAdapter);
                LinearLayoutManager manager = new LinearLayoutManager(minflater.getContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                eventHolder.recyclerViewchild.setLayoutManager(manager);
                Log.d("@@", "hi in 1" + event.getImages());
            } else {
                eventHolder.mTextView.setText(event.getDatetime());
                eventHolder.mRelative.setVisibility(View.GONE);
                eventHolder.BelowRecyclerView.setVisibility(View.GONE);
            }

        }
    }

    public void setRecyclerListner(RecyclerListner mRecyclerListner) {
        mListner = mRecyclerListner;

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    private class EventHol extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;
        TextView mTextView;
        TextView Title;
        TextView Description;
        TextView mREcyclerTextView;
        TextView RecyclerTitle;
        TextView RecyclerDescription;
        LinearLayout BelowEvemtdesc;
        LinearLayout BelowRecyclerView;
        RelativeLayout mRelative;
        boolean isImageFitToScreen;
        RecyclerView recyclerViewchild;
        //ProgressBar spinner;

        public EventHol(View itemView) {
            super(itemView);

            recyclerViewchild = (RecyclerView) itemView.findViewById(R.id.rv_recyclerevent);
            mTextView = (TextView) itemView.findViewById(R.id.top);
            Title = (TextView) itemView.findViewById(R.id.Eventname);
            Description = (TextView) itemView.findViewById(R.id.EventDesc);
            RecyclerTitle = (TextView) itemView.findViewById(R.id.RecyclerEventname);
            RecyclerDescription = (TextView) itemView.findViewById(R.id.RecyclerEventsmalldisc);
            mREcyclerTextView = (TextView) itemView.findViewById(R.id.topRecycler);
            mRelative = (RelativeLayout) itemView.findViewById(R.id.RecyclerViewlayout);
            BelowEvemtdesc = (LinearLayout) itemView.findViewById(R.id.BelowEventDesc);
            BelowRecyclerView = (LinearLayout) itemView.findViewById(R.id.BelowRecyclerview);
            //spinner=(ProgressBar)itemView.findViewById(R.id.progressBar);
            //spinner.setVisibility(View.GONE);

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
}
