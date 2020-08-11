package me.pltzz.rankcolor.listener;

import me.pltzz.rankcolor.user.User;
import me.pltzz.rankcolor.user.UserCacheStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionListener implements Listener {

    private final ExecutorService service = new ThreadPoolExecutor(
      2, 4,
      15, TimeUnit.SECONDS,
      new LinkedBlockingQueue<>()
    );

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        service.submit(() -> {
            UserCacheStore.getInstance().getOrDefault(
              player.getUniqueId(),
              () -> create(player),
              true
            );
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        service.submit(() -> {
            UUID uniqueId = player.getUniqueId();
            User userModel = UserCacheStore.getInstance().getById(uniqueId);
            if(userModel == null) return;

            UserCacheStore.getInstance().update(
              uniqueId,
              userModel,
              true
            );
        });
    }

    private User create(Player player) {
        return new User(player.getUniqueId(), player.getName());
    }
}
