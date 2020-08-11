package me.pltzz.rankcolor.user;

import lombok.Getter;
import me.pltzz.rankcolor.RankColorPlugin;
import me.saiintbrisson.minecraft.boilerplate.Dao;
import me.saiintbrisson.minecraft.boilerplate.common.AbstractCacheStore;

import java.util.UUID;

public class UserCacheStore extends AbstractCacheStore<UUID, User> {

    @Getter
    private static UserCacheStore instance;

    private final UserDataSourceDao dataSourceDao;
    private final UserInMemoryDao inMemoryDao;

    public UserCacheStore(RankColorPlugin plugin) {
        instance = this;

        this.dataSourceDao = new UserDataSourceDao(plugin);
        this.inMemoryDao = new UserInMemoryDao();
    }


    @Override
    protected Dao<UUID, User> getBackingInMemoryDao() {
        return inMemoryDao;
    }

    @Override
    protected Dao<UUID, User> getBackingDataSourceDao() {
        return dataSourceDao;
    }
}
