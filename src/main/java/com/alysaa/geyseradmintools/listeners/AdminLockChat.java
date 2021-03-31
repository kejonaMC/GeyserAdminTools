package com.alysaa.geyseradmintools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AdminLockChat implements Listener {
    public static boolean isMuted = false;
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(isMuted) {
            e.setCancelled(true);
        }
        if(e.getPlayer().hasPermission("geyseradmintools.gadmin")) {
            e.setCancelled(false);
        }
        else {
            e.setCancelled(false);
        }
    }
}
