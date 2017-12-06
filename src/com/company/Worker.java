package com.company;


import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker extends Thread implements Runnable  {

    private AtomicInteger Worker_Counter;
    private BinarySemaphore BinarySemaphore;
    public int state = 0;
    public int Iterations;
    private double[][] matrix;
    private List<String> Results;

    Worker(int Iterations, BinarySemaphore BinarySemaphore, AtomicInteger Worker_Counter, List<String> Results)   {
        this.Iterations = Iterations;
        this.BinarySemaphore = BinarySemaphore;
        this.Worker_Counter = Worker_Counter;
        this.Results = Results;
        matrix = new double[10000][10000];
        for (int i = 0; i < 10000; ++i) {
            for (int j = 0; j < 10000; ++j) {
                matrix[i][j] = 1;
            }
        }
    }

    @Override
    public void start() {

        run();
    }



    @Override
    public void run() {
        try {
            for (int i = 0; i < Iterations; ++i) {
                Results.add("Worker " + 160/Iterations + " Waiting");
                BinarySemaphore.doWait();
                state = 1;
                Results.add("Worker " + 160/Iterations + " Acquired");
                for(int j = 0; j < 160 / Iterations; ++j)    {
                    doWork();
                    Results.add("Worker " + 160/Iterations + " Working");
                }
                Worker_Counter.incrementAndGet();

                state = 2;
                Results.add("Worker " + 160/Iterations + " Done ");
            }
        } catch (Exception e) {

        }
    }

    private void doWork()    {
        for (int i = 0; i < 10000; ++i) {
            matrix[new Random().nextInt(100)][new Random().nextInt(100)]
                    = matrix[new Random().nextInt(100)][new Random().nextInt(100)] * 5.2323223586654321;
        }
    }
}