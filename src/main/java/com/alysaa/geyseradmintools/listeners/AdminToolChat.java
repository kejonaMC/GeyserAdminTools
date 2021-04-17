package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdminToolChat implements Listener {

    public static boolean isMuted = false;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (isMuted) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("shhhhhhhhh!");
        }
    }
    @EventHandler
    public void onMute(AsyncPlayerChatEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        try {
            PreparedStatement statement = BanDatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + MuteDatabaseSetup.Mutetable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String reason = results.getString("Reason");
                e.setCancelled(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
