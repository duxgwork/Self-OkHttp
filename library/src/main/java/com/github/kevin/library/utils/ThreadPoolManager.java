package com.github.kevin.library.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
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

    // 5 创建延迟队列







}
