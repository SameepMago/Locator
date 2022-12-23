package com.ymca.locator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder>{
    private List<Route> routeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView route,busnum;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            route = (TextView) view.findViewById(R.id.textroute);
            image = (ImageView) view.findViewById(R.id.imageViewroute);
            busnum=(TextView)view.findViewById(R.id.textBusnum);
        }
    }


    public RouteAdapter(List<Route> routeList) {
        this.routeList = routeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.busnum.setText(route.getBusNum());
        holder.route.setText(route.getRoute());
        holder.image.setImageResource(route.getImage());
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
