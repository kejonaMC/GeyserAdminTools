package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.BanDatabaseSetup;
import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
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
            PreparedStatement statement = BanDatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + BanDatabaseSetup.Bantable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String enddate = results.getString("EndDate");
                Date datadate = new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
                String curdate = LocalDate.now().toString();
                Date currdate = new SimpleDateFormat("yyyy-MM-dd").parse(curdate);
                if (datadate.compareTo(currdate) > 0) {
                    return;
                } else if (datadate.compareTo(currdate) < 0) {
                    try {
                        statement = MuteDatabaseSetup.getConnection()
                                .prepareStatement("DELETE FROM " + BanDatabaseSetup.Bantable + " WHERE UUID=?");
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