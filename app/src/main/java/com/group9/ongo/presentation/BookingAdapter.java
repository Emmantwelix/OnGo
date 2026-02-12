package com.group9.ongo.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<BookingDetails> bookings;

    public BookingAdapter(List<BookingDetails> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingDetails details = bookings.get(position);
        Flight flight = details.getFlight();
        Passenger passenger = details.getPassenger();

        holder.passengerName.setText(passenger.getFirstName() + " " + passenger.getLastName());
        holder.origin.setText(flight.getOrigin());
        holder.destination.setText(flight.getDestination());
        holder.originTime.setText(flight.getDepartTime());
        holder.destinationTime.setText(flight.getLandTime());
        holder.airline.setText(flight.getAirline());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void setBookings(List<BookingDetails> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView passengerName, origin, destination, originTime, destinationTime, airline;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerName = itemView.findViewById(R.id.text_passenger_name);
            origin = itemView.findViewById(R.id.text_origin);
            destination = itemView.findViewById(R.id.text_destination);
            originTime = itemView.findViewById(R.id.text_origin_time);
            destinationTime = itemView.findViewById(R.id.text_destination_time);
            airline = itemView.findViewById(R.id.text_airline);
        }
    }
}
