package com.seatbooking.event;

import com.seatbooking.model.Booking;
import com.seatbooking.model.Event;
import com.seatbooking.monitor.StatusMonitor;
import com.seatbooking.service.BookingService;
import com.seatbooking.task.BookingTask;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SeatBookingApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter event name : ");
        String eventName = sc.nextLine();
        System.out.println("Enter total seats : ");
        int totalSeats = Integer.parseInt(sc.nextLine());


        Event event = new Event(eventName , totalSeats);
        BookingService service = new BookingService(event);
        StatusMonitor monitor = new StatusMonitor(5000, service);

        Thread monitorThread = new Thread(monitor,"status monitor");
        monitorThread.start();

        ExecutorService executor = Executors.newFixedThreadPool(3);
        try{
            boolean running = true;
            while (running) {
                System.out.println("\n===== EVENT BOOKING MENU =====");
                System.out.println("1. Book one seat");
                System.out.println("2. Submit concurrent bookings");
                System.out.println("3. View available seats");
                System.out.println("4. View confirmed bookings");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        System.out.print("Customer name: ");
                        String customer = sc.nextLine();

                        System.out.print("Seat number: ");
                        int seat = Integer.parseInt(sc.nextLine());

                        executor.submit(
                                new BookingTask(service, customer, seat));
                        break;

                    case 2:
                        System.out.print(
                                "Number of booking requests: ");

                        int count = Integer.parseInt(sc.nextLine());

                        List<Future<?>> futures = new ArrayList<>();

                        for (int i = 1; i <= count; i++) {
                            System.out.println(
                                    "Request " + i);

                            System.out.print("Customer name: ");
                            String name = sc.nextLine();

                            System.out.print("Seat number: ");
                            int seatNumber =
                                    Integer.parseInt(sc.nextLine());

                            futures.add(executor.submit(
                                    new BookingTask(
                                            service, name, seatNumber)));
                        }

                        // Wait until every submitted request finishes
                        for (Future<?> future : futures) {
                            future.get();
                        }
                        break;

                    case 3:
                        System.out.println(
                                "Available seats: "
                                        + service.getAvailableSeats());
                        break;

                    case 4:
                        System.out.println("Confirmed bookings:");

                        for (Booking booking : service.getBookings()) {
                            System.out.println(booking);
                        }
                        break;

                    case 5:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid menu choice.");
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } catch (java.util.concurrent.ExecutionException e) {
            System.out.println("Booking task failed: " + e.getCause());

        } finally {
            executor.shutdown();

            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }

            monitor.stopMonitoring();
            monitorThread.interrupt();

            try {
                monitorThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("\n===== FINAL SUMMARY =====");
            System.out.println("Event: " + event.getEventName());
            System.out.println("Bookings: " + service.getBookings());
            System.out.println(
                    "Available seats: " + service.getAvailableSeats());


            System.out.println("Seat booking system finished.");
            sc.close();

        }
        }

    }

