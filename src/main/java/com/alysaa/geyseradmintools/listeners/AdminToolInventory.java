package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdminToolInventory  implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("geyseradmintools.item")) {
                return;
            }
            FileConfiguration config = Gat.plugin.getConfig();
            if (!config.getBoolean("DisableItemMove")) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("geyseradmintools.item")) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (player.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                MainForm.formList();
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("geyseradmintools.item")) {
            return;
        }
        FileConfiguration config = Gat.plugin.getConfig();
        if (!config.getBoolean("DisableItemDrop")) {
            return;
        }
            e.setCancelled(true);
    }
}
