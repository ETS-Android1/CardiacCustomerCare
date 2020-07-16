package com.kfs.cardiaccustomercare.C3Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.kfs.cardiaccustomercare";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_RECORDS = "records";

    // UserResults is an inner class that defines the contents of the table records which holds user records`s results
    public static final class UserResults implements BaseColumns {

        // records content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECORDS).build();


        // records table and column names
        public static final String TABLE_NAME="records";


        // Since UserResults implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_result="result";
        public static final String COLUMN_Date="date";

    }
}
