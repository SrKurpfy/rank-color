package me.pltzz.rankcolor.user;

import lombok.Getter;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class UserQueue {

    @Getter
    private static UserQueue instance;

    private final Queue<UUID> queue;

    public UserQueue() {
        instance = this;
        queue = new LinkedBlockingQueue<>();
    }

    public void addToQueue(UUID id) {
        if (!queue.contains(id))
            queue.add(id);
    }

    public void removeFromQueue(UUID id) {
        queue.remove(id);
    }

    public void flush() {
        UUID id = queue.poll();

        User user = UserCacheStore.getInstance().getById(id);
        if (user != null) {
            UserCacheStore.getInstance().update(user.getId(), user);
        }
    }

    public void flushAll() {
        while (!queue.isEmpty()) {
            flush();
        }
    }

    public void startQueue() {
        Executors.newSingleThreadScheduledExecutor()
          .scheduleWithFixedDelay(this::flushAll, 1L, 1L, TimeUnit.MINUTES);
    }
}
