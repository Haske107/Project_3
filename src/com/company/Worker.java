package com.company;


import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker extends Thread implements Runnable  {

    private AtomicInteger Worker_Counter;
    private BinarySemaphore Semaphore;
    public int Iterations;
    private double[][] matrix;

    Worker(int Iterations, BinarySemaphore semaphore, AtomicInteger Worker_Counter)   {
        this.Iterations = Iterations;
        this.Semaphore = semaphore;
        this.Worker_Counter = Worker_Counter;
        matrix = new double[1000][1000];
        for (int i = 0; i < 1000; ++i) {
            for (int j = 0; j < 1000; ++j) {
                matrix[i][j] = 1;
            }
        }
    }

    @Override
    public void start() {
        System.out.println("Worker " + 160/Iterations + " Started");
        run();
    }



    @Override
    public void run() {
        try {
            for (int i = 0; i < Iterations; ++i) {
                Semaphore.acquire();
                System.out.println("Worker " + 160/Iterations + " Acquired");
                for(int j = 0; j < 160 / Iterations; ++j)    {
                    doWork();
                    Worker_Counter.incrementAndGet();
                    System.out.println("Worker " + 160/Iterations + " Working");
                }
            }
        } catch (Exception e) {
        } finally {
        }
    }

    private void doWork()    {
        for (int i = 0; i < 100; ++i) {
            matrix[new Random().nextInt(100)][new Random().nextInt(100)]
                    = matrix[new Random().nextInt(100)][new Random().nextInt(100)] * 5.2323223586654321;
        }
    }
}