package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.language.Messages;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class AdminToolOnLogin implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void checkBan(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String endDate = results.getString("EndDate");
                Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                String curDate = LocalDate.now().toString();
                Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(curDate);
                if (dataDate.compareTo(currDate) > 0) {
                    return;
                } else if (dataDate.compareTo(currDate) < 0) {
                    try {
                        statement = DatabaseSetup.getConnection()
                                .prepareStatement("DELETE FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
                        statement.setString(1, e.getUniqueId().toString());
                        statement.execute();

                    } catch (SQLException exe) {
                        exe.printStackTrace();
                    }
                }

            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.banTable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String reason = results.getString("Reason");
                String endDate = results.getString("EndDate");
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + Messages.get("ban.join.event",endDate,reason));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}