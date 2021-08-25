package org.github.hoorf.dbboot.migrate.core.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;

public class ExecuteEngine {

    public static final String THREAD_NAME_FORMAT = "migrate-execute-%d";


    private ExecutorService executorService;

    public ExecuteEngine(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static ExecuteEngine newCachedThreadPool() {
        return new ExecuteEngine(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat(THREAD_NAME_FORMAT).build()));
    }

    public static ExecuteEngine newFixExecutorService(int workThreads) {
        return new ExecuteEngine(Executors.newFixedThreadPool(workThreads));
    }

    public static ExecuteEngine getFixExecutorService(int workThreads, String name) {
        return new ExecuteEngine(Executors.newFixedThreadPool(workThreads, new ThreadFactoryBuilder().setDaemon(true).setNameFormat(THREAD_NAME_FORMAT).build()));
    }

    public void submit(MigrateExecutor executor, Consumer<Void> success, Consumer<Throwable> error) {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(executor, executorService);
        completableFuture.exceptionally(e -> {
            error.accept(e);
            return null;
        });
        completableFuture.thenAccept(success);
    }

    public void submit(MigrateExecutor executor, Consumer<Throwable> error) {
        submit(executor, null, error);
    }


}
