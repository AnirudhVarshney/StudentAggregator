package com.devionlabs.ray.studentaggregator;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.devionlabs.ray.studentaggregator.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 25-04-2016.
 */
public class RecyclerDialog2 extends DialogFragment implements View.OnClickListener {
    private Button mBtnOK;
    TextView mTexttution;
    TextView mTextComputer;
    TextView mTextLibrary;
    TextView mTextsum;
    DBHelper dbHelper;
    ArrayList<Fees> mResults;
    int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);

    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), (int) (size.x * 0.75));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Fee details");
        dbHelper = new DBHelper(getActivity());

        mResults = dbHelper.getAllFees();
        return inflater.inflate(R.layout.recyclerdilog, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnOK = (Button) view.findViewById(R.id.bt_ok);
        mBtnOK.setOnClickListener(this);
        Bundle arguments = getArguments();  //to be used for the position of the recycler view
        if (arguments != null) {
            position = arguments.getInt("POSITION");
        }
        Fees fees = mResults.get(position);
        mTexttution = (TextView) view.findViewById(R.id.tv_tutionvalue);
        mTextComputer = (TextView) view.findViewById(R.id.tv_computervalue);
        mTextLibrary = (TextView) view.findViewById(R.id.tv_libraryvalue);
        mTextsum = (TextView) view.findViewById(R.id.tv_sum);
        mTexttution.setText(String.valueOf(fees.getTutionFee()) + " Rs");
        mTextComputer.setText(String.valueOf(fees.getComputerFee()) + " Rs");
        mTextLibrary.setText(String.valueOf(fees.getLibraryFee()) + " Rs");
        mTextsum.setText(String.valueOf(fees.getTotalSum()) + " Rs");

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
