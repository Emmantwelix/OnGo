package com.group9.ongo.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.models.Flight;
import com.group9.ongo.business.services.FlightService;

import java.util.List;
import java.util.Locale;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<Flight> flights;
    private  FlightService flightService;
    private OnFlightClickListener listener;

    public interface OnFlightClickListener {
        void onFlightClick(Flight flight);
    }

    public FlightAdapter(List<Flight> flights,FlightService flightService, OnFlightClickListener listener) {
        this.flights = flights;
        this.flightService = flightService;
        this.listener = listener;
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
        Flight flight = flights.get(position);

        String price = String.format(Locale.ROOT, "$%.2f",flight.getPrice());
        int hours = flightService.getDurationHours(flight);
        int min = flightService.getDurationRemainingMinutes(flight);
        String duration = String.format(Locale.ROOT, "%dhr %dmin",hours,min);
        holder.origin.setText(flight.getOrigin());
        holder.destination.setText(flight.getDestination());
        holder.departTime.setText(flight.getDepartTimeString());
        holder.landTime.setText(flight.getLandTimeString());
        holder.airlines.setText(flight.getAirline());
        holder.price.setText(price);
        holder.duration.setText(duration);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFlightClick(flight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView origin, destination, departTime, landTime, airlines, price, duration;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            origin = itemView.findViewById(R.id.originText);
            destination = itemView.findViewById(R.id.destinationText);
            departTime = itemView.findViewById(R.id.originTime);
            landTime = itemView.findViewById(R.id.destinationTime);
            airlines = itemView.findViewById(R.id.airlines);
            price = itemView.findViewById(R.id.price);
            duration = itemView.findViewById(R.id.duration);

        }
    }
}
