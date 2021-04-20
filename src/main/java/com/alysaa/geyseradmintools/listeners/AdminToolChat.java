package com.alysaa.geyseradmintools.listeners;

import com.alysaa.geyseradmintools.database.MuteDatabaseSetup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void checkMute(AsyncPlayerChatEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        try {
            PreparedStatement statement = MuteDatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + MuteDatabaseSetup.Mutetable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String enddate = results.getString("EndDate");
                Date datadate = new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
                String curdate = LocalDate.now().toString();
                Date currdate = new SimpleDateFormat("yyyy-MM-dd").parse(curdate);
                if (datadate.compareTo(currdate) > 0) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("You are still muted. Mute will lift on: " + enddate);
                } else if (datadate.compareTo(currdate) < 0) {
                    try {
                        statement = MuteDatabaseSetup.getConnection()
                                .prepareStatement("DELETE FROM " + MuteDatabaseSetup.Mutetable + " WHERE UUID=?");
                        statement.setString(1, e.getPlayer().getUniqueId().toString());
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
}
