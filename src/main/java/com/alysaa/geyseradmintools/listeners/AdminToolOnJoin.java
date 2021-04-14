package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminToolOnJoin implements Listener {

    // private final ItemStack starTool = Gat.plugin.getStarTool();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
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
        ItemStack starTool = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta starToolMeta = starTool.getItemMeta();
        starToolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Admin Tools"));
        starTool.setItemMeta(starToolMeta);


        if (player.getInventory().contains(starTool)) {
            return;
        }
        // Either we create a copy of the array that is shorter or just use a for loop to only access the itemstacks of the hotbar
        ItemStack[] wholeInventory = e.getPlayer().getInventory().getContents();
        boolean success = false;
        for (int slot = 0; slot < 9; slot++) {
            if (wholeInventory[slot] == null) {
                System.out.println("slot number " + slot + " is null");
                e.getPlayer().getInventory().setItem(slot, starTool);
                success = true;
                break;
            }
            System.out.println("slot number " + slot + " is not null");
        }
        if (!success) {
            e.getPlayer().sendMessage(ChatColor.RED + "Make room in your hotbar for the star tool!");
        }
    }
}
