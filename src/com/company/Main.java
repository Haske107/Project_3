package com.company;

import org.apache.commons.lang3.concurrent.TimedSemaphore;

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

        // WORKER SEMAPHORES
        BinarySemaphore Worker1_Semaphore = new BinarySemaphore( true);
        BinarySemaphore Worker2_Semaphore = new BinarySemaphore(true);
        BinarySemaphore Worker4_Semaphore = new BinarySemaphore(true);
        BinarySemaphore Worker16_Semaphore = new BinarySemaphore(true);

        // CREATE NEW WORKER THREADS
        Worker W1 = new Worker(160, Worker1_Semaphore, Worker1_Counter);
        Worker W2 = new Worker(80, Worker2_Semaphore, Worker2_Counter);
        Worker W3 = new Worker(40, Worker4_Semaphore, Worker4_Counter);
        Worker W4 = new Worker(10, Worker16_Semaphore, Worker16_Counter);

        // SET PRIORITIES
        W1.setPriority(8);
        W2.setPriority(7);
        W3.setPriority(6);
        W4.setPriority(5);


       Thread.currentThread().setPriority(10);
       /* try {
            Worker1_Semaphore.acquire();
            Worker2_Semaphore.acquire();
            Worker4_Semaphore.acquire();
            Worker16_Semaphore.acquire();
        }   catch (Exception e)  { }
*/
//        Future<?> W1_Handle = Executor.submit(W1);
//        Future<?> W2_Handle = Executor.submit(W2);
//        Future<?> W3_Handle = Executor.submit(W3);
//        Future<?> W4_Handle = Executor.submit(W4);



        // TIMED SEMAPHORE
        TimedSemaphore Scheduler_Semaphore = new TimedSemaphore(400, TimeUnit.MILLISECONDS, 1);

        // CREATE NEW SCHEDULER
        Scheduler Scheduler = new Scheduler(
                W1, W2, W3, W4,
                Scheduler_Semaphore,
                Program_Counter,
                Worker1_Counter,
                Worker2_Counter,
                Worker4_Counter,
                Worker16_Counter,
                Worker1_Overrun_Counter,
                Worker2_Overrun_Counter,
                Worker4_Overrun_Counter,
                Worker16_Overrun_Counter,
                Worker1_Semaphore,
                Worker2_Semaphore,
                Worker4_Semaphore,
                Worker16_Semaphore
        );
        Scheduler.setPriority(9);


        Scheduler.start();


    }
}