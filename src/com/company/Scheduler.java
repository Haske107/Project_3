package com.company;



import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Scheduler extends Thread {

    // PROGRAM COUNTER
    private AtomicInteger Program_Counter;

    // RESULT
    private List<String> Results;

    // PROGRAM TIME
    private long Time;

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

    // TIMED BinarySemaphore
    private TimedSemaphore Scheduler_BinarySemaphore;

    // WORKER BinarySemaphoreS
    private BinarySemaphore Worker1_BinarySemaphore;
    private BinarySemaphore Worker2_BinarySemaphore;
    private BinarySemaphore Worker4_BinarySemaphore;
    private BinarySemaphore Worker16_BinarySemaphore;

    // WORKERS
    private Worker W1;
    private Worker W2;
    private Worker W4;
    private Worker W16;

    ExecutorService executor = Executors.newSingleThreadExecutor();



    Scheduler (
            Worker W1, Worker W2, Worker W3, Worker W4,
            TimedSemaphore Scheduler_BinarySemaphore,
            AtomicInteger Program_Counter,
            AtomicInteger Worker1_Counter,
            AtomicInteger Worker2_Counter,
            AtomicInteger Worker4_Counter,
            AtomicInteger Worker16_Counter,
            AtomicInteger Worker1_Overrun_Counter,
            AtomicInteger Worker2_Overrun_Counter,
            AtomicInteger Worker4_Overrun_Counter,
            AtomicInteger Worker16_Overrun_Counter,
            BinarySemaphore Worker1_BinarySemaphore,
            BinarySemaphore Worker2_BinarySemaphore,
            BinarySemaphore Worker4_BinarySemaphore,
            BinarySemaphore Worker16_BinarySemaphore,
            List<String> Results
    )
    {
        this.Results = Results;
        this.W1 = W1;
        this.W2 = W2;
        this.W4 = W3;
        this.W16 = W4;
        this.Scheduler_BinarySemaphore = Scheduler_BinarySemaphore;
        this.Program_Counter = Program_Counter;
        this.Worker1_Counter = Worker1_Counter;
        this.Worker2_Counter = Worker2_Counter;
        this.Worker4_Counter = Worker4_Counter;
        this.Worker16_Counter = Worker16_Counter;
        this.Worker1_Overrun_Counter = Worker1_Overrun_Counter;
        this.Worker2_Overrun_Counter = Worker2_Overrun_Counter;
        this.Worker4_Overrun_Counter = Worker4_Overrun_Counter;
        this.Worker16_Overrun_Counter = Worker16_Overrun_Counter;
        this.Worker1_BinarySemaphore = Worker1_BinarySemaphore;
        this.Worker2_BinarySemaphore = Worker2_BinarySemaphore;
        this.Worker4_BinarySemaphore = Worker4_BinarySemaphore;
        this.Worker16_BinarySemaphore = Worker16_BinarySemaphore;
    }
@Override
public void start() {
        run();
}
@Override
public void run()    {
    try   {

       Scheduler_BinarySemaphore.acquire();

        IntStream.range(0,160).forEach(iteration ->   {
            try {
                Future<?> handle = executor.submit(Period);
                handle.get(100, TimeUnit.MILLISECONDS);
            }   catch (Exception e) {
                try {
                    Worker1_BinarySemaphore.doNotify();
                } catch (Exception el) {

                }
                try {
                    Worker2_BinarySemaphore.doNotify();
                } catch (Exception el) {

                }
                try {
                    Worker4_BinarySemaphore.doNotify();
                } catch (Exception el) {

                }
                try {
                    Worker16_BinarySemaphore.doNotify();
                } catch (Exception el) {

                }

            }

        });
    } catch(Exception e)   {
    }
    Results.add("Worker 1 Ran " + Worker1_Counter.get() + " times and overran " + Worker1_Overrun_Counter.get() + " times.");
    Results.add("Worker 2 Ran " + Worker2_Counter.get() + " times and overran " + Worker2_Overrun_Counter.get() + " times.");
    Results.add("Worker 4 Ran " + Worker4_Counter.get() + " times and overran " + Worker4_Overrun_Counter.get() + " times.");
    Results.add("Worker 16 Ran " + Worker16_Counter.get() + " times and overran " + Worker16_Overrun_Counter.get() + " times.");

}





private Runnable Period = new Runnable() {
    public void run()   {
        Time = System.nanoTime();

        try {
            Results.add("Scheduler Waiting");
            Scheduler_BinarySemaphore.acquire();
            Results.add("Scheduler Acquired");

            try {
                if (W1.state == 1){
                    Worker1_BinarySemaphore.doNotify();
                    Results.add("Worker " + 160/W1.Iterations + " notified" );
                }
                Worker_Period(W1,Worker1_BinarySemaphore,Worker1_Overrun_Counter, Worker1_Counter);
            }   catch (Exception e) {

            }
            try {
                if(Program_Counter.get() % 2 ==0) {
                    if (W2.state == 1){
                        Worker2_BinarySemaphore.doNotify();
                        Results.add("Worker " + 160/W2.Iterations + " notified" );
                    }
                    Worker_Period(W2, Worker2_BinarySemaphore, Worker2_Overrun_Counter, Worker2_Counter);
                }
            }   catch (Exception e) {

            }
            try {
                if(Program_Counter.get() % 4 == 0) {
                    if (W4.state == 1) {
                        Worker4_BinarySemaphore.doNotify();
                        Results.add("Worker " + 160 / W4.Iterations + " notified");
                    }
                    Worker_Period(W4, Worker4_BinarySemaphore, Worker4_Overrun_Counter, Worker4_Counter);
                }
            }   catch (Exception e) {

            }
            try {
                if (Program_Counter.get() % 16 == 0) {
                    if (W16.state == 1) {
                        Worker16_BinarySemaphore.doNotify();
                        Results.add("Worker " + 160 / W16.Iterations + " notified");
                    }
                    Worker_Period(W16, Worker16_BinarySemaphore, Worker16_Overrun_Counter, Worker16_Counter);
                }
            }   catch (Exception e) {

            }
            Results.add("----------------------------------" + (System.nanoTime()-Time)/100000);

            Program_Counter.incrementAndGet();
            // BinarySemaphore ACQUIRE

        } catch (Exception e)   {

        }
    }

};




    private void Worker_Period(Worker worker, BinarySemaphore BinarySemaphore, AtomicInteger OverrunCounter, AtomicInteger ProgramCounter)   {
        if(!worker.isAlive())   {
            try {
                //Results.add("Worker " + 160/worker.Iterations + " Started");
                worker.start();
            }   catch (Exception e)  {

            }
        }
            if (ProgramCounter.get() != 0 && worker.state < 2) {
                OverrunCounter.incrementAndGet();
            }
        BinarySemaphore.doNotify();
        Results.add("Worker " + 160/worker.Iterations + " notified" );
    }



}