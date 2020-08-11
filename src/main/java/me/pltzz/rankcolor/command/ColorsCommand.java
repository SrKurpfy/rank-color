package me.pltzz.rankcolor.command;

import me.pltzz.rankcolor.view.ColorsInv;
import me.pltzz.rankcolor.rank.RankColor;
import me.pltzz.rankcolor.rank.RankColorManager;
import me.pltzz.rankcolor.user.User;
import me.pltzz.rankcolor.user.UserCacheStore;
import me.saiintbrisson.minecraft.command.Execution;
import me.saiintbrisson.minecraft.command.annotations.Command;
import me.saiintbrisson.minecraft.command.annotations.CommandTarget;
import me.saiintbrisson.minecraft.command.argument.Argument;
import org.bukkit.entity.Player;

public class ColorsCommand {

    @Command(
      name = "colors",
      usage = "colors [name]",
      aliases = "color",
      target = CommandTarget.PLAYER,
      permission = "nrc.rankcolor"
    )
    public void colors(Execution execution, @Argument(nullable = true) String colorName) {
        Player player = execution.getPlayer();

        if(colorName == null) {
            User user = UserCacheStore.getInstance().getById(player.getUniqueId());
            if(user == null) {
                execution.sendMessage("§cAn internal error occurred. Please call any admin!");
                return;
            }

            ColorsInv.getInstance().createNode(user).show(player);

        } else {
            RankColor color = RankColorManager.getInstance().getByName(colorName);
            if(color == null) {
                execution.sendMessage("§cThere is no color with name %s!", colorName);
                return;
            }

            User user = UserCacheStore.getInstance().getById(player.getUniqueId());
            if(user == null) {
                execution.sendMessage("§cYou do not have an account associated in your account, please relogin!");
                return;
            }

            if(color.getName().equalsIgnoreCase(user.getRank())) {
                execution.sendMessage("§cThis color is already selected.");
                return;
            }

            RankColorManager.getInstance().apply(user, color);
            execution.sendMessage("§aYour rank color has been set to " + color.getFriendlyName() + "§a.");
        }
    }
}