package com.alysaa.geyseradmintools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AdminLockChat implements Listener {

    public static boolean isMuted = false;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (isMuted) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("shhhhhhhhh!");
        }
    }
}
