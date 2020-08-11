package me.pltzz.rankcolor;

import lombok.Getter;
import me.pltzz.rankcolor.command.ColorsCommand;
import me.pltzz.rankcolor.listener.ConnectionListener;
import me.pltzz.rankcolor.view.ColorsInv;
import me.pltzz.rankcolor.rank.RankColorManager;
import me.pltzz.rankcolor.user.UserCacheStore;
import me.pltzz.rankcolor.user.UserQueue;
import me.saiintbrisson.ErrorReporter;
import me.saiintbrisson.minecraft.InventoryFrame;
import me.saiintbrisson.minecraft.command.CommandFrame;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class RankColorPlugin extends JavaPlugin {

    @Getter
    private static RankColorPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        File errorsFolder = new File(getDataFolder(), "errors");
        if(!errorsFolder.exists()) {
            errorsFolder.mkdir();
        }

        ErrorReporter.getInstance().setOutputDirectory(errorsFolder.toURI());

        new UserCacheStore(this);
        new RankColorManager(this);
        new UserQueue();
        new InventoryFrame(this).registerListener();

        new ColorsInv(this, getConfig().getString("inventory.title"), getConfig().getInt("inventory.rows"));

        CommandFrame commandFrame = new CommandFrame(this);
        commandFrame.register(
          new ColorsCommand()
        );

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(), this);
    }

    @Override
    public void onDisable() {
        UserQueue.getInstance().flushAll();
    }
}
