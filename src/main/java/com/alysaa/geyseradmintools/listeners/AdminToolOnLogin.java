package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdminToolOnLogin implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        try {
            PreparedStatement statement = BanDatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + BanDatabaseSetup.Bantable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String reason = results.getString("Reason");
                String enddate = results.getString("EndDate");
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED
                            + "You are banned till: "+"\n" + ChatColor.WHITE + enddate + ChatColor.RED + "\n Reason: "
                            + ChatColor.WHITE + reason);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}