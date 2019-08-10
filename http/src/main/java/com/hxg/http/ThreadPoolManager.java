package com.hxg.http;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 线程池管理类（队列、线程池）
public class ThreadPoolManager {
    private static final int MAX_RETRY_COUNT = 3;
    private static ThreadPoolManager threadPoolManager;
    //    队列
    private LinkedBlockingQueue<Runnable> mQueue;
    //    存放失败任务的队列
    private DelayQueue<HttpTask> delayQueue;
    //    线程池
    private ThreadPoolExecutor threadPoolExecutor;
    //    "叫号员"线程， 用来让队列和线程池进行交互
    private Runnable coreThread = new Runnable() {
        Runnable runnable = null;

        @Override
        public void run() {
            try {
                while (true) {
                    runnable = mQueue.take();
                    threadPoolExecutor.execute(runnable);
                }
            } catch (Exception ignored) {
            }
        }
    };

    //    "叫号员"线程， 用来让存放失败任务的队列和线程池进行交互
    private Runnable delayThread = new Runnable() {
        HttpTask task = null;

        @Override
        public void run() {
            while (true) {
                try {
                    task = delayQueue.take();
                    int retryCount = task.getRetryCount();
                    if (retryCount < MAX_RETRY_COUNT) {
                        threadPoolExecutor.execute(task);
                        task.setRetryCount(retryCount + 1);
                        Log.e("hxg", "第" + (retryCount + 1) + "次重试： " + System.currentTimeMillis());
                    } else {
                        Log.e("hxg", "已经重试了" + MAX_RETRY_COUNT + "次， 停止重试。");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private ThreadPoolManager() {
        mQueue = new LinkedBlockingQueue<>();
        delayQueue = new DelayQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                        addTask(runnable);
                    }
                });
        threadPoolExecutor.execute(coreThread);
        threadPoolExecutor.execute(delayThread);
    }

    public static ThreadPoolManager getInstance() {
        if (threadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (threadPoolManager == null) {
                    threadPoolManager = new ThreadPoolManager();
                }
            }
        }
        return threadPoolManager;
    }

    // 将异步任务添加到队列
    public void addTask(Runnable task) {
        try {
            if (task == null) return;
            mQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 将异步任务添加到队列
    public void addDelayTask(HttpTask task) {
        if (task == null) return;
        task.setDelayTime(3000);
        delayQueue.offer(task);
    }
}
