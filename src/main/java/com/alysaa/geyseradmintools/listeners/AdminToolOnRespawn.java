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
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("geyseradmintools.gadmin")){
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
        ItemStack nether = new ItemStack(Material.NETHER_STAR);
        ItemMeta netherMeta = nether.getItemMeta();
        netherMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Admin Tools"));
        nether.setItemMeta(netherMeta);
        player.getInventory().setItem(Gat.plugin.getConfig().getInt("Slot"), nether);
    }
}