package com.kfs.cardiaccustomercare.C3Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kfs.cardiaccustomercare.C3Database.Contract;

public class DatabaseHelper extends SQLiteOpenHelper {


    // The name of the database
    private static final String DATABASE_NAME = "CardiacCustomerCare.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //table of records
        final String SQL_CREATA_TABLE_RECORDS="CREATE TABLE IF NOT EXISTS "+ Contract.UserResults.TABLE_NAME+"("+
                Contract.UserResults._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Contract.UserResults.COLUMN_result+" TEXT NOT NULL,"+
                Contract.UserResults.COLUMN_Date+" TEXT NOT NULL"+
                ");";
        db.execSQL(SQL_CREATA_TABLE_RECORDS);
        Log.i("SQLiteOpenHelper","Record Table is created..");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Contract.UserResults.TABLE_NAME);
        onCreate(db);

    }
}
