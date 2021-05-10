package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.database.DatabaseSetup;
import com.projectg.geyseradmintools.language.Messages;
import org.bukkit.ChatColor;
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
            e.getPlayer().sendMessage(ChatColor.GOLD + "shhhhhhhhh!");
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void checkMute(AsyncPlayerChatEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        try {
            PreparedStatement statement = DatabaseSetup.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseSetup.muteTable + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String endDate = results.getString("EndDate");
                Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                String curDate = LocalDate.now().toString();
                Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(curDate);
                if (dataDate.compareTo(currDate) > 0) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.GOLD + Messages.get("mute.join.text1") + endDate);
                } else if (dataDate.compareTo(currDate) < 0) {
                    try {
                        statement = DatabaseSetup.getConnection()
                                .prepareStatement("DELETE FROM " + DatabaseSetup.muteTable + " WHERE UUID=?");
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
