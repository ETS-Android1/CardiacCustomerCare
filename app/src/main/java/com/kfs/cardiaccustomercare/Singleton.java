package com.kfs.cardiaccustomercare;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {

    private static Singleton instance;
    private RequestQueue requestQueue;
    private static Context context;


    private Singleton (Context context)
    {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Singleton getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new Singleton(context);
        }
        return instance;
    }


    public <T> void addToRequest(Request<T> request)
    {
        requestQueue.add(request);
    }
}
