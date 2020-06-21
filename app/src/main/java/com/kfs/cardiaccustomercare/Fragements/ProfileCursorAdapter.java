package com.kfs.cardiaccustomercare.Fragements;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kfs.cardiaccustomercare.C3Database.Contract;
import com.kfs.cardiaccustomercare.R;


public class ProfileCursorAdapter  extends RecyclerView.Adapter<ProfileCursorAdapter.MyHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public ProfileCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        int resultC = mCursor.getColumnIndex(Contract.UserResults.COLUMN_result);
        int dateC = mCursor.getColumnIndex(Contract.UserResults.COLUMN_Date);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        String result = mCursor.getString(resultC);
        Log.e("proo","1//"+result);
        String date = mCursor.getString(dateC);

        holder.result.setText(result);
        holder.date.setText(date);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    public class MyHolder extends RecyclerView.ViewHolder{

        // Class variables for user result and date TextViews
        TextView result;
        TextView date;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            result = (TextView) itemView.findViewById(R.id.text_record_result);
            date = (TextView) itemView.findViewById(R.id.text_record_date);

        }
    }

}
