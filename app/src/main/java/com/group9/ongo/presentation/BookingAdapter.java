package com.group9.ongo.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

    public interface BookingActionListener {
        void onModify(BookingDetails b);
        void onCancel(BookingDetails b);
        void onEditInfo(BookingDetails b);
        void onViewDetails(BookingDetails b);
    }

    private List<BookingDetails> bookings;
    private FlightService flightService;
    private BookingActionListener listener;

    public BookingAdapter(List<BookingDetails> bookings, FlightService flightService, BookingActionListener listener) {
        this.bookings = bookings;
        this.flightService = flightService;
        this.listener = listener;
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

        // Set the airline logo using the Airline enum
        holder.airlineLogo.setImageResource(Airline.fromName(flight.getAirline()).getLogoResId());

        holder.btnMoreOptions.setOnClickListener(v -> showPopupMenu(v, details));
    }

    private void showPopupMenu(View view, BookingDetails details) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_booking_options, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_modify) {
                Toast.makeText(view.getContext(), "Feature Under Construction", Toast.LENGTH_SHORT).show();
                listener.onModify(details);
                return true;
            } else if (id == R.id.action_cancel) {
                listener.onCancel(details);
                return true;
            } else if (id == R.id.action_edit_info) {
                Toast.makeText(view.getContext(), "Feature Under Construction", Toast.LENGTH_SHORT).show();
                listener.onEditInfo(details);
                return true;
            } else if (id == R.id.action_view_details) {
                Toast.makeText(view.getContext(), "Feature Under Construction", Toast.LENGTH_SHORT).show();
                listener.onViewDetails(details);
                return true;
            }
            return false;
        });
        popup.show();
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
                flightNumber, bookingStatus, destinationCode, originCode;
        ShapeableImageView airlineLogo;
        ImageButton btnMoreOptions;

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
            btnMoreOptions = itemView.findViewById(R.id.btn_more_options);
        }
    }
}
