package com.ymca.locator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifications extends AppCompatActivity {

    List<Notify> GetDataAdapter1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;
    String JSON_IMAGE_TITLE_NAME = "message";
    String Stamp="date";
    StringRequest jsonArrayRequest ;
    TextView notification;

    RequestQueue requestQueue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        GetDataAdapter1 = new ArrayList<>();
        notification=(TextView)findViewById(R.id.notification);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview2);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        JSON_DATA_WEB_CALL();
    }
    public void JSON_DATA_WEB_CALL(){
        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressnotification);
        progressBar.setVisibility(View.VISIBLE);



        jsonArrayRequest = new StringRequest(Request.Method.POST,"",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse( String response) {
                        Log.d("",response);
                        progressBar.setVisibility(View.GONE);
                        if(response.trim().equals("0 results")){
                            notification.setVisibility(View.VISIBLE);
                        }
                        else {
                            JSONArray a = null;
                            try {
                                a = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSON_PARSE_DATA_AFTER_WEBCALL(a);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Notifications.this, "Network Problem", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                params.put("busnum",sharedPreferences.getString("Busnum", ""));
                return params;
            }

        };
        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray a){


        for(int i = a.length()-1; i>=0 ;i--) {

            Notify GetDataAdapter2 = new Notify();
            JSONObject json = null;
            try {
                json =a.getJSONObject(i);

                GetDataAdapter2.setName(json.getString(JSON_IMAGE_TITLE_NAME));
                GetDataAdapter2.setStamp(json.getString(Stamp));


            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);
        }

        recyclerViewadapter = new NotificationAdapter(GetDataAdapter1, this);

        recyclerView.setAdapter(recyclerViewadapter);
    }

}

