package com.company;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BinarySemaphore {

    private ReentrantLock lock;
    private int count;
    private Condition cv;


    BinarySemaphore(int count)
    {
        this.lock = new ReentrantLock();
        this.count = count;
        this.cv = this.lock.newCondition();
    }


    public void doNotify()
    {
        lock.unlock();
        ++count;
        cv.notify();
    }

    void doWait() throws InterruptedException
    {
        lock.lock();
        while(count == 0)
        {
            cv.wait();
        }
        --count;
    }


}