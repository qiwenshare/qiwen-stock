package com.qiwenshare.stock.executor;

import java.util.concurrent.Executor;

/**
 * 在调用线程中以同步方式执行所有任务的Executor
 * （学习用例，不建议这样用，应使用线程池）
 */
public class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
