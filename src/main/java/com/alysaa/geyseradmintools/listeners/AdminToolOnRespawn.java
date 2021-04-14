package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminToolOnRespawn implements Listener {

    private final ItemStack starTool = Gat.getStarTool();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("geyseradmintools.item")){
            return;
        }
        FileConfiguration config = Gat.plugin.getConfig();
        if (!config.getBoolean("ItemJoin")){
            return;
        }
        boolean isFloodGatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(e.getPlayer().getUniqueId());
        if (!isFloodGatePlayer) {
            return;
        }
        if (player.getInventory().contains(starTool)) {
            return;
        }
        // Either we create a copy of the array that is shorter or just use a for loop to only access the itemstacks of the hotbar
        ItemStack[] wholeInventory = e.getPlayer().getInventory().getContents();
        boolean success = false;
        for (int slot = 0; slot < 9; slot++) {
            if (wholeInventory[slot] == null) {
                e.getPlayer().getInventory().setItem(slot, starTool);
                success = true;
                break;
            }
        }
        if (!success) {
            e.getPlayer().sendMessage(ChatColor.RED + "Make room in your hotbar for the star tool!");
        }
    }
}