package com.ymca.locator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DriverDetails extends AppCompatActivity {
    private List<Driver> driverList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverAdapter mAdapter;

    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.driver_detail);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mAdapter = new DriverAdapter(driverList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
    }
    private void prepareMovieData() {
        Driver movie = new Driver("Amar Nath","+917404321361","Experience: 20yrs","Bus Number:7620",R.drawable.driver1);
        driverList.add(movie);
        movie = new Driver("Satish Kumar","+919416958043","Experience: 10yrs","Bus Number:7622",R.drawable.driver2);
        driverList.add(movie);
        movie = new Driver("Isser Singh","+919992815072","Experience: 15yrs","Bus Number:4297",R.drawable.driver4);
        driverList.add(movie);
        movie = new Driver("Sukhdev Singh","+918950135321","Experience: 25yrs","Bus Number:0516",R.drawable.driver3);
        driverList.add(movie);
        movie = new Driver("Netar Singh","+918395926642","Experience: 20yrs","Bus Number:7619",R.drawable.driver1);
        driverList.add(movie);
        movie = new Driver("Surinder Singh","+918607776935","Experience: 10yrs","Bus Number:1185",R.drawable.driver2);
        driverList.add(movie);
        movie = new Driver("Gurcharan","+919671149665","Experience: 15yrs","Bus Number:1186",R.drawable.driver4);
        driverList.add(movie);
        movie = new Driver("Arjun Gulati","+919416178326","Experience: 25yrs","Bus Number:1195",R.drawable.driver3);
        driverList.add(movie);
        movie = new Driver("Gurnam Singh","+919671792365","Experience: 20yrs","Bus Number:4298",R.drawable.driver1);
        driverList.add(movie);
        movie = new Driver("Rajesh Kumar","+919991727657","Experience: 10yrs","Bus Number:5116",R.drawable.driver2);
        driverList.add(movie);
        movie = new Driver("Amar Nath(RR)","+919896038694","Experience: 15yrs","Bus Number:5117",R.drawable.driver4);
        driverList.add(movie);
        mAdapter.notifyDataSetChanged();
    }
}