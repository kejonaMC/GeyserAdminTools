package com.projectg.geyseradmintools.listeners;

import com.projectg.geyseradmintools.database.BanData;
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
    public void checkBan(AsyncPlayerPreLoginEvent e) throws ParseException {
        UUID uuid = e.getUniqueId();
        try {
            String endDate = BanData.infoBan(uuid, "ENDDATE");
            String reason = BanData.infoBan(uuid, "REASON");
            Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            String curDate = LocalDate.now().toString();
            Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(curDate);

            if (dataDate.compareTo(currDate) < 0) {
                BanData.deleteBan(e.getUniqueId());
            } else {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + Messages.get("ban.join.event", endDate, reason));
            }
        } catch (ParseException | NullPointerException ignored) {
        }
    }
}