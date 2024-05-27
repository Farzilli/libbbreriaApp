package com.example.libbbreriaapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataFetcher {
    private Context mContext;
    private RequestQueue queue;

    public DataFetcher(Context context) {
        mContext = context;
        queue = Volley.newRequestQueue(mContext);
    }

    public void get(String url, String root, final DataListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                (response) -> {
                    try {
                        listener.onDataFetched(response.getJSONArray(root));
                    } catch (Exception err) {
                        listener.onDataFetched(response);
                    }
                },
                (err) -> {
                    listener.onError("Error fetching data: " + err.getMessage());
                });
        queue.add(request);
    }

    public void post(String url, final Map<String, String> postData, final DataListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                (response) -> {
                        listener.onDataFetched(response);
                },
                (error)-> {
                        listener.onError("Error fetching data: " + error.getMessage());
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return postData;
            }
        };
        queue.add(request);
    }

    public interface DataListener {
        default void onDataFetched(JSONArray data){}
        default void onDataFetched(JSONObject data){}
        default void onDataFetched(String data){}
        void onError(String errorMessage);
    }
}