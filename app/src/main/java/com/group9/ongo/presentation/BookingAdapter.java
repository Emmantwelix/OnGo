package com.group9.ongo.presentation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.group9.ongo.R;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.models.Airline;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<BookingDetails> bookings;
    private FlightService flightService;

    public BookingAdapter(List<BookingDetails> bookings, FlightService flightService) {
        this.bookings = bookings;
        this.flightService = flightService;
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
        Booking booking = details.getBooking();

        holder.passengerName.setText(passenger.getFirstName() + " " + passenger.getLastName());
        holder.origin.setText(flight.getOrigin());
        holder.destination.setText(flight.getDestination());
        holder.originTime.setText(flight.getDepartTimeString());
        holder.destinationTime.setText(flight.getLandTimeString());
        holder.airline.setText(flight.getAirline());
        holder.flightNumber.setText(flight.getFlightNumber());
        holder.bookingStatus.setText(booking.getBookingStatus());
        holder.originCode.setText(flightService.getAirportCode(flight.getOrigin()));
        holder.destinationCode.setText(flightService.getAirportCode(flight.getDestination()));
        holder.flightDate.setText(flight.getDateString());

        // Set the airline logo using the Airline enum
        holder.airlineLogo.setImageResource(Airline.fromName(flight.getAirline()).getLogoResId());

        // Open BookingDetailsActivity when card is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookingDetailsActivity.class);
            intent.putExtra("booking_id", booking.getBookingId());
            v.getContext().startActivity(intent);
        });
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
        TextView passengerName, origin, destination, originTime, destinationTime, airline,
                flightNumber, bookingStatus, destinationCode, originCode, flightDate;
        ShapeableImageView airlineLogo;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerName = itemView.findViewById(R.id.text_passenger_name);
            origin = itemView.findViewById(R.id.text_origin);
            destination = itemView.findViewById(R.id.text_destination);
            originTime = itemView.findViewById(R.id.text_origin_time);
            destinationTime = itemView.findViewById(R.id.text_destination_time);
            airline = itemView.findViewById(R.id.text_airline);
            airlineLogo = itemView.findViewById(R.id.airlineLogo);
            flightNumber = itemView.findViewById(R.id.text_flight_number);
            bookingStatus = itemView.findViewById(R.id.text_status);
            destinationCode = itemView.findViewById(R.id.text_destination_rcode);
            originCode = itemView.findViewById(R.id.text_origin_rcode);
            flightDate = itemView.findViewById(R.id.text_flight_date);
        }
    }
}
