package com.kfs.cardiaccustomercare.Fragements;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kfs.cardiaccustomercare.C3Database.Contract;

import com.kfs.cardiaccustomercare.R;

import java.util.List;


public class Profile extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{
    public Profile() {
        // Required empty public constructor
    }

    private static final int TASK_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private ProfileCursorAdapter mAdapter;
    RecyclerView mRecyclerView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("profile a7o","1");
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        // Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewTasks);
        Log.e("profile a7o","1");
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.e("profile a7o","1");
        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new ProfileCursorAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        Log.e("profile a7o","1");
        getLoaderManager().restartLoader(TASK_LOADER_ID , null,Profile.this);
        Log.e("3","31");
        return v;
    }

    public static Profile newInstance ()
    {
        Profile profile=new Profile();
        return profile;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {


            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getActivity().getApplicationContext().getContentResolver().query(Contract.UserResults.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e("exxxx", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
