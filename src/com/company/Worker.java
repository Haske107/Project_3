package com.company;


import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker extends Thread implements Runnable  {

    private AtomicInteger Worker_Counter;
    private BinarySemaphore BinarySemaphore;
    public boolean isDone = false;
    public int Iterations;
    private double[][] matrix;
    private List<String> Results;

    Worker(int Iterations, BinarySemaphore BinarySemaphore, AtomicInteger Worker_Counter, List<String> Results)   {
        this.Iterations = Iterations;
        this.BinarySemaphore = BinarySemaphore;
        this.Worker_Counter = Worker_Counter;
        this.Results = Results;
        matrix = new double[1000][1000];
        for (int i = 0; i < 1000; ++i) {
            for (int j = 0; j < 1000; ++j) {
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
                Results.add("Worker " + 160/Iterations + " Acquired");
                for(int j = 0; j < 160 / Iterations; ++j)    {
                    doWork();
                    Results.add("Worker " + 160/Iterations + " Working");
                }
                Worker_Counter.incrementAndGet();

                isDone = true;
                Results.add("Worker " + 160/Iterations + " Done ");
            }
        } catch (Exception e) {
        } finally {

        }
    }

    private void doWork()    {
        for (int i = 0; i < 100000; ++i) {
            matrix[new Random().nextInt(100)][new Random().nextInt(100)]
                    = matrix[new Random().nextInt(100)][new Random().nextInt(100)] * 5.2323223586654321;
        }
    }
}