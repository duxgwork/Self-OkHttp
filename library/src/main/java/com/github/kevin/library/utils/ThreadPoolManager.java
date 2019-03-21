package com.github.kevin.library.utils;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static final String TAG = "ThreadPoolManager";
    private static volatile ThreadPoolManager instance;

    private ThreadPoolManager(){
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10,15, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //处理任务执行失败后抛出来的任务
                addTast(r);
            }
        });
        mThreadPoolExecutor.execute(communicateThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    public static ThreadPoolManager getInstance(){
        if (instance == null){
            synchronized (ThreadPoolManager.class){
                if (instance == null){
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    // 1 创建队列，保存异步请求任务
    // LinkedBlockingDeque是一个双端队列，任何一端都可以进行元素的出入。
    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque<>();

    // 2 添加异步任务到队列中
    public void addTast(Runnable runnable){
        if (runnable != null){
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 3 创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    // 4 创建队列与线程池之间的交互线程
    private Runnable communicateThread = new Runnable() {
        @Override
        public void run() {
            Runnable runnable = null;
            while (true){
                try {
                    runnable = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runnable);
            }
        }
    };

    // 5 创建延迟（重试）队列
    //DelayQueue是一个支持延时获取元素的无界阻塞队列，在创建元素时可以指定多久才能从队列中获取当前元素，
    //只有在延迟期满时才能从队列中提取元素，如果元素没有达到延时时间，就阻塞当前线程。
    //需要注意的是，添加到该队列中的元素必须实现Delayed接口。
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    // 6 创建延迟队列与线程池之间的交互线程
    private Runnable delayThread = new Runnable() {
        @Override
        public void run() {
            HttpTask task = null;
            while (true){
                try {
                    task = mDelayQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task.getRetryCount() < 3){
                    mThreadPoolExecutor.execute(task);
                    task.setRetryCount(task.getRetryCount() + 1);
                    Log.e(TAG, "重试机制：" + task.getRetryCount());
                } else {
                    Log.e(TAG, "重试机制：执行次数超限！");
                }
            }
        }
    };

    // 7 添加异步任务到延迟队列中
    public void addDelayTask(HttpTask task){
        if (task != null){
            task.setDelayTime(3000);
            mDelayQueue.offer(task);
        }
    }

}
