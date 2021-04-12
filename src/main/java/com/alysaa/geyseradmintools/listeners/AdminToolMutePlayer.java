package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.MySql;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class AdminToolMutePlayer implements Listener {
  public List<Player> bannedFromChat = new ArrayList<>();
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        for (Player s : this.bannedFromChat) {
            if (s.equals(p.getName())) {
                event.setCancelled(true);
                p.sendMessage("[GeyserAdminTool] You have been muted, you can not chat.");
            }
        }

    }
}
