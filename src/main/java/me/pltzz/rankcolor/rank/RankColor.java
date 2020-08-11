package me.pltzz.rankcolor.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.material.MaterialData;

@Getter
@AllArgsConstructor
public class RankColor {

    private final String name, friendlyName, addCommand, removeCommand;
    private final int slot;
    private final MaterialData materialData;
}
