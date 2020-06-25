package com.kfs.cardiaccustomercare.Fragements;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kfs.cardiaccustomercare.C3Database.Contract;
import com.kfs.cardiaccustomercare.C3Database.DatabaseHelper;

import com.kfs.cardiaccustomercare.Home;
import com.kfs.cardiaccustomercare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Record extends Fragment implements Handler.Callback {


    Button start , stop ,logout;
    String path = Environment.getExternalStorageDirectory().getPath()+"/MyRecords.wav";


    Button buttonStart;
    ProgressBar progressBar;
    TextView textViewCounter;
    CountDownTimer countDownTimer;
    ImageView imageViewHeart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        Log.e("recorddd","1");
        start = v.findViewById(R.id.startt);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                // Put the task description and selected mPriority into the ContentValues
                contentValues.put(Contract.UserResults.COLUMN_result, "normall");
                contentValues.put(Contract.UserResults.COLUMN_Date, "1/1/1/1");
                // Insert the content values via a ContentResolver
                Log.e("mas","1");
                Uri uri = getActivity().getApplicationContext().getContentResolver().insert(Contract.UserResults.CONTENT_URI, contentValues);
                Profile.reCreateLoader();
            }
        });
        Handler handler = new Handler((Handler.Callback) this);
        final com.kfs.cardiaccustomercare.Record record = new com.kfs.cardiaccustomercare.Record(path,handler);

        buttonStart = v.findViewById(R.id.custom_button);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                textViewCounter.setVisibility(View.VISIBLE);
                //start count
                countDownTimer.start();
            }
        });
        progressBar = v.findViewById(R.id.prgress_par);
        textViewCounter = v.findViewById(R.id.text_view_coubter);
        imageViewHeart = v.findViewById(R.id.image_view_health);

        //counter object
        countDownTimer = new CountDownTimer(5000, 1000
        ) {
            //on start countDownTimer
            @Override
            public void onTick(long millisUntilFinished) {
                textViewCounter.setText("00:00:" + millisUntilFinished / 1000);
                record.startRecording();
            }

            //when finish count timer
            @Override
            public void onFinish() {
                textViewCounter.setText("Normal");
                imageViewHeart.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                record.stopRecording();
                StoreRecord();
            }
        };


        logout = (Button)v.findViewById(R.id.logout);

        return v;
    }

    private void StoreRecord() {
        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        DatabaseHelper dbHelper =new DatabaseHelper(getActivity());
        Log.e("n1","11111");

    }


    public static Record newInstance ()
    {
        Record home=new Record();
        return home;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
}
