package com.company;

import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {

//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Turn on Processor Affinity then enter any key");
//        keyboard.next();
//        System.out.println("Loading...");

        // PROGRAM COUNTER
        AtomicInteger Program_Counter = new AtomicInteger(0);

        // RESULTS
        List<String> Results = new ArrayList<>();

        // COUNTERS
        AtomicInteger Worker1_Counter = new AtomicInteger(0);
        AtomicInteger Worker2_Counter = new AtomicInteger(0);
        AtomicInteger Worker4_Counter = new AtomicInteger(0);
        AtomicInteger Worker16_Counter = new AtomicInteger(0);

        // OVERRUN COUNTERS
        AtomicInteger Worker1_Overrun_Counter = new AtomicInteger(0);
        AtomicInteger Worker2_Overrun_Counter = new AtomicInteger(0);
        AtomicInteger Worker4_Overrun_Counter = new AtomicInteger(0);
        AtomicInteger Worker16_Overrun_Counter = new AtomicInteger(0);

        // WORKER BinarySemaphoreS
        BinarySemaphore Worker1_BinarySemaphore = new BinarySemaphore(1 );
        BinarySemaphore Worker2_BinarySemaphore = new BinarySemaphore(1);
        BinarySemaphore Worker4_BinarySemaphore = new BinarySemaphore(1);
        BinarySemaphore Worker16_BinarySemaphore = new BinarySemaphore(1);

        // CREATE NEW WORKER THREADS
        Worker W1 = new Worker(160, Worker1_BinarySemaphore, Worker1_Counter, Results);
        Worker W2 = new Worker(80, Worker2_BinarySemaphore, Worker2_Counter, Results);
        Worker W3 = new Worker(40, Worker4_BinarySemaphore, Worker4_Counter, Results);
        Worker W4 = new Worker(10, Worker16_BinarySemaphore, Worker16_Counter, Results);

        // SET PRIORITIES
        W1.setPriority(8);
        W2.setPriority(7);
        W3.setPriority(6);
        W4.setPriority(5);


       Thread.currentThread().setPriority(10);


        // TIMED BinarySemaphore
        TimedSemaphore Scheduler_BinarySemaphore = new TimedSemaphore(100, TimeUnit.MILLISECONDS, 1);

        // CREATE NEW SCHEDULER
        Scheduler Scheduler = new Scheduler(
                W1, W2, W3, W4,
                Scheduler_BinarySemaphore,
                Program_Counter,
                Worker1_Counter,
                Worker2_Counter,
                Worker4_Counter,
                Worker16_Counter,
                Worker1_Overrun_Counter,
                Worker2_Overrun_Counter,
                Worker4_Overrun_Counter,
                Worker16_Overrun_Counter,
                Worker1_BinarySemaphore,
                Worker2_BinarySemaphore,
                Worker4_BinarySemaphore,
                Worker16_BinarySemaphore,
                Results
        );
        Scheduler.setPriority(9);


        Scheduler.start();

        try {
            Scheduler.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            for (String a : Results)
            {
                System.out.println(a);
            }
        }


    }
}