package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Forms.MainForm;
import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminToolOnJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Gat.plugin.getConfig().getBoolean("ItemJoin")) {
            boolean isFloodgatePlayer = CheckJavaOrFloodPlayer.isFloodgatePlayer(e.getPlayer().getUniqueId());
            if (isFloodgatePlayer) {
                Player player = e.getPlayer();
                ItemStack nether = new ItemStack(Material.NETHER_STAR);
                ItemMeta netherMeta = nether.getItemMeta();
                netherMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Admin Tools"));
                nether.setItemMeta(netherMeta);
                player.getInventory().setItem(Gat.plugin.getConfig().getInt("Slot"), nether);
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (p.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                MainForm.formList();
            }
        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (Gat.plugin.getConfig().getBoolean("DisableItemDrop")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Gat.plugin.getConfig().getBoolean("DisableItemMove")) {
            event.setCancelled(true);
        }
    }
}
