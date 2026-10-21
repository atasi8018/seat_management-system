package com.seatbooking.model;

public class Event {
    private final String eventName;
    private final int totalSeats;


    public Event(String eventName , int totalSeats){
        if (totalSeats <= 0) {
            throw new IllegalArgumentException(
                    "Total seats must be greater than zero");
        }
        if (eventName == null || eventName.isBlank()){
            throw  new IllegalArgumentException(
                    "Event name  field should not be empty");
        }
                this.eventName = eventName;
        this.totalSeats = totalSeats;

    }
    public String getEventName(){
        return eventName;
    }

    public int getTotalSeats(){
        return totalSeats;
    }

}
