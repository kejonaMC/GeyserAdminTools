package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.MySql;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdminToolOnLogin implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        try {
            PreparedStatement statement = MySql.getConnection()
                    .prepareStatement("SELECT * FROM " + MySql.Bantable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            while (results.next()) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);

            }
        } catch (SQLException exe) {
            exe.printStackTrace();
        }
    }

}