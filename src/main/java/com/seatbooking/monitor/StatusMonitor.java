package com.seatbooking.monitor;

import com.seatbooking.service.BookingService;

public class StatusMonitor implements Runnable{
    private volatile boolean running = true;
    private final long intervalMillis;
    private final BookingService service;

    public StatusMonitor(long intervalMillis,BookingService service){

        if (intervalMillis <= 0){
            throw new IllegalArgumentException(
            "Monitoring interval must be positive");
        }

        if(service == null){
            throw new IllegalArgumentException(
                    "Booking service should not be null"
            );
        }
        this.intervalMillis = intervalMillis;
       this.service = service;
    }
    @Override
    public void run(){
        while (running){
            System.out.println(
                    "[" + Thread.currentThread().getName()
                            + "] Booking service is ACTIVE");

            try {
                Thread.sleep(intervalMillis);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Status monitor stopped.");
        }

        public void stopMonitoring(){
        running = false;
        }

    }


