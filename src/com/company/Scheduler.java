package com.company;

import org.apache.commons.lang3.concurrent.TimedSemaphore;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Scheduler extends Thread {

    // PROGRAM COUNTER
    private AtomicInteger Program_Counter;

    // COUNTERS
    private AtomicInteger Worker1_Counter;
    private AtomicInteger Worker2_Counter;
    private AtomicInteger Worker4_Counter;
    private AtomicInteger Worker16_Counter;

    // OVERRUN COUNTERS
    private AtomicInteger Worker1_Overrun_Counter;
    private AtomicInteger Worker2_Overrun_Counter;
    private AtomicInteger Worker4_Overrun_Counter;
    private AtomicInteger Worker16_Overrun_Counter;

    // TIMED SEMAPHORE
    private TimedSemaphore Scheduler_Semaphore;

    // WORKER SEMAPHORES
    private BinarySemaphore Worker1_Semaphore;
    private BinarySemaphore Worker2_Semaphore;
    private BinarySemaphore Worker4_Semaphore;
    private BinarySemaphore Worker16_Semaphore;

    // WORKERS
    private Worker W1;
    private Worker W2;
    private Worker W4;
    private Worker W16;


    // EXECUTOR
    private ExecutorService Executor = Executors.newSingleThreadExecutor();

    Scheduler (
            Worker W1, Worker W2, Worker W3, Worker W4,
            TimedSemaphore Scheduler_Semaphore,
            AtomicInteger Program_Counter,
            AtomicInteger Worker1_Counter,
            AtomicInteger Worker2_Counter,
            AtomicInteger Worker4_Counter,
            AtomicInteger Worker16_Counter,
            AtomicInteger Worker1_Overrun_Counter,
            AtomicInteger Worker2_Overrun_Counter,
            AtomicInteger Worker4_Overrun_Counter,
            AtomicInteger Worker16_Overrun_Counter,
            BinarySemaphore Worker1_Semaphore,
            BinarySemaphore Worker2_Semaphore,
            BinarySemaphore Worker4_Semaphore,
            BinarySemaphore Worker16_Semaphore
    )
    {
        this.W1 = W1;
        this.W2 = W2;
        this.W4 = W3;
        this.W16 = W4;
        this.Scheduler_Semaphore = Scheduler_Semaphore;
        this.Program_Counter = Program_Counter;
        this.Worker1_Counter = Worker1_Counter;
        this.Worker2_Counter = Worker2_Counter;
        this.Worker4_Counter = Worker4_Counter;
        this.Worker16_Counter = Worker16_Counter;
        this.Worker1_Overrun_Counter = Worker1_Overrun_Counter;
        this.Worker2_Overrun_Counter = Worker2_Overrun_Counter;
        this.Worker4_Overrun_Counter = Worker4_Overrun_Counter;
        this.Worker16_Overrun_Counter = Worker16_Overrun_Counter;
        this.Worker1_Semaphore = Worker1_Semaphore;
        this.Worker2_Semaphore = Worker2_Semaphore;
        this.Worker4_Semaphore = Worker4_Semaphore;
        this.Worker16_Semaphore = Worker16_Semaphore;
    }
@Override
public void start() {
        run();
}
@Override
public void run()    {
    try   {
       Scheduler_Semaphore.acquire();
        IntStream.range(0,160).forEach(iteration ->   {
            try {
                Period.run();
            }   catch (Exception e) { }
        });
    } catch(Exception e)   {
    }
}





private Runnable Period = new Runnable() {
    public void run()   {
        System.out.println("----------------------------------" + (System.nanoTime()/100000000));
        try {
            // SEMAPHORE ACQUIRE
            Scheduler_Semaphore.acquire();
            System.out.println("Scheduler Acquires");
            Worker_Period(W1,Worker1_Semaphore,Worker1_Overrun_Counter, Worker1_Counter);
            Worker_Period(W2,Worker2_Semaphore,Worker2_Overrun_Counter, Worker2_Counter);
            Worker_Period(W4,Worker4_Semaphore,Worker4_Overrun_Counter, Worker4_Counter);
            Worker_Period(W16,Worker16_Semaphore,Worker16_Overrun_Counter, Worker16_Counter);
            Program_Counter.incrementAndGet();
        } catch (Exception e)   {

        }
    }
};




    private void Worker_Period(Worker worker, BinarySemaphore semaphore, AtomicInteger OverrunCounter, AtomicInteger ProgramCounter)   {
        if(!worker.isAlive())   {
            try {
                Future<?> worker_Handle = Executor.submit(worker);
                worker_Handle.get(10, TimeUnit.MILLISECONDS);
            }   catch (Exception e)  {

            }
        }
        if (semaphore.countingSemaphore.availablePermits() == 0) {
            if (ProgramCounter.get() != 0) {
                OverrunCounter.incrementAndGet();
            }
        }
        semaphore.release();
        System.out.println("Worker " + 160/worker.Iterations + " Released For Work" );
    }


    //        System.out.println("Worker 1 Ran " + Worker1_Counter.get() + " times and overran " + Worker1_Overrun_Counter.get() + " times.");
//        System.out.println("Worker 2 Ran " + Worker2_Counter.get() + " times and overran " + Worker2_Overrun_Counter.get() + " times.");
//        System.out.println("Worker 4 Ran " + Worker4_Counter.get() + " times and overran " + Worker4_Overrun_Counter.get() + " times.");
//        System.out.println("Worker 16 Ran " + Worker16_Counter.get() + " times and overran " + Worker16_Overrun_Counter.get() + " times.");
//
}