package com.devionlabs.ray.studentaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.model.Event;
import com.devionlabs.ray.studentaggregator.model.EventAdapter1;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    RecyclerView mRecycler;
    ArrayList<Event> mResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar);

        DBHelper dbHelper = new DBHelper(this);

        Event obj1 = new Event("1", "Annual Function", "Glimpse of 2015 Annual function", "10 december 2015", "5");
        Event obj2 = new Event("2", "Trip to Bird Sanctuary", "Your Student is invited for a trip to Delhi,Bird Sanctuary" +
                "\nTimings: 07:00AM - 06:00 PM\nDescription:We will be leaving by 6am Be there on time ", "3 February 2016", "0");
        Event obj3 = new Event("3", "Rainy day", "Because of heavy rain, School wil remain closed on 14 January 2016", "14 January 2016", "0");
        Event obj4 = new Event("4", "Art Competition", "The art work is a selection of some of the best entries received " +
                "from a creative expression and art competition on the theme Right to Learn. Held on 26th February 2016, " +
                "an initiative of 'Global Education First' announced this competition and reached out to schools through " +
                "their networks. Nearly 250 schools participated and over 2,000 entries were received including paintings, " +
                "pencil sketches, comic strips, posters, collages, poems, stories, write-ups and slogans", "26 February 2016", "4");
        ArrayList<Event> objlist = new ArrayList<Event>();
        objlist.add(obj1);
        objlist.add(obj2);
        objlist.add(obj3);
        objlist.add(obj4);
        dbHelper.clearEvent();
        dbHelper.addEvent(objlist);
        mResults = dbHelper.getAllEventDetails();
        mRecycler = (RecyclerView) findViewById(R.id.rv_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);
        //Adapter mAdapter = new Adapter(this, mResults);
        EventAdapter1 mAdapter = new EventAdapter1(this, mResults);
        mRecycler.setAdapter(mAdapter);
    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Notification.this, ActivityMenu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected

}
