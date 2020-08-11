package me.pltzz.rankcolor.rank;

import lombok.Getter;
import me.pltzz.rankcolor.RankColorPlugin;
import me.pltzz.rankcolor.user.User;
import me.pltzz.rankcolor.user.UserQueue;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankColorManager {

    @Getter
    private static RankColorManager instance;

    @Getter
    private final List<RankColor> colors;

    public RankColorManager(RankColorPlugin plugin) {
        instance = this;

        colors = new ArrayList<>();

        for (String key : plugin.getConfig().getConfigurationSection("colors").getKeys(false)) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("colors." + key);

            String name = section.getString("name").replace('&', '§');

            String addCommand = section.getString("addCommand");
            String removeCommand = section.getString("removeCommand");

            int slot = section.getInt("slot") - 1;

            String[] stripedMaterial = section.getString("materialData").split(";");

            if(stripedMaterial.length != 2) {
                System.out.println("There is a problem in config.yml. Rank Color: " + name);

                for (String sectionKey : section.getKeys(false)) {
                    System.out.println(sectionKey + "=" + section.getString(sectionKey));
                }

                continue;
            }

            MaterialData materialData = new MaterialData(
              Integer.parseInt(stripedMaterial[0]),
              Byte.parseByte(stripedMaterial[1])
            );

            colors.add(new RankColor(key, name, addCommand, removeCommand, slot, materialData));
        }
    }

    public void addColor(RankColor element) {
        colors.add(element);
    }

    public void addColors(RankColor... elements) {
        colors.addAll(Arrays.asList(elements));
    }

    public RankColor getByName(String name) {
        for (RankColor color : colors) {
            if (color.getName().equalsIgnoreCase(name)) {
                return color;
            }
        }

        return null;
    }

    public void apply(User user, RankColor color) {
        if(user.getRank() != null) {
            RankColor previousColor = getByName(color.getName());

            Bukkit.getServer().dispatchCommand(
              Bukkit.getConsoleSender(),
              previousColor.getRemoveCommand()
                .replace("{player}", user.getName())
                .replace("{color}", color.getName())
            );
        }

        user.setRank(color.getName());

        Bukkit.getServer().dispatchCommand(
          Bukkit.getConsoleSender(),
          color.getAddCommand()
            .replace("{player}", user.getName())
            .replace("{color}", color.getName())
        );

        UserQueue.getInstance().addToQueue(user.getId());
    }
}
