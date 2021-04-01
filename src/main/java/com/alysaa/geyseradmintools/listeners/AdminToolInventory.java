package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdminToolInventory  implements Listener {
    Player players;
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (players.hasPermission("geyseradmintools.gadmin")) {
            if (Gat.plugin.getConfig().getBoolean("DisableItemMove")) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (players.hasPermission("geyseradmintools.gadmin")) {
            Player p = e.getPlayer();
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (p.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                    MainForm.formList();
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (players.hasPermission("geyseradmintools.gadmin")) {
            if (Gat.plugin.getConfig().getBoolean("DisableItemDrop")) {
                event.setCancelled(true);
            }
        }
    }
}
