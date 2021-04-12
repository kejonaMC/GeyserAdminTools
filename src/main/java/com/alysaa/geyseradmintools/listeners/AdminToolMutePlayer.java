package com.alysaa.geyseradmintools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminToolMutePlayer implements Listener {
    public List<Player> bannedFromChat = new ArrayList<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws SQLException {
        for (Player s : this.bannedFromChat) {
            Player p = event.getPlayer();
            if (s.equals(p.getName())) {
                event.setCancelled(true);
                p.sendMessage("[GeyserAdminTool] You have been muted, you can not chat.");
            }
        }
    }
}

