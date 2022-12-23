package com.ymca.locator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {
    private List<Driver> driverList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,contactno,experience,busnum;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewName);
            contactno = (TextView) view.findViewById(R.id.textViewVersion);
            image = (ImageView) view.findViewById(R.id.imageView);
            experience=(TextView)view.findViewById(R.id.textViewExperience);
            busnum=(TextView)view.findViewById(R.id.textViewBusnum);
        }
    }


    public DriverAdapter(List<Driver> driverList) {
        this.driverList = driverList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.name.setText(driver.getName());
        holder.contactno.setText(driver.getContactno());
        holder.image.setImageResource(driver.getImage());
        holder.experience.setText(driver.getExperience());
        holder.busnum.setText(driver.getBusnum());
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }
}
