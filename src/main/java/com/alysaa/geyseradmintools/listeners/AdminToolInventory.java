package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class AdminToolInventory  implements Listener {

    FileConfiguration config = Gat.plugin.getConfig();
    private final ItemStack starTool = Gat.getStarTool();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (config.getBoolean("DisableItemMove")) {
            if (e.getCurrentItem().equals(starTool)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("geyseradmintools.item")) {
            if (player.getInventory().getItemInMainHand().equals(starTool) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                MainForm.formList(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (config.getBoolean("DisableItemDrop")) {
            // TODO fix this
            if (e.getItemDrop().equals(starTool)) {
                e.setCancelled(true);
            }
        }
    }
}
