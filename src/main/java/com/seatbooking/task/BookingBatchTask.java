package com.seatbooking.task;

import java.util.List;

public class BookingBatchTask implements Runnable{
    private final List<BookingTask> tasks;

    public BookingBatchTask(List<BookingTask> tasks){
        this.tasks = tasks;
    }
    @Override
    public void run(){
        for (BookingTask task : tasks){
            task.run();
        }
    }
}
