package org.github.hoorf.dbboot.migrate.core.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;

@Slf4j
public class ExecuteEngine {

    public static final String THREAD_NAME_FORMAT = "migrate-execute-%d";

    private List<CompletableFuture> futureList = new ArrayList<>();

    private ExecutorService executorService;

    public ExecuteEngine(ExecutorService executorService) {
        this.executorService = executorService;
        startTimer();
    }

    public static ExecuteEngine newCachedThreadPool() {

        ExecutorService pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat(THREAD_NAME_FORMAT).build(),
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    log.error("-----------full:{}--------------", r.getClass().getName());
                }
            });

        return new ExecuteEngine(pool);
    }

    public static ExecuteEngine newFixExecutorService(int workThreads) {
        return new ExecuteEngine(Executors.newFixedThreadPool(workThreads));
    }

    public static ExecuteEngine getFixExecutorService(int workThreads, String name) {
        return new ExecuteEngine(Executors.newFixedThreadPool(workThreads, new ThreadFactoryBuilder().setDaemon(true).setNameFormat(THREAD_NAME_FORMAT).build()));
    }

    public void submit(MigrateExecutor executor, Consumer<Void> success, Consumer<Throwable> error) {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(executor, executorService);
        if (null != error) {
            completableFuture.exceptionally(e -> {
                error.accept(e);
                return null;
            });
        }
        if (null != success) {
            completableFuture.thenAccept(success);
        }

        futureList.add(completableFuture);
    }

    public void submit(MigrateExecutor executor, Consumer<Throwable> error) {
        submit(executor, null, error);
    }

    public void close() {
        executorService.shutdown();
    }

    public void awaitFinish() {
        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;
                log.info("getActiveCount:{},getTaskCount:{}", pool.getActiveCount(), pool.getTaskCount());
            }
        }, 0L, 1000L);
    }

}
