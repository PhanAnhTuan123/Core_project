package com.tuanPhan.coroutine;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class CoroutineLikeManager {
    private final ExecutorService executor;

    public CoroutineLikeManager() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public <T>CompletableFuture<T> launch(Supplier<T> syncTask) {
        return CompletableFuture.supplyAsync(syncTask, executor);
    }

    public <T, R> CompletableFuture<R> chain(CompletableFuture<T> future, Function<T, R> nextSyncTask) {
        return future.thenApply(nextSyncTask);
    }

    public void scope(Runnable... tasks) {
        CompletableFuture<?>[] futures = new CompletableFuture[tasks.length];
        for(int i = 0; i < tasks.length; i++) {
            futures[i] = CompletableFuture.runAsync(tasks[i], executor);
        }
        CompletableFuture.allOf(futures).join();
    }

    public void shutdown() {
        executor.shutdown();
    }


}
