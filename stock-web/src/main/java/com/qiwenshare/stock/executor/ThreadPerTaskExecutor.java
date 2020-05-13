package com.qiwenshare.stock.executor;

import java.util.concurrent.Executor;

/**
 * 为每个请求启动一个新线程的Executor
 * （学习用例，不建议这样用，应使用线程池）
 */
public class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
