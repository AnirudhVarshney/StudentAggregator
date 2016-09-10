package com.devionlabs.ray.studentaggregator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.adapter.AdapterVideo;
import com.devionlabs.ray.studentaggregator.helper.Video;

import java.util.ArrayList;

/**
 * Created by Ray on 5/12/2016.
 */
public class ServicesActivity extends AppCompatActivity {

    private String LOG_TAG = "mylog";
    private FloatingActionButton fab;
    String queryText = "";

    RecyclerView recyclerViewVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setBackgroundTintList(getResources().getColor(R.color.colorAbsent));
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e86462")));

        recyclerViewVideo = (RecyclerView) findViewById(R.id.recyclerViewVideo);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewVideo.setLayoutManager(manager);

        ArrayList<Video> videoList = new ArrayList<>();

        Video video1 = new Video("https://www.youtube.com/watch?v=r8iGnwD8i9I", "Nursery Rhymes", "ABC Song and many more Nursery Rhymes for Children","r8iGnwD8i9I");
        Video video2 = new Video("https://www.youtube.com/watch?v=7FP0ZwGRlxI", "Science Project", "Science project by class 8 students 05 simple circuit","7FP0ZwGRlxI");
        Video video3 = new Video("https://www.youtube.com/watch?v=SSwbLYiH_ig", "Science Project", "Science project by class 8 students 06 simple circuit","SSwbLYiH_ig");
        Video video4 = new Video("https://www.youtube.com/watch?v=5c_lL6I3OaA", "Geography Project", "Planets in our solar system | Sun and solar system for children","5c_lL6I3OaA");
        Video video5 = new Video("https://www.youtube.com/watch?v=3pD68uxRLkM", "life science  Project", "Photosynthesis in plants | Biology basics for children","3pD68uxRLkM");
        Video video6 = new Video("https://www.youtube.com/watch?v=mfrp78wla7g", "History Project", "Aurangzeb Alamgir -The Greatest Strongest & Invincible Mughal Emperor 1618-1707","mfrp78wla7g");

        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video3);
        videoList.add(video4);
        videoList.add(video5);
        videoList.add(video6);

        AdapterVideo adapterVideo = new AdapterVideo(this, videoList);
        recyclerViewVideo.setAdapter(adapterVideo);

        setOnClickListeners();

    }

    private void setOnClickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServicesActivity.this);
                builder.setTitle(R.string.submit_your_query);

                // Set up the input
                final EditText input = new EditText(ServicesActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                input.setHint(R.string.your_message);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        queryText = input.getText().toString();
                        Toast.makeText(ServicesActivity.this, R.string.message_sent, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(), ActivityMenu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return true;
    }
}
