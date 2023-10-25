package com.example.t1bicing;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {
    private static Context context;
    private RequestQueue requestQueue;
    private  static RequestQueueSingleton instance;

    private RequestQueueSingleton(Context context){
        RequestQueueSingleton.context = context;

    }

    public static RequestQueueSingleton getInstance(Context context){
        if(instance==null){
            instance = new RequestQueueSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
