package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class AdminToolInventory  implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("geyseradmintools.gadmin")) {
                if (Gat.plugin.getConfig().getBoolean("DisableItemMove")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("geyseradmintools.gadmin")) {
                Player p = e.getPlayer();
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (p.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                        MainForm.formList();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("geyseradmintools.gadmin")) {
                if (Gat.plugin.getConfig().getBoolean("DisableItemDrop")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
