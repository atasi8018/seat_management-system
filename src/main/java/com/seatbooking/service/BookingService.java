package com.seatbooking.service;

import com.seatbooking.exception.InvalidBookingException;
import com.seatbooking.model.Booking;
import com.seatbooking.model.Event;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final Event event;

    private final ArrayList<Integer> availableSeats = new ArrayList<>();
    private final ArrayList<Booking> bookings = new ArrayList<>();

    public BookingService(Event event) {
        this.event = event;

        for (int seat = 1;
             seat <= event.getTotalSeats();
             seat++) {
            availableSeats.add(seat);
        }
    }

    public synchronized Booking bookSeat(String customerName, int seatNumber) throws InvalidBookingException {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name field should not be empty");
        }
        validateSeat(seatNumber);

        if (!availableSeats.contains(seatNumber)) {
            throw new InvalidBookingException(
                    "Seat" + seatNumber + " is already booked"
            );
        }
        availableSeats.remove(Integer.valueOf(seatNumber));
        Booking booking = new Booking(customerName, seatNumber);
        bookings.add(booking);
        return booking;


    }

    public synchronized List<Integer> getAvailableSeats() {
        return new ArrayList<>(availableSeats);
    }

    public synchronized List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    private void validateSeat(int seatNumber) throws InvalidBookingException {
        if (seatNumber < 1 || seatNumber > event.getTotalSeats())
            throw new InvalidBookingException(
                    "Invalid seat number : " + seatNumber + ",Valid seats are 1 to : "
                            + event.getTotalSeats()
            );


    }

}
