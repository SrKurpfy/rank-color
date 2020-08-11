package me.pltzz.rankcolor.view;

import lombok.Getter;
import lombok.NonNull;
import me.pltzz.rankcolor.rank.RankColor;
import me.pltzz.rankcolor.rank.RankColorManager;
import me.pltzz.rankcolor.user.User;
import me.saiintbrisson.minecraft.ItemBuilder;
import me.saiintbrisson.minecraft.view.View;
import me.saiintbrisson.minecraft.view.ViewAction;
import me.saiintbrisson.minecraft.view.ViewNode;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ColorsInv extends View<User> {

    @Getter
    private static ColorsInv instance;

    public ColorsInv(@NonNull Plugin owner, String title, int rows) {
        super(owner, title, rows);

        instance = this;
    }

    @Override
    protected void render(ViewNode<User> node, Player player, User user) {
        for (RankColor color : RankColorManager.getInstance().getColors()) {
            if(color.getName().equalsIgnoreCase(user.getRank())) {
                node.slot(color.getSlot())
                  .withItem(alreadySelectedItem(color))
                  .updateOnClick();
            } else {
                node.slot(color.getSlot())
                  .withItem(selectColorItem(color))
                  .onClick(selectColorAction(color))
                  .updateOnClick()
                  .messageOnClick("§aYour rank color has been set to " + color.getFriendlyName() + "§a.")
                  .playSoundOnClick(Sound.ENTITY_PLAYER_LEVELUP, 3f, 3f);;
            }
        }
    }

    private ViewAction<User, InventoryClickEvent> selectColorAction(RankColor color) {
        return (node, player, event) -> RankColorManager.getInstance().apply(node.getObject(), color);
    }

    private ItemStack alreadySelectedItem(RankColor color) {
        return new ItemBuilder(color.getMaterialData().getItemType(), 1, color.getMaterialData().getData())
          .name(ChatColor.GRAY + "Rank color: " + color.getFriendlyName())
          .lore(
            "§7Color selected."
          )
          .addFlags(ItemFlag.HIDE_ENCHANTS)
          .enchantment(Enchantment.ARROW_DAMAGE, 1)
          .build();
    }

    private ItemStack selectColorItem(RankColor color) {
        return new ItemBuilder(color.getMaterialData().getItemType(), 1, color.getMaterialData().getData())
          .name(ChatColor.GRAY + "Rank color: " + color.getFriendlyName())
          .lore(
            "§aClick to select this color."
          )
          .build();
    }
}
