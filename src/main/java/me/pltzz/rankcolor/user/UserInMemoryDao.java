package me.pltzz.rankcolor.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.saiintbrisson.minecraft.boilerplate.Dao;

import java.util.Collection;
import java.util.UUID;

public class UserInMemoryDao implements Dao<UUID, User> {

    private final Cache<UUID, User> cache = CacheBuilder.newBuilder()
      .build();

    @Override
    public User getById(UUID key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Collection<User> getAll() {
        return cache.asMap().values();
    }

    @Override
    public boolean save(UUID key, User element) {
        cache.put(key, element);

        return true;
    }

    @Override
    public boolean update(UUID key, User element) {
        return false;
    }

    @Override
    public boolean delete(UUID key) {
        cache.invalidate(key);

        return true;
    }
}
