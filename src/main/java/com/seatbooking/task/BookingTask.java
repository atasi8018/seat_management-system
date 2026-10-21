package com.seatbooking.task;

import com.seatbooking.exception.InvalidBookingException;
import com.seatbooking.model.Booking;
import com.seatbooking.service.BookingService;

public class BookingTask  implements Runnable{
    private final BookingService service;
    private final String customerName;
    private final int seatNumber;


    public BookingTask(BookingService service, String customerName , int seatNumber){
        this.service = service;
        this.customerName = customerName;
        this.seatNumber = seatNumber;
    }

    @Override
    public void run(){
        try{
            Booking booking= service.bookSeat(customerName , seatNumber);

            System.out.println( "[" + Thread.currentThread().getName()
                    + "] [SUCCESS] " + booking);

        } catch (InvalidBookingException e) {

            System.out.println(
                    "[" + Thread.currentThread().getName()
                            + "] [REJECTED] " + customerName
                            + ": " + e.getMessage());

        } finally {

            System.out.println(
                    "Booking attempt completed for "
                            + customerName);
        }

    }

}
