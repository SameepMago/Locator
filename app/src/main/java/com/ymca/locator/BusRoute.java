package com.ymca.locator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

public class BusRoute extends AppCompatActivity {

    private List<Route> routeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RouteAdapter mAdapter;

    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.route);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_route);

        mAdapter = new RouteAdapter(routeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
    }
    private void prepareMovieData() {
        Route route = new Route("Bus Number: 7620","Route:"+"\n"+"DurgaNagar"+"\n"+"Sector9"+"\n"+"Manav Chowk"+"\n"+"Polytechnic",R.drawable.bus1);
        routeList.add(route);
        route = new Route("Bus Number:7622","Route:"+"\n"+"Agarsen"+"\n"+"Polytechnic"+"\n"+"Model Town"+"\n"+"Jandli",R.drawable.bus2);
        routeList.add(route);
        route = new Route("Bus Number:4297","Route:"+"\n"+"Baldev Nagar"+"\n"+"Kalka Chowk"+"\n"+"Police Line"+"\n"+"Prem Nagar"+"\n"+"Inco"+"\n"+"Model Town"+"\n"+"Jandli",R.drawable.bus3);
        routeList.add(route);
        route = new Route("Bus Number:0516","Route:"+"\n"+"Durga Nagar"+"\n"+"Manav Chowk"+"\n"+"Agarsen"+"\n"+"Polytechnic"+"\n"+"Jandli",R.drawable.bus4);
        routeList.add(route);
        route = new Route("Bus Number:7619","Route:"+"\n"+"Deluxe Dhaba"+"\n"+"Janta Sweets"+"\n"+"Civil Hospital"+"\n"+"Mahesh Nagar",R.drawable.bus1);
        routeList.add(route);
        route = new Route("Bus Number:1185","Route:"+"\n"+"Defence Colony"+"\n"+"BOH"+"\n"+"Civil Hospital"+"\n"+"Mahesh Nagar",R.drawable.bus1);
        routeList.add(route);
        route = new Route("Bus Number:1186","Route:"+"\n"+"Civil Hospital"+"\n"+"Mahesh Nagar"+"\n"+"Mithapur",R.drawable.bus2);
        routeList.add(route);
        route = new Route("Bus Number:1195","Route:"+"\n"+"Kharga Canteen"+"\n"+"Babyal"+"\n"+"BOH"+"\n"+"Defence Colony"+"\n"+"Civil Hospital"+"\n"+"Mahesh Nagar",R.drawable.bus1);
        routeList.add(route);
        route = new Route("Bus Number:4298","Route:"+"\n"+"Shahabad"+"\n"+"Markanda"+"\n"+"Naraingarh ka Mazra"+"\n"+"Kesri"+"\n"+"Saha"+"\n"+"Mithapur",R.drawable.bus2);
        routeList.add(route);
        route = new Route("Bus Number:5116","Route:"+"\n"+"Yamuna Nagar"+"\n"+"Jagadhri"+"\n"+"Thana Chhapar"+"\n"+"Dosadka"+"\n"+"Barara"+"\n"+"Mullana"+"\n"+"Kalpi"+"\n"+"Saha",R.drawable.bus3);
        routeList.add(route);
        route = new Route("Bus Number:5117","Route:"+"\n"+"Raipur Rani"+"\n"+"Bhurewala"+"\n"+"Naraingarh"+"\n"+"Sahzadpur"+"\n"+"Patrchri"+"\n"+"Kadasan"+"\n"+"Saha"+"\n"+"Mithapur",R.drawable.bus4);
        routeList.add(route);
        mAdapter.notifyDataSetChanged();
    }
}