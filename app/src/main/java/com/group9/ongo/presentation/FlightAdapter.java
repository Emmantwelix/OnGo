package com.group9.ongo.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.models.FlightClass;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<FlightClass> flights;

    public FlightAdapter(List<FlightClass> flights) {
        this.flights = flights;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightClass flight = flights.get(position);
        holder.airline.setText(flight.getAirline());
        holder.destination.setText(flight.getDestination());
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView airline, destination;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            airline = itemView.findViewById(R.id.tvAirline);
            destination = itemView.findViewById(R.id.tvDestination);
        }
    }
}
