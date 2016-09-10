package com.devionlabs.ray.studentaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devionlabs.ray.studentaggregator.AdapterFee2;
import com.devionlabs.ray.studentaggregator.R;
import com.devionlabs.ray.studentaggregator.helper.DBHelper;
import com.devionlabs.ray.studentaggregator.helper.LocalDB;
import com.devionlabs.ray.studentaggregator.model.Fee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityFee extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterFee2 adapterFee;
    DBHelper dbHelper;
    ArrayList<Fee> feeList;
    LinearLayout footer;

    TextView textViewDueDate;
    TextView textViewAmount;
    LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar);

        localDB = new LocalDB(ActivityFee.this);
        dbHelper = new DBHelper(this);

        footer = (LinearLayout) findViewById(R.id.footer);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);


        feeList = dbHelper.getAllFee(localDB.getStudent().getId());
        adapterFee = new AdapterFee2(this, feeList);
        recyclerView.setAdapter(adapterFee);

        bindUIComponents();
        setOnClickListeners();
        setTextonUIComponents();

        changeDueDateColor();
        //adapterFee.setRecyclerListner(recyclerListner);

    }

    private void changeDueDateColor() {
        Fee fee = localDB.getNextPayment(localDB.getStudent().getId());
        String dueTime = fee.getTime();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate;
        Date todaysDate = new Date();

        try {
            inputDate = fmt.parse(dueTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        inputDate.setDate(inputDate.getDate() - 7);

        if (todaysDate.compareTo(inputDate) > 0) {
            footer.setBackgroundColor(getResources().getColor(R.color.colorAbsent));
        }
    }

    private void setTextonUIComponents() {
        Fee fee = localDB.getNextPayment(localDB.getStudent().getId());
        String dueDate = "Due Date : " + fee.getTime();
        String dueAmount = "Due Amount : " + fee.getAmount() + " INR";
        textViewDueDate.setText(dueDate);
        textViewAmount.setText(dueAmount);

    }

    void bindUIComponents() {
        textViewDueDate = (TextView) findViewById(R.id.textViewDueDate);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
    }

    private void setOnClickListeners() {
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityFee.this, Payment.class);
                startActivity(i);
            }
        });
    }

    /*private void showRecyclerDilog(int position) {
        RecyclerDilog recyclerDilog = new RecyclerDilog();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        recyclerDilog.setArguments(bundle);
        recyclerDilog.show(getSupportFragmentManager(), "Recycler");
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(),ActivityMenu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }
}
