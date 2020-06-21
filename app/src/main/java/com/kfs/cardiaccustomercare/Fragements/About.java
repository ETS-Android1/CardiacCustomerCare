package com.kfs.cardiaccustomercare.Fragements;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kfs.cardiaccustomercare.R;

public class About extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("aboutuss","1");
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    public static About newInstance ()
    {
        About about=new About();
        return about;
    }
}
