package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.Gat;
import com.alysaa.geyseradmintools.utils.CheckJavaOrFloodPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminToolOnRespawn implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("geyseradmintools.gadmin")) {
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

        }
    }
}
